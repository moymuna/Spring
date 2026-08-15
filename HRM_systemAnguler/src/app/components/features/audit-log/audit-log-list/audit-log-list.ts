import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AuditLogService, AuditLogEntry } from '../../../../services/audit-log.service';
@Component({
  selector: 'app-audit-log-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './audit-log-list.html',
  styleUrl: './audit-log-list.css',
})
export class AuditLogList implements OnInit {
  entries: AuditLogEntry[] = [];
  page = 0;
  size = 20;
  totalPages = 0;
  constructor(private auditLogService: AuditLogService) {}
  ngOnInit(): void {
    this.load();
  }
  load(): void {
    this.auditLogService.getByPage(this.page, this.size).subscribe((data) => {
      this.entries = data.content ?? data;
      this.totalPages = data.totalPages ?? 1;
    });
  }
  changePage(pageNumber: number): void {
    this.page = pageNumber;
    this.load();
  }
  getPages() {
    return Array(this.totalPages)
      .fill(0)
      .map((_, i) => i);
  }
}
