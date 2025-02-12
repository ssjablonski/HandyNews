import { Component, OnInit } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ClubService } from '../../services/club.service';
import { SeasonService } from '../../../season/services/season.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ClubForm } from '../../models/clubForm.model';
import { League } from '../../../leagues/models/league.model';
import { Season } from '../../../season/models/season.model';
import { LeagueService } from '../../../leagues/services/league.service';
import { ClubFormData } from '../../models/clubFormData.model';

@Component({
  selector: 'app-club-form',
  imports: [
    MatCardModule,
    MatFormFieldModule,
    MatDatepickerModule,
    MatSelectModule,
    MatInputModule,
    MatButtonModule,
    ReactiveFormsModule,
    RouterModule,
  ],
  templateUrl: './club-form.component.html',
  styleUrl: './club-form.component.scss',
})
export class ClubFormComponent implements OnInit {
  public isEditMode = false;
  private clubId?: number;
  public leagues: League[] = [];
  public seasons: Season[] = [];

  public constructor(
    private clubService: ClubService,
    private seasonService: SeasonService,
    private leagueService: LeagueService,
    private snackBar: MatSnackBar,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  protected clubForm: FormGroup<ClubForm> = new FormGroup<ClubForm>({
    leagueId: new FormControl<number | null>(null, {
      validators: [Validators.required],
    }),
    seasonId: new FormControl<number | null>(
      { value: null, disabled: true },
      { validators: [Validators.required] }
    ),
    name: new FormControl<string>('', {
      validators: [Validators.required],
    }),
    city: new FormControl<string>('', {
      validators: [Validators.required],
    }),
    logoUrl: new FormControl<string>('', {
      validators: [Validators.required],
    }),
  });

  public ngOnInit(): void {
    this.route.params.subscribe((params) => {
      if (params['id']) {
        this.isEditMode = true;
        this.clubId = +params['id'];
        this.adjustFormForEditMode();
        this.loadClubData(this.clubId);
      }
    });

    this.loadLeagues();

    const seasonControl = this.clubForm.get('seasonId');
    this.clubForm.get('leagueId')?.valueChanges.subscribe((leagueId) => {
      if (leagueId) {
        this.loadSeasons(leagueId);
        seasonControl?.reset();
        seasonControl?.enable();
      } else {
        seasonControl?.disable();
        seasonControl?.reset();
        this.seasons = [];
      }
    });
  }

  private adjustFormForEditMode(): void {
    this.clubForm.controls.leagueId.clearValidators();
    this.clubForm.controls.seasonId.clearValidators();

    this.clubForm.controls.leagueId.disable();
    this.clubForm.controls.seasonId.disable();

    this.clubForm.controls.leagueId.updateValueAndValidity();
    this.clubForm.controls.seasonId.updateValueAndValidity();
  }

  public loadLeagues(): void {
    this.leagueService.getAllLeagues().subscribe({
      next: (data) => {
        this.leagues = data;
      },
      error: (err) => console.error(err),
    });
  }

  public loadSeasons(leagueId: number): void {
    this.seasonService.getAllSeasonsFromLeague(leagueId).subscribe({
      next: (data) => {
        this.seasons = data;
      },
      error: (err) => console.error(err),
    });
  }

  private loadClubData(clubId: number): void {
    this.clubService.getClubById(clubId).subscribe({
      next: (club) =>
        this.clubForm.patchValue({
          name: club.name,
          city: club.city,
          logoUrl: club.logoUrl,
        }),
      error: (err) => this.toastError('load club data', err),
    });
  }

  public onSubmit(): void {
    if (this.clubForm.valid) {
      const formValue = this.clubForm.getRawValue();
      const clubFormData: ClubFormData = {
        name: formValue.name!,
        city: formValue.city!,
        logoUrl: formValue.logoUrl!,
      };

      if (this.isEditMode && this.clubId) {
        this.updateClub(this.clubId, clubFormData);
      } else {
        const seasonId = formValue.seasonId!;
        this.createClub(seasonId, clubFormData);
      }
    }
  }

  private createClub(seasonId: number, clubData: ClubFormData): void {
    this.clubService.addClub(seasonId, clubData).subscribe({
      next: () => {
        this.toastSuccess('Club created successfully.');
        this.router.navigate(['/user/matches']);
      },
      error: (error) => this.toastError('create club', error),
    });
  }

  private updateClub(clubId: number, clubData: ClubFormData): void {
    this.clubService.updateClub(clubId, clubData).subscribe({
      next: () => {
        this.toastSuccess('Club updated successfully.');
        this.router.navigate(['/user/matches']);
      },
      error: (error) => this.toastError('update club', error),
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
