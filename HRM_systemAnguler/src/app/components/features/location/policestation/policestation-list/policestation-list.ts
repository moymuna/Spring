import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { PoliceStationModel } from '../../../../../models/policestation.model';
import { PolicestationService } from '../../../../../services/policestation.service';
import { ToastService } from '../../../../../services/toast.service';
import { ModalOutlet } from '../../../../shared/modal-outlet/modal-outlet';
@Component({
  selector: 'app-policestation-list',
  imports: [CommonModule, RouterModule, ModalOutlet],
  templateUrl: './policestation-list.html',
  styleUrl: './policestation-list.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PolicestationList {
  stations: PoliceStationModel[] = [];
  constructor(
    private service: PolicestationService,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private toast: ToastService,
  ) {}
  ngOnInit() {
    this.loadPoliceStations();
  }
  loadPoliceStations() {
    this.service.getAll().subscribe((data) => {
      this.stations = data;
      this.cdr.markForCheck();
      console.log(data);
    });
  }
  edit(id: number) {
    this.router.navigate(['/policestation/edit', id]);
  }
  delete(id: number) {
    if (confirm('Delete this Police Station?')) {
      this.service.delete(id).subscribe(() => {
        this.toast.success('Police Station Deleted Successfully');
        this.loadPoliceStations();
      });
    }
  }
}
