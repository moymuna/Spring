import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { InterviewModel } from '../../../../models/interview.model';
import { InterviewService } from '../../../../services/interview.service';
import { ModalOutlet } from '../../../shared/modal-outlet/modal-outlet';
@Component({
  selector: 'app-list-interview',
  standalone: true,
  imports: [CommonModule, RouterLink, ModalOutlet],
  templateUrl: './list-interview.html',
  styleUrl: './list-interview.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ListInterview {
  interviews: InterviewModel[] = [];
  constructor(
    private service: InterviewService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit() {
    this.load();
  }
  load() {
    this.service.getAll().subscribe((data) => {
      this.interviews = data;
      this.cdr.markForCheck();
    });
  }
  delete(id: number) {
    if (confirm('Delete this interview record?')) {
      this.service.delete(id).subscribe(() => this.load());
    }
  }
}
