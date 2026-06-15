import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth/auth';
import { ItemService } from '../../services/items/items';
import { OrganisationService } from '../../services/organisation/organisation';
import { Item } from '../../core/models/item.model';
import { Organisation } from '../../core/models/organisation.model';
import { AuthResponse } from '../../core/models/auth.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.scss']
})
export class DashboardComponent implements OnInit {

  user : AuthResponse | null = null;
  organisation: Organisation | null = null;
  items: Item[] = [];
  loading = true;
  


  get totalItems(): number {
    return this.items.length;
  }

  get activeItems(): number {
    return this.items.filter(i => i.isActive).length;
  }

  get highestRate(): number {
    if (!this.items.length) return 0;
    return Math.max(...this.items.map(i => i.rate));
  }

  get lowestRate(): number {
    if (!this.items.length) return 0;
    return Math.min(...this.items.map(i => i.rate));
  }

  get recentItems(): Item[] {
    return [...this.items]
      .sort((a, b) => new Date(b.updatedAt!).getTime() - new Date(a.updatedAt!).getTime())
      .slice(0, 5);
  }

  constructor(
    public authService: AuthService,
    private itemService: ItemService,
    private orgService: OrganisationService,
    private cdr: ChangeDetectorRef
  ) {
    this.user = this.authService.getCurrentUser();
  }

  ngOnInit(): void {
    this.loadDashboard();
  }

  loadDashboard(): void {
    
      this.itemService.getAll().subscribe({
      next: res => {
        this.items = res.data || [];
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: err => {
        console.error('Failed to load items', err);
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }
}