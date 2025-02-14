import { LeagueService } from './../../../leagues/services/league.service';
import { Component, OnInit } from '@angular/core';
import { Match } from '../../models/match.model';
import { ActivatedRoute, Router } from '@angular/router';
import { MatchService } from '../../services/match.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule, DatePipe } from '@angular/common';
import { Season } from '../../../season/models/season.model';
import { League } from '../../../leagues/models/league.model';
import { SeasonService } from '../../../season/services/season.service';
import { forkJoin, switchMap, tap } from 'rxjs';
import { ConfirmDialogComponent } from '../../../../shared/components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-match-details',
  imports: [CommonModule, MatCardModule, MatButtonModule],
  templateUrl: './match-details.component.html',
  styleUrl: './match-details.component.scss',
  providers: [DatePipe],
})
export class MatchDetailsComponent implements OnInit {
  public match!: Match;
  public season?: Season;
  public league?: League;

  public constructor(
    private route: ActivatedRoute,
    private router: Router,
    private matchService: MatchService,
    private seasonService: SeasonService,
    private leagueService: LeagueService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog
  ) {}

  public ngOnInit(): void {
    this.route.params
      .pipe(
        switchMap((params) => this.matchService.getMatchById(+params['id'])),
        tap((match) => (this.match = match)),
        switchMap((match) =>
          forkJoin([
            this.leagueService.getLeagueById(match.leagueId),
            this.seasonService.getSeasonById(match.seasonId),
          ])
        )
      )
      .subscribe({
        next: ([league, season]) => {
          this.league = league;
          this.season = season;
        },
        error: (error) => {
          this.snackBar.open(`Error loading match: ${error.message}`, 'Close', {
            duration: 5000,
          });
          this.router.navigate(['user/matches']);
        },
      });
  }

  public editMatch(): void {
    if (this.match.id) {
      this.router.navigate([`user/matches/form/${this.match.id}`]);
    }
  }

  public deleteMatch(id: number): void {
    this.matchService.deleteMatch(id).subscribe({
      next: () => {
        this.router.navigate(['user/matches']);
      },
      error: (error) => {
        this.snackBar.open(`Error deleting match: ${error.message}`, 'Close', {
          duration: 5000,
        });
      },
    });
  }

  public openDeleteDialog(): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '600px',
      data: {
        title: 'Delete Match',
        message: 'Are you sure you want to delete this match?',
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result === 'confirm' && this.match.id) {
        this.deleteMatch(this.match.id);
        this.snackBar.open('Match deleted successfully.', 'Close', {
          duration: 2000,
          horizontalPosition: 'right',
          verticalPosition: 'top',
        });
      }
    });
  }
}
