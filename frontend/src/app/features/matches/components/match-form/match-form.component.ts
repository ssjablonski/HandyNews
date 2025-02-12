import { Component, OnInit } from '@angular/core';
import { MatchService } from '../../services/match.service';
import { League } from '../../../leagues/models/league.model';
import { LeagueService } from '../../../leagues/services/league.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router, RouterModule } from '@angular/router';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatchForm } from '../../models/matchForm.model';
import { MatchFormData } from '../../models/matchFormData.model';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { Season } from '../../../season/models/season.model';
import { SeasonService } from '../../../season/services/season.service';
import { Team } from '../../../clubs/models/team.model';

@Component({
  selector: 'app-match-form',
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
  templateUrl: './match-form.component.html',
  styleUrl: './match-form.component.scss',
})
export class MatchFormComponent implements OnInit {
  public leagues: League[] = [];
  public seasons: Season[] = [];
  public teams: Team[] = [];

  public constructor(
    private matchService: MatchService,
    private leagueService: LeagueService,
    private seasonService: SeasonService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {}

  protected matchForm: FormGroup<MatchForm> = new FormGroup<MatchForm>({
    date: new FormControl<string>('', {
      validators: [Validators.required],
    }),
    homeScore: new FormControl<number>(0),
    awayScore: new FormControl<number>(0),
    status: new FormControl<string>('', {
      validators: [Validators.required],
    }),
    homeId: new FormControl<number | null>(
      { value: null, disabled: true },
      { validators: [Validators.required] }
    ),
    awayId: new FormControl<number | null>(
      { value: null, disabled: true },
      { validators: [Validators.required] }
    ),
    leagueId: new FormControl<number | null>(null, {
      validators: [Validators.required],
    }),
    seasonId: new FormControl<number | null>(
      { value: null, disabled: true },
      { validators: [Validators.required] }
    ),
  });

  public ngOnInit(): void {
    this.loadLeagues();
    const seasonControl = this.matchForm.get('seasonId');
    const homeTeamControl = this.matchForm.get('homeId');
    const awayTeamControl = this.matchForm.get('awayId');

    this.matchForm.get('leagueId')?.valueChanges.subscribe((leagueId) => {
      if (leagueId) {
        this.loadSeasons(leagueId);
        homeTeamControl?.reset();
        awayTeamControl?.reset();
        seasonControl?.reset();

        seasonControl?.enable();
      } else {
        seasonControl?.disable();
        seasonControl?.reset();
        this.seasons = [];
      }
    });

    this.matchForm.get('seasonId')?.valueChanges.subscribe((seasonId) => {
      if (seasonId) {
        this.loadClubs(seasonId);
        homeTeamControl?.enable();
        awayTeamControl?.enable();
      } else {
        homeTeamControl?.disable();
        awayTeamControl?.disable();
        homeTeamControl?.reset();
        awayTeamControl?.reset();
        this.teams = [];
      }
    });
  }

  public loadLeagues(): void {
    this.leagueService.getAllLeagues().subscribe({
      next: (data) => {
        console.log(data, 'leagues');
        this.leagues = data;
      },
      error: (err) => console.error(err),
    });
  }

  public loadSeasons(leagueId: number): void {
    this.seasonService.getAllSeasonsFromLeague(leagueId).subscribe({
      next: (data) => {
        console.log(data, 'seasons');
        this.seasons = data;
      },
      error: (err) => console.error(err),
    });
  }

  public loadClubs(seasonId: number): void {
    this.seasonService.getAllClubsFromSeason(seasonId).subscribe({
      next: (data) => {
        console.log(data, 'teams');
        this.teams = data;
      },
      error: (err) => console.error(err),
    });
  }

  public onSubmit(): void {
    if (this.matchForm.valid) {
      console.log(this.matchForm.value);
      const matchFormData = this.matchForm.value;
      this.matchService.addMatch(matchFormData as MatchFormData).subscribe({
        next: () => {
          this.snackBar.open('Match created successfully.', 'Close', {
            duration: 5000,
            horizontalPosition: 'right',
            verticalPosition: 'top',
          });
          this.router.navigate(['/user/matches']);
        },
        error: (error) => {
          this.snackBar.open(
            `Failed to create match: ${error.message}`,
            'Close',
            {
              duration: 5000,
              horizontalPosition: 'right',
              verticalPosition: 'top',
            }
          );
        },
      });
    }
  }
}
