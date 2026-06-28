import { Component, OnInit, ChangeDetectorRef, ChangeDetectionStrategy } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { DatePipe, DecimalPipe } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { OrganisationService } from '../../services/organisation.service';
import { ItemService } from '../../services/item.service';
import { Organisation } from '../../models/organisation.model';
import { Item } from '../../models/item.model';
import { User } from '../../models/auth.model';

interface Toast {
  id: number;
  type: 'success' | 'error' | 'info';
  message: string;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [ReactiveFormsModule, FormsModule, DatePipe, DecimalPipe],
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  currentUser: User | null = null;
  activeTab: 'items' | 'organisations' = 'items';
  
  // Data
  organisations: Organisation[] = [];
  items: Item[] = [];
  selectedOrgId: string = '';
  activeOrgName: string = 'Select Organisation';

  // Search & Filter
  itemSearchQuery: string = '';
  itemSortKey: 'name' | 'rate' | 'createdAt' = 'name';
  itemSortAsc: boolean = true;

  // Forms
  orgForm: FormGroup;
  editOrgForm: FormGroup;
  itemForm: FormGroup;
  editItemForm: FormGroup;

  // Modal States
  showAddOrgModal = false;
  showEditOrgModal = false;
  editingOrg: Organisation | null = null;

  showAddItemModal = false;
  showEditItemModal = false;
  editingItem: Item | null = null;

