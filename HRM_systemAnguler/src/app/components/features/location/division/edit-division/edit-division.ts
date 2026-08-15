import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DivisionModel } from '../../../../../models/division';
import { DivisionService } from '../../../../../services/division.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastService } from '../../../../../services/toast.service';
@Component({
  selector: 'app-edit-division',
  imports: [CommonModule, FormsModule],
  templateUrl: './edit-division.html',
  styleUrl: './edit-division.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EditDivision implements OnInit {
  division: DivisionModel = {
    name: '',
    nameBN: '',
    countryId: 0,
  };
  id!: number;
  constructor(
    private service: DivisionService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private toast: ToastService,
  ) {}
  ngOnInit() {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadDivision();
  }
  loadDivision() {
    this.service.getById(this.id).subscribe((data) => {
      this.division = data;
      this.cdr.markForCheck();
    });
  }
  update() {
    this.service.update(this.id, this.division).subscribe(() => {
      this.toast.success('Division Updated Successfully');
      this.router.navigate(['/division']);
    });
  }
  reset() {
    this.loadDivision();
  }
}
