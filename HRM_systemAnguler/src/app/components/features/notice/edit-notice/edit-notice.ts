import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NoticeService } from '../../../../services/notice.service';
import { ToastService } from '../../../../services/toast.service';
@Component({
  selector: 'app-edit-notice',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './edit-notice.html',
  styleUrl: './edit-notice.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EditNotice implements OnInit {
  noticeForm!: FormGroup;
  id!: number;
  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private noticeService: NoticeService,
    private router: Router,
    private toast: ToastService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit() {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.noticeForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      publishDate: ['', Validators.required],
      officeId: ['', Validators.required],
    });
    this.loadNotice();
  }
  loadNotice() {
    this.noticeService.getNoticeById(this.id).subscribe((data) => {
      this.noticeForm.patchValue(data);
      this.cdr.markForCheck();
    });
  }
  updateNotice() {
    this.noticeService.updateNotice(this.id, this.noticeForm.value).subscribe(() => {
      this.toast.success('Notice updated successfully');
      this.router.navigate(['/notice']);
    });
  }
}
