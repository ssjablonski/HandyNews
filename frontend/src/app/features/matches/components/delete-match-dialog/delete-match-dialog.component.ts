import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-delete-match-dialog',
  imports: [MatButtonModule, MatDialogModule],
  templateUrl: './delete-match-dialog.component.html',
  styleUrl: './delete-match-dialog.component.scss',
})
export class DeleteMatchDialogComponent {
  public constructor(@Inject(MAT_DIALOG_DATA) public data: { id: string }) {}
}
