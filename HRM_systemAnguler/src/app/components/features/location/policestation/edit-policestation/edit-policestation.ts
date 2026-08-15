import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PoliceStationModel } from '../../../../../models/policestation.model';
import { PolicestationService } from '../../../../../services/policestation.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastService } from '../../../../../services/toast.service';
@Component({
  selector: 'app-edit-policestation',
  imports: [CommonModule, FormsModule],
  templateUrl: './edit-policestation.html',
  styleUrl: './edit-policestation.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EditPolicestation implements OnInit {
  station: PoliceStationModel = {
    name: '',
    nameBn: '',
    postalCode: '',
    districtId: 0,
  };
  id!: number;
  constructor(
    private service: PolicestationService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private toast: ToastService,
  ) {}
  ngOnInit() {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadPoliceStation();
  }
  loadPoliceStation() {
    this.service.getById(this.id).subscribe((data) => {
      this.station = data;
      this.cdr.markForCheck();
    });
  }
  update() {
    this.service.update(this.id, this.station).subscribe(() => {
      this.toast.success('Police Station Updated Successfully');
      this.router.navigate(['/policestation']);
    });
  }
  reset() {
    this.loadPoliceStation();
  }
}
