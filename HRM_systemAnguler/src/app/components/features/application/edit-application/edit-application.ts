import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApplicationStatus } from '../../../../models/application.model';
import { ActivatedRoute } from '@angular/router';
import { ApplicationService } from '../../../../services/application.service';
import { ToastService } from '../../../../services/toast.service';
@Component({
  selector: 'app-edit-application',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './edit-application.html',
  styleUrl: './edit-application.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EditApplication implements OnInit {
  id!: number;
  status!: ApplicationStatus;
  statuses = Object.values(ApplicationStatus);
  constructor(
    private route: ActivatedRoute,
    private service: ApplicationService,
    private toast: ToastService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit() {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
  }
  update() {
    this.service.updateStatus(this.id, this.status).subscribe(() => {
      this.toast.success('Status Updated');
    });
  }
}
