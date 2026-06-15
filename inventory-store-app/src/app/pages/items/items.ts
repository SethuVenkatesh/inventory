import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Item } from '../../core/models/item.model';
import { ItemService } from '../../services/items/items';
import { AuthService } from '../../services/auth/auth';

import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';

@Component({
  selector: 'app-items',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,   
    MatInputModule,      
    MatSelectModule,      
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './items.html',
  styleUrls: ['./items.scss']
})
export class ItemsComponent implements OnInit {

  items: Item[] = [];
  selectedItem: Item | null = null;
  showForm = false;
  showHistory = false;
  isEditing = false;
  loading = true;
  error = '';
  success = '';

  itemForm: FormGroup;

  constructor(private itemService: ItemService,
              private authService: AuthService,
              private fb: FormBuilder,
              private cdr: ChangeDetectorRef) {

    this.itemForm = this.fb.group({
      name: ['', Validators.required],
      rate: ['', [Validators.required, Validators.min(0)]],
      currency: ['INR'],
      createdBy: [''],
      updatedBy: ['']
    });
  }

  ngOnInit(): void {
    this.loadItems();
  }

  loadItems(): void {

    this.itemService.getAll().subscribe({
      next: res => {
        this.items = res.data || [];
        this.loading = false;
        console.log('Items loaded:', this.items);
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'Failed to load items'
        this.loading = false;
        this.cdr.detectChanges();
      }

    });
  }



  openCreate(): void {
    this.isEditing = false;
    this.showForm = true;
    this.itemForm.reset({ currency: 'INR' });
  }

  openEdit(item: Item): void {
    this.isEditing = true;
    this.selectedItem = item;
    this.showForm = true;
    this.itemForm.patchValue({
      name: item.name,
      rate: item.rate,
      currency: item.currency
    });
  }



  submitItem(): void {
    this.loading = true;
    if (this.itemForm.invalid) {
      this.cdr.detectChanges();
      this.loading = false;
      return;
    };

    const payload: Item = {
      ...this.itemForm.value
    };

    const request = this.isEditing
      ? this.itemService.update(this.selectedItem!.id!, { ...payload })
      : this.itemService.create(payload);

    request.subscribe({
      next: () => {
        this.success = this.isEditing ? 'Item updated!' : 'Item created!';
        this.showForm = false;
        this.loadItems();
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: err => {
        this.error = err.error?.message || 'Operation failed';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  deleteItem(item: Item): void {
    if (!confirm(`Delete "${item.name}"?`)) return;

    this.itemService.delete(item.id!).subscribe({
      next: () => {
        this.success = 'Item deleted!';
        this.loadItems();
        this.cdr.detectChanges();
      }
    });
  }

  closeAll(): void {
    this.showForm = false;
    this.showHistory = false;
    this.selectedItem = null;
    this.error = '';
    this.success = '';
  }
}