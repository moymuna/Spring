import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Notice } from '../../../../models/notice.model';
import { NoticeService } from '../../../../services/notice.service';
import { StorageService } from '../../../../services/storage.service';
import { ModalOutlet } from '../../../shared/modal-outlet/modal-outlet';
@Component({
  selector: 'app-list-notice',
  imports: [CommonModule, RouterModule, ModalOutlet],
  templateUrl: './list-notice.html',
  styleUrl: './list-notice.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ListNotice implements OnInit {
  notices: Notice[] = [];
  successMessage = '';
  isEmployeeOnly = false;
  constructor(
    private noticeService: NoticeService,
    private storage: StorageService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit() {
    this.isEmployeeOnly = this.storage.getRole() === 'EMPLOYEE';
    this.loadNotices();
  }
  loadNotices() {
    this.noticeService.getAllNotices().subscribe({
      next: (data) => {
        this.notices = data;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.log(err);
        this.cdr.markForCheck();
      },
    });
  }
  deleteNotice(id: number) {
    if (confirm('Are you sure you want to delete this notice?')) {
      this.noticeService.deleteNotice(id).subscribe(() => {
        this.successMessage = 'Notice deleted successfully';
        this.cdr.markForCheck();
        this.loadNotices();
      });
    }
  }
}
