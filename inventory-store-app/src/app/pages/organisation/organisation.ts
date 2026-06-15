import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { OrganisationService } from '../../services/organisation/organisation';
import { AuthService } from '../../services/auth/auth';
import { Organisation } from '../../core/models/organisation.model';

@Component({
  selector: 'app-organisation',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './organisation.html',
  styleUrls: ['./organisation.scss']
})
export class OrganisationComponent implements OnInit {

  organisation: Organisation | null = null;
  isEditing = false;
  loading = false;
  success = '';
  error = '';

  form: FormGroup;

  constructor(
    private orgService: OrganisationService,
    private authService: AuthService,
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef
  ) {
    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      updatedBy: ['']
    });
  }

  ngOnInit(): void {
    this.loadOrganisation();
  }

  loadOrganisation(): void {
    const user = this.authService.getCurrentUser();
    this.loading = true;
    if (!user) {
      this.error = 'User not authenticated';
      this.loading = false;
      this.cdr.detectChanges();
      return;
    }

    this.orgService.getMyOrganisation().subscribe({
      next: res => {
        this.organisation = res.data;
        this.form.patchValue({ name: this.organisation?.name });
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: err => {
        this.error = err.error?.message || 'Failed to load organisation';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  toggleEdit(): void {
    this.isEditing = !this.isEditing;
    this.success = '';
    this.error = '';
    if (!this.isEditing) {
      this.form.patchValue({ name: this.organisation?.name });
    }
  }

  submit(): void {
    if (this.form.invalid) return;
    this.loading = true;
    this.success = '';
    this.error = '';

    const user = this.authService.getCurrentUser()!;
    const payload = {
      ...this.form.value
    };

    this.orgService.update(user.organisationId, payload).subscribe({
      next: res => {
        this.organisation = res.data;
        this.isEditing = false;
        this.success = 'Organisation updated successfully';
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: err => {
        this.error = err.error?.message || 'Update failed';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }
}