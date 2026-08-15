import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { DistrictModel } from '../../../../../models/district';
import { DistrictService } from '../../../../../services/district.service';
import { ToastService } from '../../../../../services/toast.service';
import { ModalOutlet } from '../../../../shared/modal-outlet/modal-outlet';
@Component({
  selector: 'app-district-list',
  imports: [CommonModule, RouterModule, ModalOutlet],
  templateUrl: './district-list.html',
  styleUrl: './district-list.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DistrictList implements OnInit {
  districts: DistrictModel[] = [];
  constructor(
    private service: DistrictService,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private toast: ToastService,
  ) {}
  ngOnInit() {
    this.loadDistricts();
  }
  loadDistricts() {
    this.service.getAll().subscribe((data) => {
      this.districts = data;
      this.cdr.markForCheck();
      console.log(data);
    });
  }
  edit(id: number) {
    this.router.navigate(['/district/edit', id]);
  }
  delete(id: number) {
    if (confirm('Delete this District?')) {
      this.service.delete(id).subscribe(() => {
        this.toast.success('District Deleted Successfully');
        this.loadDistricts();
      });
    }
  }
}
