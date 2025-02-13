import { Component, OnInit } from '@angular/core';
import { LeagueService } from '../../services/league.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { LeagueForm } from '../../models/leagueForm.model';
import { LeagueFormData } from '../../models/leagueFormData.model';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-league-form',
  imports: [
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    ReactiveFormsModule,
  ],
  templateUrl: './league-form.component.html',
  styleUrl: './league-form.component.scss',
})
export class LeagueFormComponent implements OnInit {
  public isEditMode = false;
  private leagueId?: number;

  public constructor(
    private leagueService: LeagueService,
    private snackBar: MatSnackBar,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  protected leagueForm: FormGroup<LeagueForm> = new FormGroup<LeagueForm>({
    name: new FormControl<string | null>('', {
      validators: [Validators.required],
    }),
    country: new FormControl<string | null>('', {
      validators: [Validators.required],
    }),
    logoUrl: new FormControl<string | null>('', {
      validators: [Validators.required],
    }),
  });

  public ngOnInit(): void {
    this.route.params.subscribe((params) => {
      if (params['id']) {
        this.isEditMode = true;
        this.leagueId = +params['id'];
        this.loadLeagueData(this.leagueId);
      }
    });
  }

  public loadLeagueData(id: number): void {
    this.leagueService.getLeagueById(id).subscribe({
      next: (league) => {
        this.leagueForm.patchValue({
          name: league.name,
          country: league.country,
          logoUrl: league.logoUrl,
        });
      },
      error: (err) => this.toastError('load', err),
    });
  }

  public onSubmit(): void {
    if (this.leagueForm.valid) {
      const formValue = this.leagueForm.getRawValue();
      const leagueData: LeagueFormData = {
        name: formValue.name!,
        country: formValue.country!,
        logoUrl: formValue.logoUrl!,
      };

      if (this.isEditMode && this.leagueId) {
        this.updateLeague(this.leagueId, leagueData);
      } else {
        this.createLeague(leagueData);
      }
    }
  }

  public createLeague(leagueData: LeagueFormData): void {
    this.leagueService.addLeague(leagueData).subscribe({
      next: () => {
        this.toastSuccess('League created successfully.');
        this.router.navigate(['/user/matches']);
      },
      error: (error) => this.toastError('update', error),
    });
  }

  public updateLeague(id: number, leagueData: LeagueFormData): void {
    this.leagueService.updateLeague(id, leagueData).subscribe({
      next: () => {
        this.toastSuccess('League updated successfully.');
        this.router.navigate(['/user/matches']);
      },
      error: (error) => this.toastError('update', error),
    });
  }

  public toastSuccess(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 5000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
    });
  }

  public toastError(action: string, error: Error): void {
    this.snackBar.open(`Failed to ${action}: ${error.message}`, 'Close', {
      duration: 5000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
    });
  }
}
