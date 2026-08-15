import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DistrictModel } from '../../../../../models/district';
import { DistrictService } from '../../../../../services/district.service';
import { Router } from '@angular/router';
import { ToastService } from '../../../../../services/toast.service';
@Component({
  selector: 'app-add-district',
  imports: [CommonModule, FormsModule],
  templateUrl: './add-district.html',
  styleUrl: './add-district.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AddDistrict {
  district: DistrictModel = {
    districtsName: '',
    nameBN: '',
    districtCode: '',
    divisionId: 0,
  };
  constructor(
    private service: DistrictService,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private toast: ToastService,
  ) {}
  ngOnInit() {}
  save() {
    this.service.save(this.district).subscribe(() => {
      this.toast.success('District Saved Successfully');
      this.reset();
      this.router.navigate(['/district']);
    });
  }
  reset() {
    this.district = {
      districtsName: '',
      nameBN: '',
      districtCode: '',
      divisionId: 0,
    };
    this.cdr.markForCheck();
  }
}
