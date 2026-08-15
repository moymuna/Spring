import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { PayrollService, SalarySheetRow } from '../../../../services/payroll.service';
import { ToastService } from '../../../../services/toast.service';
const MONTHS = [
  'January',
  'February',
  'March',
  'April',
  'May',
  'June',
  'July',
  'August',
  'September',
  'October',
  'November',
  'December',
];
@Component({
  selector: 'app-salary-sheet',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './salary-sheet.html',
  styleUrl: './salary-sheet.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SalarySheet implements OnInit {
  rows: SalarySheetRow[] = [];
  loading = false;
  errorMessage = '';
  month = new Date().getMonth() + 1;
  year = new Date().getFullYear();
  months = MONTHS.map((name, i) => ({ value: i + 1, name }));
  years: number[] = [];
  constructor(
    private payrollService: PayrollService,
    private toast: ToastService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    const current = new Date().getFullYear();
    this.years = [current + 1, current, current - 1, current - 2];
    this.load();
  }
  load(): void {
    this.loading = true;
    this.errorMessage = '';
    this.payrollService.getSalarySheet(this.year, this.month).subscribe({
      next: (rows) => {
        this.rows = rows ?? [];
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.errorMessage = 'Could not load the salary sheet.';
        this.loading = false;
        this.cdr.markForCheck();
      },
    });
  }
  get periodLabel(): string {
    return `${MONTHS[this.month - 1]} ${this.year}`;
  }
  get notGeneratedCount(): number {
    return this.rows.filter((r) => r.status === 'NOT_GENERATED').length;
  }
  total(field: keyof SalarySheetRow): number {
    return this.rows.reduce((sum, r) => sum + (Number(r[field]) || 0), 0);
  }
  private get columns(): { key: keyof SalarySheetRow; label: string }[] {
    return [
      { key: 'employeeCode', label: 'Employee Code' },
      { key: 'employeeName', label: 'Name' },
      { key: 'department', label: 'Department' },
      { key: 'designation', label: 'Designation' },
      { key: 'gradeNumber', label: 'Grade' },
      { key: 'basicSalary', label: 'Basic' },
      { key: 'hra', label: 'HRA' },
      { key: 'conveyanceAllowance', label: 'Conveyance' },
      { key: 'medicalAllowance', label: 'Medical' },
      { key: 'specialAllowance', label: 'Special' },
      { key: 'grossSalary', label: 'Gross' },
      { key: 'providentFund', label: 'PF' },
      { key: 'professionalTax', label: 'Prof. Tax' },
      { key: 'incomeTax', label: 'Income Tax' },
      { key: 'lopDays', label: 'LOP Days' },
      { key: 'unpaidLeaveDays', label: 'Unpaid Leave Days' },
      { key: 'leaveDeduction', label: 'Leave Deduction' },
      { key: 'advanceDeduction', label: 'Advance Recovered' },
      { key: 'totalDeductions', label: 'Total Deductions' },
      { key: 'netSalary', label: 'Net Payable' },
      { key: 'status', label: 'Status' },
    ];
  }
  /** CSV is built here rather than server-side so the download needs no extra endpoint. */
  downloadCsv(): void {
    if (!this.rows.length) {
      this.toast.error('Nothing to export.');
      return;
    }
    const escape = (v: any) => {
      const s = v === null || v === undefined ? '' : String(v);
      return /[",\n]/.test(s) ? `"${s.replace(/"/g, '""')}"` : s;
    };
    const header = this.columns.map((c) => c.label).join(',');
    const body = this.rows
      .map((r) => this.columns.map((c) => escape(r[c.key])).join(','))
      .join('\n');
    const totalsRow = [
      'TOTAL',
      '',
      '',
      '',
      '',
      this.total('basicSalary'),
      this.total('hra'),
      this.total('conveyanceAllowance'),
      this.total('medicalAllowance'),
      this.total('specialAllowance'),
      this.total('grossSalary'),
      this.total('providentFund'),
      this.total('professionalTax'),
      this.total('incomeTax'),
      this.total('lopDays'),
      this.total('unpaidLeaveDays'),
      this.total('leaveDeduction'),
      this.total('advanceDeduction'),
      this.total('totalDeductions'),
      this.total('netSalary'),
      '',
    ].join(',');
    const csv = `Salary Sheet - ${this.periodLabel}\n\n${header}\n${body}\n${totalsRow}\n`;
    this.saveBlob(new Blob([csv], { type: 'text/csv;charset=utf-8;' }), 'csv');
  }
  /** Excel opens CSV natively; a .xls wrapper keeps column types intact. */
  downloadExcel(): void {
    if (!this.rows.length) {
      this.toast.error('Nothing to export.');
      return;
    }
    const cell = (v: any) => `<td>${v ?? ''}</td>`;
    const header = `<tr>${this.columns.map((c) => `<th>${c.label}</th>`).join('')}</tr>`;
    const body = this.rows
      .map((r) => `<tr>${this.columns.map((c) => cell(r[c.key])).join('')}</tr>`)
      .join('');
    const html =
      `<html><head><meta charset="utf-8"></head><body>` +
      `<h3>Salary Sheet - ${this.periodLabel}</h3>` +
      `<table border="1">${header}${body}</table></body></html>`;
    this.saveBlob(new Blob([html], { type: 'application/vnd.ms-excel' }), 'xls');
  }
  async downloadPdf(): Promise<void> {
    if (!this.rows.length) {
      this.toast.error('Nothing to export.');
      return;
    }
    const { jsPDF } = await import('jspdf');
    const doc = new jsPDF({ orientation: 'landscape', unit: 'pt', format: 'a4' });
    doc.setFontSize(14);
    doc.text(`Salary Sheet - ${this.periodLabel}`, 40, 40);
    doc.setFontSize(8);
    const headers = ['Code', 'Name', 'Dept', 'Gross', 'LOP', 'Leave Ded.', 'Advance', 'Deductions', 'Net'];
    const widths = [60, 130, 100, 70, 40, 65, 65, 75, 75];
    let x = 40;
    let y = 70;
    headers.forEach((h, i) => {
      doc.text(h, x, y);
      x += widths[i];
    });
    doc.line(40, y + 4, 795, y + 4);
    y += 18;
    const num = (v: any) => (Number(v) || 0).toFixed(2);
    for (const r of this.rows) {
      if (y > 540) {
        doc.addPage();
        y = 50;
      }
      const cells = [
        r.employeeCode ?? '',
        (r.employeeName ?? '').substring(0, 26),
        (r.department ?? '-').substring(0, 18),
        num(r.grossSalary),
        String(r.lopDays ?? 0),
        num(r.leaveDeduction),
        num(r.advanceDeduction),
        num(r.totalDeductions),
        num(r.netSalary),
      ];
      x = 40;
      cells.forEach((c, i) => {
        doc.text(c, x, y);
        x += widths[i];
      });
      y += 14;
    }
    doc.line(40, y - 8, 795, y - 8);
    doc.setFontSize(9);
    doc.text(
      `TOTAL   Gross ${this.total('grossSalary').toFixed(2)}   Deductions ${this.total('totalDeductions').toFixed(2)}   Net ${this.total('netSalary').toFixed(2)}`,
      40,
      y + 8,
    );
    doc.save(`salary-sheet-${this.year}-${String(this.month).padStart(2, '0')}.pdf`);
  }
  print(): void {
    window.print();
  }
  private saveBlob(blob: Blob, extension: string): void {
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `salary-sheet-${this.year}-${String(this.month).padStart(2, '0')}.${extension}`;
    link.click();
    URL.revokeObjectURL(url);
  }
}
