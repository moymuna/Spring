import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { PayrollStatus } from '../../../../models/payslip.model';
import { PayslipService } from '../../../../services/payslip.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastService } from '../../../../services/toast.service';
@Component({
  selector: 'app-edit-payslip',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './edit-payslip.html',
  styleUrl: './edit-payslip.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EditPayslip implements OnInit {
  form!: FormGroup;
  id!: number;
  statuses = Object.values(PayrollStatus);
  constructor(
    private fb: FormBuilder,
    private service: PayslipService,
    private route: ActivatedRoute,
    private router: Router,
    private toast: ToastService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit() {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.form = this.fb.group({
      month: [],
      year: [],
      grossSalary: [],
      totalDeductions: [],
      netSalary: [],
      paidDays: [],
      lopDays: [],
      status: [],
      generatedAt: [],
      paidAt: [],
      employeeId: [],
      payrollId: [],
    });
    this.service.getPayslipById(this.id).subscribe((data) => {
      this.form.patchValue(data);
      this.cdr.markForCheck();
    });
  }
  update() {
    this.service.updatePayslip(this.id, this.form.value).subscribe(() => {
      this.toast.success('Updated');
      this.router.navigate(['/payslip']);
    });
  }
}
