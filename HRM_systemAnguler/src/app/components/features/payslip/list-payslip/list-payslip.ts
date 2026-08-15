import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { jsPDF } from 'jspdf';
import { Payslip } from '../../../../models/payslip.model';
import { PayslipService } from '../../../../services/payslip.service';
import { EmployeeService } from '../../../../services/employee.service';
import { silentContext } from '../../../../interceptors/error.interceptor';
import { StorageService } from '../../../../services/storage.service';
import { ModalOutlet } from '../../../shared/modal-outlet/modal-outlet';
const MONTH_NAMES = [
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
  selector: 'app-list-payslip',
  standalone: true,
  imports: [CommonModule, RouterModule, ModalOutlet],
  templateUrl: './list-payslip.html',
  styleUrl: './list-payslip.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ListPayslip implements OnInit {
  payslips: Payslip[] = [];
  successMessage = '';
  isEmployeeOnly = false;
  constructor(
    private service: PayslipService,
    private employeeService: EmployeeService,
    private storage: StorageService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit() {
    this.isEmployeeOnly = this.storage.getRole() === 'EMPLOYEE';
    this.load();
  }
  load() {
    if (this.isEmployeeOnly) {
      const userId = this.storage.getUser()?.id;
      if (!userId) {
        return;
      }
      this.employeeService.getByUserId(userId, silentContext()).subscribe({
        next: (emp) => {
          this.service.getPayslipsByEmployee(emp.id!).subscribe((data) => {
            this.payslips = data;
            this.cdr.markForCheck();
          });
        },
      });
      return;
    }
    this.service.getAllPayslips().subscribe((data) => {
      this.payslips = data;
      this.cdr.markForCheck();
    });
  }
  delete(id: number) {
    if (confirm('Delete Payslip?')) {
      this.service.deletePayslip(id).subscribe(() => {
        this.successMessage = 'Payslip deleted successfully';
        this.cdr.markForCheck();
        this.load();
      });
    }
  }
  downloadPayslip(p: Payslip): void {
    const doc = new jsPDF();
    const monthName = MONTH_NAMES[p.month - 1] ?? p.month;
    doc.setFontSize(16);
    doc.text('Payslip', 14, 18);
    doc.setFontSize(11);
    doc.text(`Pay Period: ${monthName} ${p.year}`, 14, 28);
    doc.text(`Employee: ${p.employeeName ?? '-'}`, 14, 36);
    doc.text(`Status: ${p.status}`, 14, 44);
    const rows: [string, string][] = [
      ['Gross Salary', p.grossSalary?.toFixed(2) ?? '-'],
      ['Paid Days', String(p.paidDays ?? '-')],
      ['Deductions', ''],
      ['  Provident Fund', (p.providentFund ?? 0).toFixed(2)],
      ['  Professional Tax', (p.professionalTax ?? 0).toFixed(2)],
      ['  Income Tax', (p.incomeTax ?? 0).toFixed(2)],
      [`  Absent / Loss of Pay (${p.lopDays ?? 0} days)`, (p.lopDeduction ?? 0).toFixed(2)],
      [`  Unpaid Leave (${p.unpaidLeaveDays ?? 0} days)`, (p.leaveDeduction ?? 0).toFixed(2)],
      ['  Advance Recovery', (p.advanceDeduction ?? 0).toFixed(2)],
      ['Total Deductions', p.totalDeductions?.toFixed(2) ?? '-'],
      ['Net Salary', p.netSalary?.toFixed(2) ?? '-'],
      ['Payment', ''],
      ['  Payment Mode', p.bankAccountNumber ? 'Bank Transfer' : 'Manual / Cash'],
      ['  Bank', p.bankName ?? '-'],
      ['  Account', p.bankAccountNumber ?? '-'],
      ['  Paid On', p.paidAt ? String(p.paidAt).substring(0, 10) : 'Not paid yet'],
    ];
    let y = 58;
    doc.setLineWidth(0.2);
    doc.line(14, y - 6, 196, y - 6);
    rows.forEach(([label, value]) => {
      doc.text(label, 14, y);
      doc.text(value, 120, y);
      y += 10;
    });
    doc.line(14, y - 4, 196, y - 4);
    doc.save(`payslip-${monthName}-${p.year}.pdf`);
  }
}
