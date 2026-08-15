import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { LeaveType } from '../../../../models/leavetype.model';
import { LeaveTypeService } from '../../../../services/leave-type.service';
import { ModalOutlet } from '../../../shared/modal-outlet/modal-outlet';
@Component({
  selector: 'app-leavetype-list',
  standalone: true,
  imports: [CommonModule, RouterModule, ModalOutlet],
  templateUrl: './leavetype-list.html',
  styleUrl: './leavetype-list.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LeavetypeList implements OnInit {
  leaveTypes: LeaveType[] = [];
  successMessage = '';
  errorMessage = '';
  constructor(
    private leaveTypeService: LeaveTypeService,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    this.loadLeaveTypes();
  }
  loadLeaveTypes() {
    this.leaveTypeService.getAllLeaveTypes().subscribe({
      next: (res) => {
        this.leaveTypes = res;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.log(err);
        this.errorMessage = 'Unable to load Leave Types.';
        this.cdr.markForCheck();
      },
    });
  }
  editLeaveType(id: number) {
    this.router.navigate(['/leavetype/edit', id]);
  }
  deleteLeaveType(id: number) {
    if (!confirm('Are you sure you want to delete this Leave Type?')) {
      return;
    }
    this.leaveTypeService.deleteLeaveType(id).subscribe({
      next: () => {
        this.successMessage = 'Leave Type Deleted Successfully.';
        this.errorMessage = '';
        this.cdr.markForCheck();
        this.loadLeaveTypes();
      },
      error: (err) => {
        console.log(err);
        this.successMessage = '';
        this.errorMessage = 'Delete Failed.';
        this.cdr.markForCheck();
      },
    });
  }
}
