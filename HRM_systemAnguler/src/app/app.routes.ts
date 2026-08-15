import { Routes } from '@angular/router';
import { MainLayout } from './components/shared/layout/main-layout/main-layout';
import { PublicLayout } from './components/public/public-layout/public-layout';
import { Landing } from './components/public/landing/landing';
import { Contact } from './components/public/contact/contact';
import { AddEmployee } from './components/features/employee/add-employee/add-employee';
import { AddApplicant } from './components/features/applicant/add-applicant/add-applicant';
import { EmployeeList } from './components/features/employee/employee-list/employee-list';
import { CountryList } from './components/features/location/country/country-list/country-list';
import { CountryAdd } from './components/features/location/country/country-add/country-add';
import { CountryEdit } from './components/features/location/country/country-edit/country-edit';
import { DivisionList } from './components/features/location/division/division-list/division-list';
import { AddDivision } from './components/features/location/division/add-division/add-division';
import { EditDivision } from './components/features/location/division/edit-division/edit-division';
import { DepartmentList } from './components/features/department/department-list/department-list';
import { AddDepartment } from './components/features/department/add-department/add-department';
import { EditDepartment } from './components/features/department/edit-department/edit-department';
import { ListDesignation } from './components/features/designation/list-designation/list-designation';
import { AddDesignation } from './components/features/designation/add-designation/add-designation';
import { EditDesignation } from './components/features/designation/edit-designation/edit-designation';
import { ListOffice } from './components/features/office/list-office/list-office';
import { AddOffice } from './components/features/office/add-office/add-office';
import { EditOffice } from './components/features/office/edit-office/edit-office';
import { PolicestationList } from './components/features/location/policestation/policestation-list/policestation-list';
import { AddPolicestation } from './components/features/location/policestation/add-policestation/add-policestation';
import { EditPolicestation } from './components/features/location/policestation/edit-policestation/edit-policestation';
import { DistrictList } from './components/features/location/district/district-list/district-list';
import { AddDistrict } from './components/features/location/district/add-district/add-district';
import { EditDistrict } from './components/features/location/district/edit-district/edit-district';
import { EditEmployee } from './components/features/employee/edit-employee/edit-employee';
import { AttendanceEdit } from './components/features/attendance/attendance-edit/attendance-edit';
import { AttendanceCreate } from './components/features/attendance/attendance-create/attendance-create';
import { AttendanceList } from './components/features/attendance/attendance-list/attendance-list';
import { LeaveCreate } from './components/features/leave/leave-create/leave-create';
import { LeaveList } from './components/features/leave/leave-list/leave-list';
import { LeaveEdit } from './components/features/leave/leave-edit/leave-edit';
import { ListApplicant } from './components/features/applicant/list-applicant/list-applicant';
import { EditApplicant } from './components/features/applicant/edit-applicant/edit-applicant';
import { ListApplication } from './components/features/application/list-application/list-application';
import { AddApplication } from './components/features/application/add-application/add-application';
import { EditApplication } from './components/features/application/edit-application/edit-application';
import { HireApplicant } from './components/features/application/hire-applicant/hire-applicant';
import { ListDocuments } from './components/features/documents/list-documents/list-documents';
import { AddDocuments } from './components/features/documents/add-documents/add-documents';
import { EditDocuments } from './components/features/documents/edit-documents/edit-documents';
import { CompanyBank } from './components/features/companybank/company-bank';
import { LeavetypeList } from './components/features/leavetype/leavetype-list/leavetype-list';
import { LeavetypeAdd } from './components/features/leavetype/leavetype-add/leavetype-add';
import { LeavetypeEdit } from './components/features/leavetype/leavetype-edit/leavetype-edit';
import { LeavebalanceEdit } from './components/features/leavebalance/leavebalance-edit/leavebalance-edit';
import { LeavebalanceAdd } from './components/features/leavebalance/leavebalance-add/leavebalance-add';
import { LeavebalanceList } from './components/features/leavebalance/leavebalance-list/leavebalance-list';
import { ListHoliday } from './components/features/holiday/list-holiday/list-holiday';
import { AddHoliday } from './components/features/holiday/add-holiday/add-holiday';
import { EditHoliday } from './components/features/holiday/edit-holiday/edit-holiday';
import { ListInterview } from './components/features/interview/list-interview/list-interview';
import { AddInterview } from './components/features/interview/add-interview/add-interview';
import { EditInterview } from './components/features/interview/edit-interview/edit-interview';
import { ListJobpost } from './components/features/jobpost/list-jobpost/list-jobpost';
import { AddJobpost } from './components/features/jobpost/add-jobpost/add-jobpost';
import { EditJobpost } from './components/features/jobpost/edit-jobpost/edit-jobpost';
import { ListNotice } from './components/features/notice/list-notice/list-notice';
import { AddNotice } from './components/features/notice/add-notice/add-notice';
import { EditNotice } from './components/features/notice/edit-notice/edit-notice';
import { ListPayroll } from './components/features/payroll/list-payroll/list-payroll';
import { SalarySheet } from './components/features/payroll/salary-sheet/salary-sheet';
import { AddPayroll } from './components/features/payroll/add-payroll/add-payroll';
import { EditPayroll } from './components/features/payroll/edit-payroll/edit-payroll';
import { ListPayslip } from './components/features/payslip/list-payslip/list-payslip';
import { AddPayslip } from './components/features/payslip/add-payslip/add-payslip';
import { EditPayslip } from './components/features/payslip/edit-payslip/edit-payslip';
import { ListPerformancereview } from './components/features/performancereview/list-performancereview/list-performancereview';
import { AddPerformancereview } from './components/features/performancereview/add-performancereview/add-performancereview';
import { EditPerformancereview } from './components/features/performancereview/edit-performancereview/edit-performancereview';
import { ListPrioject } from './components/features/project/list-prioject/list-prioject';
import { AddPrioject } from './components/features/project/add-prioject/add-prioject';
import { EditPrioject } from './components/features/project/edit-prioject/edit-prioject';
import { ListSalary } from './components/features/salary/list-salary/list-salary';
import { AddSalary } from './components/features/salary/add-salary/add-salary';
import { EditSalary } from './components/features/salary/edit-salary/edit-salary';
import { MySalary } from './components/features/salary/my-salary/my-salary';
import { SalarygradeList } from './components/features/salarygrade/salarygrade-list/salarygrade-list';
import { SalarygradeAdd } from './components/features/salarygrade/salarygrade-add/salarygrade-add';
import { SalarygradeEdit } from './components/features/salarygrade/salarygrade-edit/salarygrade-edit';
import { AdvanceList } from './components/features/advance/advance-list/advance-list';
import { AdvanceCreate } from './components/features/advance/advance-create/advance-create';
import { AdvanceEdit } from './components/features/advance/advance-edit/advance-edit';
import { ListTraining } from './components/features/training/list-training/list-training';
import { AddTraining } from './components/features/training/add-training/add-training';
import { EditTraining } from './components/features/training/edit-training/edit-training';
import { UserCreate } from './components/features/user/user-create/user-create';
import { UserEdit } from './components/features/user/user-edit/user-edit';
import { UserList } from './components/features/user/user-list/user-list';
import { Login } from './components/auth/login/login';
import { RollRedirect } from './components/auth/roll-redirect/roll-redirect';
import { Register } from './components/auth/register/register';
import { ForgotPassword } from './components/auth/forgot-password/forgot-password';
import { ResetPassword } from './components/auth/reset-password/reset-password';
import { VeryfyEmail } from './components/auth/veryfy-email/veryfy-email';
import { authGuard, roleGuard } from './guards/auth-guard';
import { AdminDashboard } from './components/features/dashboards/admin-dashboard/admin-dashboard';
import { HrDashboard } from './components/features/dashboards/hr-dashboard/hr-dashboard';
import { ManagerDashboard } from './components/features/dashboards/manager-dashboard/manager-dashboard';
import { EmployeeDashboard } from './components/features/dashboards/employee-dashboard/employee-dashboard';
import { ApplicantDashboard } from './components/features/dashboards/applicant-dashboard/applicant-dashboard';
import { EmployeeDetail } from './components/features/employee/employee-detail/employee-detail';
import { EmployeeProfileComponent } from './components/features/employee/employee-profile-component/employee-profile-component';
import { AuditLogList } from './components/features/audit-log/audit-log-list/audit-log-list';
import { AttendanceDetail } from './components/features/attendance/attendance-detail/attendance-detail';
export const routes: Routes = [
  {
    path: '',
    component: PublicLayout,
    children: [
      { path: '', component: Landing },
      { path: 'contact', component: Contact },
    ],
  },
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  { path: 'forgot-password', component: ForgotPassword },
  { path: 'reset-password', component: ResetPassword },
  { path: 'verify-email', component: VeryfyEmail },
  {
    path: '',
    component: MainLayout,
    canActivate: [authGuard],
    children: [
      // Each add/edit route is a child of its list route, so the form renders into
      // the list page's modal outlet instead of navigating away. URLs are unchanged.
      {
        path: 'applicant',
        component: ListApplicant,
        canActivate: [roleGuard(['ADMIN', 'HR'])],
        children: [
          { path: 'add', component: AddApplicant },
          { path: 'edit/:id', component: EditApplicant },
        ],
      },
      {
        path: 'application',
        component: ListApplication,
        canActivate: [roleGuard(['ADMIN', 'HR'])],
        children: [
          { path: 'add', component: AddApplication },
          { path: 'edit/:id', component: EditApplication },
          { path: 'hire/:id', component: HireApplicant },
        ],
      },
      {
        path: 'country',
        component: CountryList,
        children: [
          { path: 'add', component: CountryAdd, canActivate: [roleGuard(['ADMIN'])] },
          { path: 'edit/:id', component: CountryEdit, canActivate: [roleGuard(['ADMIN'])] },
        ],
      },
      {
        path: 'division',
        component: DivisionList,
        children: [
          { path: 'add', component: AddDivision, canActivate: [roleGuard(['ADMIN'])] },
          { path: 'edit/:id', component: EditDivision, canActivate: [roleGuard(['ADMIN'])] },
        ],
      },
      {
        path: 'district',
        component: DistrictList,
        children: [
          { path: 'add', component: AddDistrict, canActivate: [roleGuard(['ADMIN'])] },
          { path: 'edit/:id', component: EditDistrict, canActivate: [roleGuard(['ADMIN'])] },
        ],
      },
      {
        path: 'policestation',
        component: PolicestationList,
        children: [
          { path: 'add', component: AddPolicestation, canActivate: [roleGuard(['ADMIN'])] },
          { path: 'edit/:id', component: EditPolicestation, canActivate: [roleGuard(['ADMIN'])] },
        ],
      },
      {
        path: 'department',
        component: DepartmentList,
        canActivate: [roleGuard(['ADMIN', 'HR', 'MANAGER'])],
        children: [
          { path: 'add', component: AddDepartment, canActivate: [roleGuard(['ADMIN'])] },
          { path: 'edit/:id', component: EditDepartment, canActivate: [roleGuard(['ADMIN'])] },
        ],
      },
      {
        path: 'designation',
        component: ListDesignation,
        canActivate: [roleGuard(['ADMIN', 'HR', 'MANAGER'])],
        children: [
          { path: 'add', component: AddDesignation, canActivate: [roleGuard(['ADMIN'])] },
          { path: 'edit/:id', component: EditDesignation, canActivate: [roleGuard(['ADMIN'])] },
        ],
      },
      {
        path: 'documents',
        component: ListDocuments,
        canActivate: [roleGuard(['ADMIN', 'HR', 'EMPLOYEE'])],
        children: [
          {
            path: 'add',
            component: AddDocuments,
            canActivate: [roleGuard(['ADMIN', 'HR', 'MANAGER', 'EMPLOYEE'])],
          },
          {
            path: 'edit/:id',
            component: EditDocuments,
            canActivate: [roleGuard(['ADMIN', 'HR', 'MANAGER'])],
          },
        ],
      },
      {
        path: 'office',
        component: ListOffice,
        canActivate: [roleGuard(['ADMIN', 'HR', 'MANAGER'])],
        children: [
          { path: 'add', component: AddOffice, canActivate: [roleGuard(['ADMIN'])] },
          { path: 'edit/:id', component: EditOffice, canActivate: [roleGuard(['ADMIN'])] },
        ],
      },
      {
        path: 'employee',
        canActivate: [roleGuard(['ADMIN', 'HR', 'MANAGER'])],
        children: [
          // A full detail page, not a form, so it stays outside the modal outlet.
          { path: 'view/:id', component: EmployeeDetail },
          {
            path: '',
            component: EmployeeList,
            children: [
              { path: 'add', component: AddEmployee, canActivate: [roleGuard(['ADMIN', 'HR'])] },
              {
                path: 'edit/:id',
                component: EditEmployee,
                canActivate: [roleGuard(['ADMIN', 'HR'])],
              },
            ],
          },
        ],
      },
      {
        path: 'attendance',
        canActivate: [roleGuard(['ADMIN', 'HR', 'MANAGER', 'EMPLOYEE'])],
        children: [
          // The employee's own attendance history is a full page, not a form.
          { path: 'details', component: AttendanceDetail },
          {
            path: '',
            component: AttendanceList,
            canActivate: [roleGuard(['ADMIN', 'HR', 'MANAGER'])],
            children: [
              {
                path: 'add',
                component: AttendanceCreate,
                canActivate: [roleGuard(['ADMIN', 'HR', 'MANAGER'])],
              },
              {
                path: 'edit/:id',
                component: AttendanceEdit,
                canActivate: [roleGuard(['ADMIN', 'HR'])],
              },
            ],
          },
        ],
      },
      {
        path: 'leave',
        component: LeaveList,
        canActivate: [roleGuard(['ADMIN', 'HR', 'MANAGER', 'EMPLOYEE'])],
        children: [
          { path: 'add', component: LeaveCreate },
          {
            path: 'edit/:id',
            component: LeaveEdit,
            canActivate: [roleGuard(['ADMIN', 'HR', 'MANAGER'])],
          },
        ],
      },
      {
        path: 'leavetype',
        component: LeavetypeList,
        canActivate: [roleGuard(['ADMIN', 'HR', 'MANAGER', 'EMPLOYEE'])],
        children: [
          { path: 'add', component: LeavetypeAdd, canActivate: [roleGuard(['ADMIN', 'HR'])] },
          { path: 'edit/:id', component: LeavetypeEdit, canActivate: [roleGuard(['ADMIN', 'HR'])] },
        ],
      },
      {
        path: 'leavebalance',
        component: LeavebalanceList,
        canActivate: [roleGuard(['ADMIN', 'HR', 'EMPLOYEE'])],
        children: [
          { path: 'add', component: LeavebalanceAdd, canActivate: [roleGuard(['ADMIN', 'HR'])] },
          {
            path: 'edit/:id',
            component: LeavebalanceEdit,
            canActivate: [roleGuard(['ADMIN', 'HR'])],
          },
        ],
      },
      {
        path: 'holiday',
        component: ListHoliday,
        canActivate: [roleGuard(['ADMIN', 'HR', 'MANAGER', 'EMPLOYEE'])],
        children: [
          { path: 'add', component: AddHoliday, canActivate: [roleGuard(['ADMIN', 'HR'])] },
          { path: 'edit/:id', component: EditHoliday, canActivate: [roleGuard(['ADMIN', 'HR'])] },
        ],
      },
      {
        path: 'interview',
        component: ListInterview,
        canActivate: [roleGuard(['ADMIN', 'HR'])],
        children: [
          { path: 'add', component: AddInterview },
          { path: 'edit/:id', component: EditInterview },
        ],
      },
      {
        path: 'jobpost',
        component: ListJobpost,
        canActivate: [roleGuard(['ADMIN', 'HR', 'APPLICANT'])],
        children: [
          { path: 'add', component: AddJobpost, canActivate: [roleGuard(['ADMIN', 'HR'])] },
          { path: 'edit/:id', component: EditJobpost, canActivate: [roleGuard(['ADMIN', 'HR'])] },
        ],
      },
      {
        path: 'notice',
        component: ListNotice,
        canActivate: [roleGuard(['ADMIN', 'HR', 'MANAGER', 'EMPLOYEE'])],
        children: [
          { path: 'add', component: AddNotice, canActivate: [roleGuard(['ADMIN', 'HR'])] },
          { path: 'edit/:id', component: EditNotice, canActivate: [roleGuard(['ADMIN', 'HR'])] },
        ],
      },
      {
        path: 'payroll',
        component: ListPayroll,
        canActivate: [roleGuard(['ADMIN', 'HR'])],
        children: [
          { path: 'add', component: AddPayroll },
          { path: 'edit/:id', component: EditPayroll },
        ],
      },
      {
        path: 'salary-sheet',
        component: SalarySheet,
        canActivate: [roleGuard(['ADMIN', 'HR'])],
      },
      {
        path: 'company-bank',
        component: CompanyBank,
        canActivate: [roleGuard(['ADMIN', 'HR'])],
      },
      {
        path: 'payslip',
        component: ListPayslip,
        canActivate: [roleGuard(['ADMIN', 'HR', 'EMPLOYEE'])],
        children: [
          { path: 'add', component: AddPayslip, canActivate: [roleGuard(['ADMIN', 'HR'])] },
          { path: 'edit/:id', component: EditPayslip, canActivate: [roleGuard(['ADMIN', 'HR'])] },
        ],
      },
      {
        path: 'performancereview',
        component: ListPerformancereview,
        canActivate: [roleGuard(['ADMIN', 'HR', 'MANAGER'])],
        children: [
          { path: 'add', component: AddPerformancereview },
          { path: 'edit/:id', component: EditPerformancereview },
        ],
      },
      {
        path: 'project',
        component: ListPrioject,
        canActivate: [roleGuard(['ADMIN', 'HR', 'MANAGER', 'EMPLOYEE'])],
        children: [
          {
            path: 'add',
            component: AddPrioject,
            canActivate: [roleGuard(['ADMIN', 'HR', 'MANAGER'])],
          },
          {
            path: 'edit/:id',
            component: EditPrioject,
            canActivate: [roleGuard(['ADMIN', 'MANAGER'])],
          },
        ],
      },
      {
        path: 'salary',
        component: ListSalary,
        canActivate: [roleGuard(['ADMIN', 'HR'])],
        children: [
          { path: 'add', component: AddSalary },
          { path: 'edit/:id', component: EditSalary },
        ],
      },
      {
        // The pay scale is admin-owned; everyone else may read it.
        path: 'salarygrade',
        component: SalarygradeList,
        canActivate: [roleGuard(['ADMIN', 'HR', 'MANAGER', 'EMPLOYEE'])],
        children: [
          { path: 'add', component: SalarygradeAdd, canActivate: [roleGuard(['ADMIN'])] },
          { path: 'edit/:id', component: SalarygradeEdit, canActivate: [roleGuard(['ADMIN'])] },
        ],
      },
      {
        path: 'advance',
        component: AdvanceList,
        canActivate: [roleGuard(['ADMIN', 'HR', 'MANAGER', 'EMPLOYEE'])],
        children: [
          { path: 'add', component: AdvanceCreate },
          {
            path: 'edit/:id',
            component: AdvanceEdit,
            canActivate: [roleGuard(['ADMIN', 'HR', 'MANAGER'])],
          },
        ],
      },
      {
        path: 'training',
        component: ListTraining,
        canActivate: [roleGuard(['ADMIN', 'HR', 'MANAGER', 'EMPLOYEE'])],
        children: [
          {
            path: 'add',
            component: AddTraining,
            canActivate: [roleGuard(['ADMIN', 'HR', 'MANAGER'])],
          },
          {
            path: 'edit/:id',
            component: EditTraining,
            canActivate: [roleGuard(['ADMIN', 'HR', 'MANAGER'])],
          },
        ],
      },
      {
        path: 'user',
        component: UserList,
        canActivate: [roleGuard(['ADMIN'])],
        children: [
          { path: 'add', component: UserCreate },
          { path: 'edit/:id', component: UserEdit },
        ],
      },
      { path: 'dashboard', component: RollRedirect },
      { path: 'admin-dashboard', component: AdminDashboard, canActivate: [roleGuard(['ADMIN'])] },
      { path: 'hr-dashboard', component: HrDashboard, canActivate: [roleGuard(['ADMIN', 'HR'])] },
      {
        path: 'manager-dashboard',
        component: ManagerDashboard,
        canActivate: [roleGuard(['ADMIN', 'MANAGER'])],
      },
      {
        path: 'employee-dashboard',
        component: EmployeeDashboard,
        canActivate: [roleGuard(['ADMIN', 'HR', 'MANAGER', 'EMPLOYEE'])],
      },
      {
        path: 'my-profile',
        component: EmployeeProfileComponent,
        canActivate: [roleGuard(['ADMIN', 'HR', 'MANAGER', 'EMPLOYEE'])],
      },
      {
        path: 'my-salary',
        component: MySalary,
        canActivate: [roleGuard(['ADMIN', 'HR', 'MANAGER', 'EMPLOYEE'])],
      },
      { path: 'audit-log', component: AuditLogList, canActivate: [roleGuard(['ADMIN'])] },
      {
        path: 'applicant-dashboard',
        component: ApplicantDashboard,
        canActivate: [roleGuard(['APPLICANT'])],
      },
    ],
  },
  {
    path: '**',
    redirectTo: '',
  },
];
