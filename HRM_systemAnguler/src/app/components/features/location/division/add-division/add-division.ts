import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DivisionModel } from '../../../../../models/division';
import { DivisionService } from '../../../../../services/division.service';
import { ToastService } from '../../../../../services/toast.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-add-division',
  imports: [CommonModule, FormsModule],
  templateUrl: './add-division.html',
  styleUrl: './add-division.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AddDivision {
  division: DivisionModel = {
    name: '',
    nameBN: '',
    countryId: 0,
  };
  constructor(
    private service: DivisionService,
    private cdr: ChangeDetectorRef,
    private toast: ToastService,
    private router: Router,
  ) {}
  save() {
    this.service.save(this.division).subscribe({
      next: () => {
        this.toast.success('Division Saved Successfully');
        this.reset();
        this.cdr.markForCheck();
        setTimeout(() => {
          this.router.navigate(['/division']);
        }, 800);
      },
      error: (err) => {
        console.log(err);
        this.toast.error('Division Save Failed');
      },
    });
  }
  reset() {
    this.division = {
      name: '',
      nameBN: '',
      countryId: 0,
    };
  }
}
