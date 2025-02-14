import { Component, OnInit } from '@angular/core';
import { SeasonService } from '../../services/season.service';
import { LeagueService } from '../../../leagues/services/league.service';
import { League } from '../../../leagues/models/league.model';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { SeasonForm } from '../../models/seasonForm.model';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { SeasonFormData } from '../../models/seasonFormData.model';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-season-form',
  imports: [
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    ReactiveFormsModule,
  ],
  templateUrl: './season-form.component.html',
  styleUrl: './season-form.component.scss',
})
export class SeasonFormComponent implements OnInit {
  public isEditMode = false;
  private seasonId?: number;
  public leagues: League[] = [];

  public constructor(
    private seasonService: SeasonService,
    private leagueService: LeagueService,
    private snackBar: MatSnackBar,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  protected seasonForm: FormGroup<SeasonForm> = new FormGroup<SeasonForm>({
    leagueId: new FormControl<number | null>(null, {
      validators: [Validators.required],
    }),
    name: new FormControl<string>('', {
      validators: [Validators.required],
    }),
    year: new FormControl<number>(0, {
      validators: [Validators.required],
    }),
    // validator do sprawdzenia czy juz sezon o tym roku nie istnieje?
  });

  public ngOnInit(): void {
    this.loadLeagues();
    this.checkEditMode();
  }

  private loadLeagues(): void {
    this.leagueService.getAllLeagues().subscribe((leagues) => {
      this.leagues = leagues;
    });
  }

  private checkEditMode(): void {
    this.route.params.subscribe((params) => {
      if (params['id']) {
        this.isEditMode = true;
        this.seasonId = +params['id'];
        this.loadSeasonData(this.seasonId);
      }
    });
  }

  private loadSeasonData(id: number): void {
    this.seasonService.getSeasonById(id).subscribe({
      next: (season) => {
        this.seasonForm.patchValue({
          leagueId: season.leagueDto.id,
          name: season.name,
          year: season.year,
        });
        this.seasonForm.get('leagueId')?.disable();
      },
      error: (err) => this.toastError('load', err),
    });
  }

  public onSubmit(): void {
    if (this.seasonForm.valid) {
      const formValue = this.seasonForm.getRawValue();
      const leagueId = formValue.leagueId!;
      const seasonData: SeasonFormData = {
        name: formValue.name!,
        year: formValue.year!,
      };

      if (this.isEditMode && this.seasonId) {
        this.updateSeason(this.seasonId, seasonData);
      } else {
        this.createSeason(leagueId, seasonData);
      }
    }
  }

  private createSeason(leagueId: number, seasonData: SeasonFormData): void {
    this.seasonService.addSeason(leagueId, seasonData).subscribe({
      next: () => {
        this.toastSuccess('Season created successfully.');
        this.router.navigate(['/user/seasons']);
      },
      error: (error) => this.toastError('create', error),
    });
  }

  private updateSeason(seasonId: number, seasonData: SeasonFormData): void {
    this.seasonService.updateSeason(seasonId, seasonData).subscribe({
      next: () => {
        this.toastSuccess('Match updated successfully.');
        this.router.navigate(['/user/seasons']);
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
