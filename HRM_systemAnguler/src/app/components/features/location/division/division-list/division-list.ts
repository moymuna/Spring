import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { DivisionModel } from '../../../../../models/division';
import { DivisionService } from '../../../../../services/division.service';
import { ToastService } from '../../../../../services/toast.service';
import { ModalOutlet } from '../../../../shared/modal-outlet/modal-outlet';
@Component({
  selector: 'app-division-list',
  imports: [CommonModule, RouterModule, ModalOutlet],
  templateUrl: './division-list.html',
  styleUrl: './division-list.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DivisionList implements OnInit {
  divisions: DivisionModel[] = [];
  constructor(
    private service: DivisionService,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private toast: ToastService,
  ) {}
  ngOnInit() {
    this.loadDivisions();
  }
  loadDivisions() {
    this.service.getAll().subscribe((data) => {
      this.divisions = data;
      this.cdr.markForCheck();
      console.log(data);
    });
  }
  edit(id: number) {
    this.router.navigate(['/division/edit', id]);
  }
  delete(id: number) {
    if (confirm('Delete this Division?')) {
      this.service.delete(id).subscribe(() => {
        this.toast.success('Division Deleted Successfully');
        this.loadDivisions();
      });
    }
  }
}
