import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-delete-account-dialog',
  imports: [MatDialogModule, MatButtonModule],
  templateUrl: './delete-account-dialog.component.html',
  styleUrl: './delete-account-dialog.component.scss',
})
export class DeleteAccountDialogComponent {
  public constructor(@Inject(MAT_DIALOG_DATA) public data: { id: string }) {}
}
