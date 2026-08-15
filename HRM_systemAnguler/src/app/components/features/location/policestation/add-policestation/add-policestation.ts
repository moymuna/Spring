import { ChangeDetectionStrategy, ChangeDetectorRef, Component } from '@angular/core';
import { PoliceStationModel } from '../../../../../models/policestation.model';
import { PolicestationService } from '../../../../../services/policestation.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ToastService } from '../../../../../services/toast.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-add-policestation',
  imports: [FormsModule, CommonModule],
  templateUrl: './add-policestation.html',
  styleUrl: './add-policestation.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AddPolicestation {
  station: PoliceStationModel = {
    name: '',
    nameBn: '',
    postalCode: '',
    districtId: 0,
  };
  constructor(
    private service: PolicestationService,
    private cdr: ChangeDetectorRef,
    private toast: ToastService,
    private router: Router,
  ) {}
  save() {
    this.service.save(this.station).subscribe({
      next: () => {
        this.toast.success('Police Station Saved Successfully');
        this.reset();
        this.cdr.markForCheck();
        setTimeout(() => {
          this.router.navigate(['/policestation']);
        }, 800);
      },
      error: (err) => {
        console.log(err);
        this.toast.error('Save Failed');
      },
    });
  }
  reset() {
    this.station = {
      name: '',
      nameBn: '',
      postalCode: '',
      districtId: 0,
    };
  }
}