  // Feedback State
  loadingOrgs = false;
  loadingItems = false;
  submitting = false;
  toasts: Toast[] = [];
  private toastIdCounter = 0;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private orgService: OrganisationService,
    private itemService: ItemService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {
    this.currentUser = this.authService.currentUser;

    this.orgForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(255)]]
    });

    this.editOrgForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(255)]]
    });

    this.itemForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(255)]],
      rate: [0, [Validators.required, Validators.min(0)]]
    });

    this.editItemForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(255)]],
      rate: [0, [Validators.required, Validators.min(0)]]
    });
  }

  ngOnInit(): void {
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login']);
      return;
    }
    this.loadOrganisations();
  }

  // Toast System
  showToast(message: string, type: 'success' | 'error' | 'info' = 'success'): void {
    const id = ++this.toastIdCounter;
    this.toasts.push({ id, type, message });
    setTimeout(() => {
      this.toasts = this.toasts.filter(t => t.id !== id);
    }, 4000);
  }

  // Organizations Methods
  loadOrganisations(selectFirstIfNeeded: boolean = true): void {
    this.loadingOrgs = true;
    this.orgService.getAll().subscribe({
      next: (res) => {
        if (res.success && res.data) {
          this.organisations = res.data;
          this.cdr.markForCheck();
          
          // Determine which org should be selected
          const storedOrgId = this.authService.activeOrganisationId;
          const stillExists = this.organisations.some(o => o.id === storedOrgId);

          if (storedOrgId && stillExists) {
            this.selectedOrgId = storedOrgId;
          } else if (this.organisations.length > 0 && selectFirstIfNeeded) {
            // Default to primary or first organisation
            const primary = this.organisations.find(o => o.isPrimary);
            this.selectedOrgId = primary?.id || this.organisations[0].id || '';
            this.authService.setOrganisationId(this.selectedOrgId);
          } else if (this.organisations.length === 0) {
            this.selectedOrgId = '';
            this.authService.setOrganisationId(null);
            this.items = [];
          }

          // Set display name
          const selected = this.organisations.find(o => o.id === this.selectedOrgId);
          this.activeOrgName = selected ? selected.name : 'Select Organisation';

          if (this.selectedOrgId) {
            this.loadItems();
          }
        } else {
          this.showToast(res.message || 'Failed to load organisations', 'error');
        }
        this.loadingOrgs = false;
      },
      error: (err) => {
        this.showToast(err.error?.message || 'Error loading organisations', 'error');
        this.loadingOrgs = false;
      }
    });
  }

  onOrgChange(orgId: string): void {
    this.selectedOrgId = orgId;
    this.authService.setOrganisationId(orgId);
    
    const selected = this.organisations.find(o => o.id === orgId);
    this.activeOrgName = selected ? selected.name : 'Select Organisation';
    
    if (orgId) {
      this.loadItems();
    } else {
      this.items = [];
    }
    this.showToast(`Switched workspace to ${this.activeOrgName}`, 'info');
  }

  onCreateOrg(): void {
    if (this.orgForm.invalid) {
      this.orgForm.markAllAsTouched();
      return;
    }

    this.submitting = true;
    this.orgService.create(this.orgForm.value).subscribe({
      next: (res) => {
        if (res.success && res.data) {
          const newOrg = res.data;
          this.showToast(`Organisation "${newOrg.name}" created!`);
          this.showAddOrgModal = false;
          this.orgForm.reset();
          
          if (newOrg.id) {
            this.authService.setOrganisationId(newOrg.id);
            this.selectedOrgId = newOrg.id;
            this.activeOrgName = newOrg.name;
          }
          this.organisations = [...this.organisations, newOrg];
          this.cdr.markForCheck();
          this.cdr.detectChanges();
          this.loadOrganisations();
        } else {
          this.showToast(res.message || 'Failed to create organisation', 'error');
        }
        this.submitting = false;
      },
      error: (err) => {
        this.showToast(err.error?.message || 'Error creating organisation', 'error');
        this.submitting = false;
      }
    });
  }

  openEditOrgModal(org: Organisation): void {
    this.editingOrg = org;
    this.editOrgForm.patchValue({
      name: org.name
    });
    this.showEditOrgModal = true;
  }

  onUpdateOrg(): void {
    if (this.editOrgForm.invalid || !this.editingOrg?.id) {
      return;
    }

    this.submitting = true;
    this.orgService.update(this.editingOrg.id, this.editOrgForm.value).subscribe({
      next: (res) => {
        if (res.success) {
          this.showToast('Organisation updated successfully');
          this.showEditOrgModal = false;
          if (res.data) {
            const updatedOrg = res.data;
            this.organisations = this.organisations.map(org => org.id === updatedOrg.id ? updatedOrg : org);
            if (this.selectedOrgId === updatedOrg.id) {
              this.activeOrgName = updatedOrg.name;
            }
            this.cdr.markForCheck();
            this.cdr.detectChanges();
          }
          this.loadOrganisations(false);
        } else {
          this.showToast(res.message || 'Failed to update organisation', 'error');
        }
        this.submitting = false;
      },
      error: (err) => {
        this.showToast(err.error?.message || 'Error updating organisation', 'error');
        this.submitting = false;
      }
    });
  }

  onDeleteOrg(org: Organisation): void {
    if (!org.id) return;
    if (org.isPrimary) {
      this.showToast('Cannot delete your primary organisation', 'error');
      return;
    }
    if (confirm(`Are you sure you want to delete "${org.name}"? This action cannot be undone.`)) {
      this.orgService.delete(org.id).subscribe({
        next: (res) => {
          if (res.success) {
            this.showToast('Organisation deleted successfully');
            if (this.selectedOrgId === org.id) {
              // If deleted active org, load organizations and select first
              this.loadOrganisations(true);
            } else {
              this.loadOrganisations(false);
            }
          } else {
            this.showToast(res.message || 'Failed to delete organisation', 'error');
          }
        },
        error: (err) => {
          this.showToast(err.error?.message || 'Error deleting organisation', 'error');
        }
      });
    }
  }

  // Items Methods
  loadItems(): void {
    if (!this.selectedOrgId) return;
    this.loadingItems = true;
    this.itemService.getAll(this.selectedOrgId).subscribe({
      next: (res) => {
        if (res.success && res.data) {
          this.items = res.data;
          this.cdr.markForCheck();
        } else {
          this.showToast(res.message || 'Failed to load items', 'error');
        }
        this.loadingItems = false;
      },
      error: (err) => {
        this.showToast(err.error?.message || 'Error loading items', 'error');
        this.loadingItems = false;
      }
    });
  }

  onCreateItem(): void {
    if (this.itemForm.invalid || !this.selectedOrgId) {
      this.itemForm.markAllAsTouched();
      return;
    }

    this.submitting = true;
    this.itemService.create(this.selectedOrgId, this.itemForm.value).subscribe({
      next: (res) => {
        if (res.success && res.data) {
          this.showToast(`Item "${res.data.name}" added!`);
          this.showAddItemModal = false;
          this.itemForm.reset({ name: '', rate: 0 });
          this.items = [...this.items, res.data];
          this.cdr.markForCheck();
          this.loadItems();
        } else {
          this.showToast(res.message || 'Failed to add item', 'error');
        }
        this.submitting = false;
      },
      error: (err) => {
        this.showToast(err.error?.message || 'Error adding item', 'error');
        this.submitting = false;
      }
    });
  }

  openEditItemModal(item: Item): void {
    this.editingItem = item;
    this.editItemForm.patchValue({
      name: item.name,
      rate: item.rate
    });
    this.showEditItemModal = true;
  }

  onUpdateItem(): void {
    if (this.editItemForm.invalid || !this.editingItem?.id || !this.selectedOrgId) {
      return;
    }

    this.submitting = true;
    this.itemService.update(this.selectedOrgId, this.editingItem.id, this.editItemForm.value).subscribe({
      next: (res) => {
        if (res.success) {
          this.showToast('Item updated successfully');
          this.showEditItemModal = false;
          if (res.data) {
            const updatedItem = res.data;
            this.items = this.items.map(item => item.id === updatedItem.id ? updatedItem : item);
            this.cdr.markForCheck();
            this.cdr.detectChanges();
          }
          this.loadItems();
        } else {
          this.showToast(res.message || 'Failed to update item', 'error');
        }
        this.submitting = false;
      },
      error: (err) => {
        this.showToast(err.error?.message || 'Error updating item', 'error');
        this.submitting = false;
      }
    });
  }

  onDeleteItem(item: Item): void {
    if (!item.id || !this.selectedOrgId) return;
    if (confirm(`Are you sure you want to delete "${item.name}"?`)) {
      this.itemService.delete(this.selectedOrgId, item.id).subscribe({
        next: (res) => {
          if (res.success) {
            this.showToast('Item deleted successfully');
            this.loadItems();
          } else {
            this.showToast(res.message || 'Failed to delete item', 'error');
          }
        },
        error: (err) => {
          this.showToast(err.error?.message || 'Error deleting item', 'error');
        }
      });
    }
  }

  // Getters for Dashboard Stats
  get totalItemsCount(): number {
    return this.items.length;
  }

  get totalInventoryValue(): number {
    return this.items.reduce((sum, item) => sum + (Number(item.rate) || 0), 0);
  }

  // Filters and Sorting
  get filteredItems(): Item[] {
    let result = [...this.items];
    
    // Search filter
    if (this.itemSearchQuery.trim()) {
      const q = this.itemSearchQuery.toLowerCase();
      result = result.filter(item => 
        item.name.toLowerCase().includes(q)
      );
    }

    // Sort
    result.sort((a, b) => {
      let valA: any = a[this.itemSortKey];
      let valB: any = b[this.itemSortKey];

      if (this.itemSortKey === 'rate') {
        valA = Number(valA) || 0;
        valB = Number(valB) || 0;
      } else {
        valA = (valA || '').toString().toLowerCase();
        valB = (valB || '').toString().toLowerCase();
      }

      if (valA < valB) return this.itemSortAsc ? -1 : 1;
      if (valA > valB) return this.itemSortAsc ? 1 : -1;
      return 0;
    });

    return result;
  }

  changeSort(key: 'name' | 'rate' | 'createdAt'): void {
    if (this.itemSortKey === key) {
      this.itemSortAsc = !this.itemSortAsc;
    } else {
      this.itemSortKey = key;
      this.itemSortAsc = true;
    }
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
