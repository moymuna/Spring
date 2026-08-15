import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DistrictModel } from '../../../../../models/district';
import { DistrictService } from '../../../../../services/district.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastService } from '../../../../../services/toast.service';
@Component({
  selector: 'app-edit-district',
  imports: [CommonModule, FormsModule],
  templateUrl: './edit-district.html',
  styleUrl: './edit-district.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EditDistrict implements OnInit {
  district: DistrictModel = {
    districtsName: '',
    nameBN: '',
    districtCode: '',
    divisionId: 0,
  };
  id!: number;
  constructor(
    private service: DistrictService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private toast: ToastService,
  ) {}
  ngOnInit() {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadDistrict();
  }
  loadDistrict() {
    this.service.getById(this.id).subscribe((data) => {
      this.district = data;
      this.cdr.markForCheck();
    });
  }
  update() {
    this.service.update(this.id, this.district).subscribe(() => {
      this.toast.success('District Updated Successfully');
      this.router.navigate(['/district']);
    });
  }
  reset() {
    this.loadDistrict();
  }
}
