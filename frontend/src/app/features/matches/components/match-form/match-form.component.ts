import { Component, OnInit } from '@angular/core';
import { MatchService } from '../../services/match.service';
import { League } from '../../../leagues/models/league.model';
import { LeagueService } from '../../../leagues/services/league.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
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
import { Match } from '../../models/match.model';

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
  public isEditMode = false;
  private matchId?: number;
  public leagues: League[] = [];
  public seasons: Season[] = [];
  public teams: Team[] = [];

  public constructor(
    private matchService: MatchService,
    private leagueService: LeagueService,
    private seasonService: SeasonService,
    private snackBar: MatSnackBar,
    private router: Router,
    private route: ActivatedRoute
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
    seasonId: new FormControl<number | null>(null, {
      validators: [Validators.required],
    }),
  });

  public ngOnInit(): void {
    this.checkEditMode();
    this.loadLeagues();
    this.setupFormListeners();
  }

  private checkEditMode(): void {
    this.route.params.subscribe((params) => {
      if (params['id']) {
        this.isEditMode = true;
        this.matchId = +params['id'];
        this.loadMatchData(this.matchId);
      }
    });
  }

  private setupFormListeners(): void {
    this.matchForm.get('leagueId')?.valueChanges.subscribe((leagueId) => {
      if (leagueId) {
        this.loadSeasons(leagueId);
        this.matchForm.get('seasonId')?.reset();
        this.matchForm.get('seasonId')?.enable();
      } else {
        this.matchForm.get('seasonId')?.disable();
        this.seasons = [];
      }
    });

    this.matchForm.get('seasonId')?.valueChanges.subscribe((seasonId) => {
      if (seasonId) {
        this.loadClubs(seasonId);
        this.matchForm.get('homeId')?.enable();
        this.matchForm.get('awayId')?.enable();
      } else {
        this.matchForm.get('homeId')?.disable();
        this.matchForm.get('awayId')?.disable();
        this.teams = [];
      }
    });
  }

  private loadLeagues(): void {
    this.leagueService.getAllLeagues().subscribe({
      next: (data) => (this.leagues = data),
      error: (err) => this.toastError('load leagues', err),
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

  public loadClubs(seasonId: number): void {
    this.seasonService.getAllClubsFromSeason(seasonId).subscribe({
      next: (data) => {
        this.teams = data;
      },
      error: (err) => console.error(err),
    });
  }

  private loadMatchData(matchId: number): void {
    this.matchService.getMatchById(matchId).subscribe({
      next: (match) => this.patchFormValues(match),
      error: (err) => this.toastError('load', err),
    });
  }

  private patchFormValues(match: Match): void {
    this.matchForm.patchValue({
      date: match.date.toString(),
      leagueId: match.leagueId,
      seasonId: match.seasonId,
      homeId: match.homeTeam.id,
      awayId: match.awayTeam.id,
      homeScore: match.homeScore,
      awayScore: match.awayScore,
      status: match.status,
    });

    this.handleLeagueId(match.leagueId);
    this.handleSeasonId(match.seasonId);
  }

  private handleLeagueId(leagueId: number | null): void {
    if (leagueId) {
      this.loadSeasons(leagueId);
      this.matchForm.get('leagueId')?.disable();
    }
  }

  private handleSeasonId(seasonId: number | null): void {
    if (seasonId) {
      this.loadClubs(seasonId);
      this.matchForm.get('seasonId')?.disable();
      this.matchForm.get('homeId')?.enable();
      this.matchForm.get('awayId')?.enable();
    }
  }

  public onSubmit(): void {
    if (this.matchForm.valid) {
      const formValue = this.matchForm.getRawValue();
      const matchData: MatchFormData = {
        date: formValue.date!,
        homeScore: formValue.homeScore!,
        awayScore: formValue.awayScore!,
        status: formValue.status!,
        homeId: formValue.homeId!,
        awayId: formValue.awayId!,
        leagueId: formValue.leagueId!,
        seasonId: formValue.seasonId!,
      };

      if (this.isEditMode && this.matchId) {
        this.updateMatch(this.matchId, matchData);
      } else {
        this.createMatch(matchData);
      }
    }
  }

  private createMatch(matchData: MatchFormData): void {
    this.matchService.addMatch(matchData).subscribe({
      next: () => {
        this.toastSuccess('Match created successfully.');
        this.router.navigate(['/user/matches']);
      },
      error: (error) => this.toastError('create', error),
    });
  }

  private updateMatch(matchId: number, matchData: MatchFormData): void {
    this.matchService.updateMatch(matchId, matchData).subscribe({
      next: () => {
        this.toastSuccess('Match updated successfully.');
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
