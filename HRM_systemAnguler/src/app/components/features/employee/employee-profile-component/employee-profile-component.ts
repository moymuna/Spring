import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { EmployeeService } from '../../../../services/employee.service';
import { CountryService } from '../../../../services/country.service';
import { DivisionService } from '../../../../services/division.service';
import { DistrictService } from '../../../../services/district.service';
import { PolicestationService } from '../../../../services/policestation.service';
import { StorageService } from '../../../../services/storage.service';
import { ToastService } from '../../../../services/toast.service';
import { UserService } from '../../../../services/user.service';
import { ProfileEventsService } from '../../../../services/profile-events.service';
import { EmployeeResponse } from '../../../../models/employee.model';
import { environment } from '../../../../../environments/environment';
import { silentContext } from '../../../../interceptors/error.interceptor';
@Component({
  selector: 'app-employee-profile-component',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './employee-profile-component.html',
  styleUrl: './employee-profile-component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EmployeeProfileComponent implements OnInit {
  employee: EmployeeResponse | null = null;
  hasEmployeeRecord = false;
  profileForm!: FormGroup;
  loading = false;
  imageUrl = environment.imgUrl + 'employee/';
  employeePhotoUrl(filename: string | null | undefined): string {
    return filename ? this.storage.appendToken(this.imageUrl + filename) : '';
  }
  genders = ['MALE', 'FEMALE', 'OTHER'];
  countries: any[] = [];
  presentDivisions: any[] = [];
  presentDistricts: any[] = [];
  presentPoliceStations: any[] = [];
  permanentDivisions: any[] = [];
  permanentDistricts: any[] = [];
  permanentPoliceStations: any[] = [];
  emailForm!: FormGroup;
  emailSaving = false;
  editingAccount = false;
  passwordForm!: FormGroup;
  passwordSaving = false;
  editingPassword = false;
  signatureUrl = '';
  savingSignature = false;
  savingPhoto = false;
  constructor(
    private fb: FormBuilder,
    private employeeService: EmployeeService,
    private countryService: CountryService,
    private divisionService: DivisionService,
    private districtService: DistrictService,
    private policeStationService: PolicestationService,
    private storage: StorageService,
    private toast: ToastService,
    private userService: UserService,
    private profileEvents: ProfileEventsService,
    private http: HttpClient,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    this.createForm();
    this.emailForm = this.fb.group({
      fullName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
    });
    this.passwordForm = this.fb.group({
      oldPassword: ['', Validators.required],
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required],
    });
    this.countryService.getAllCountries().subscribe((data) => {
      this.countries = data;
      this.cdr.markForCheck();
    });
    const session = this.storage.getUser();
    if (session) {
      this.emailForm.patchValue({ fullName: session.fullName, email: session.email });
      this.userService.getUserById(session.id).subscribe((user) => {
        this.emailForm.patchValue({ fullName: user.fullName, email: user.email });
        this.signatureUrl = user.signaturePath
          ? this.storage.appendToken(`${environment.imgUrl}${user.signaturePath}`)
          : '';
        this.cdr.markForCheck();
      });
      this.employeeService.getByUserId(session.id, silentContext()).subscribe({
        next: (data) => {
          this.hasEmployeeRecord = true;
          this.patchEmployee(data);
          this.emailForm.patchValue({ fullName: data.fullName, email: data.email });
          this.cdr.markForCheck();
        },
        error: (err) => {
          if (err.status !== 404) {
            this.toast.error('Could not load your employee profile.');
          }
          this.cdr.markForCheck();
        },
      });
    }
  }
  toggleEditAccount(): void {
    this.editingAccount = !this.editingAccount;
    if (!this.editingAccount) {
      const session = this.storage.getUser();
      this.emailForm.patchValue({
        fullName: this.employee?.fullName ?? session?.fullName ?? '',
        email: this.employee?.email ?? session?.email ?? '',
      });
    }
  }
  togglePasswordForm(): void {
    this.editingPassword = !this.editingPassword;
    if (!this.editingPassword) {
      this.passwordForm.reset();
    }
  }
  saveEmail(): void {
    const session = this.storage.getUser();
    if (this.emailForm.invalid || !session) {
      this.emailForm.markAllAsTouched();
      return;
    }
    this.emailSaving = true;
    const payload: any = {
      fullName: this.emailForm.value.fullName,
      email: this.emailForm.value.email,
      role: this.employee?.role ?? session.role,
    };
    this.userService.updateUser(session.id, payload).subscribe({
      next: (updated) => {
        this.emailSaving = false;
        this.editingAccount = false;
        this.storage.updateUserSession({ fullName: updated.fullName, email: updated.email });
        this.profileEvents.profileUpdated();
        this.toast.success('Profile updated successfully.');
        this.cdr.markForCheck();
      },
      error: (err) => {
        this.emailSaving = false;
        this.toast.error(err.error?.message || 'Failed to update profile.');
        this.cdr.markForCheck();
      },
    });
  }
  changePassword(): void {
    if (this.passwordForm.invalid) {
      this.passwordForm.markAllAsTouched();
      return;
    }
    const { oldPassword, newPassword, confirmPassword } = this.passwordForm.value;
    if (newPassword !== confirmPassword) {
      this.toast.error('New password and confirmation do not match.');
      return;
    }
    this.passwordSaving = true;
    this.userService
      .changePassword(this.storage.getUser()!.id, oldPassword, newPassword)
      .subscribe({
        next: () => {
          this.passwordSaving = false;
          this.editingPassword = false;
          this.passwordForm.reset();
          this.toast.success('Password changed successfully.');
          this.cdr.markForCheck();
        },
        error: (err) => {
          this.passwordSaving = false;
          this.toast.error(err.error?.message || 'Failed to change password.');
          this.cdr.markForCheck();
        },
      });
  }
  createForm() {
    this.profileForm = this.fb.group({
      contractNo: [''],
      dateOfBirth: [''],
      gender: [''],
      bloodGroup: [''],
      presentAddress: this.createAddressForm(),
      permanentAddress: this.createAddressForm(),
    });
  }
  createAddressForm() {
    return this.fb.group({
      addressLine1: [''],
      addressLine2: [''],
      postOffice: [''],
      postalCode: [''],
      countryId: [''],
      divisionId: [''],
      districtId: [''],
      policeStationId: [''],
    });
  }
  patchEmployee(employee: EmployeeResponse) {
    this.employee = employee;
    this.profileForm.patchValue({
      contractNo: employee.contractNo,
      dateOfBirth: employee.dateOfBirth?.substring(0, 10),
      gender: employee.gender,
      bloodGroup: employee.bloodGroup,
    });
    if (employee.presentAddress) {
      this.profileForm.get('presentAddress')?.patchValue(employee.presentAddress);
    }
    if (employee.permanentAddress) {
      this.profileForm.get('permanentAddress')?.patchValue(employee.permanentAddress);
    }
    this.loadDependentAddressDropdowns('presentAddress');
    this.loadDependentAddressDropdowns('permanentAddress');
  }
  loadDependentAddressDropdowns(type: 'presentAddress' | 'permanentAddress') {
    const addressGroup = this.profileForm.get(type);
    if (!addressGroup) return;
    const countryId = addressGroup.get('countryId')?.value;
    const divisionId = addressGroup.get('divisionId')?.value;
    const districtId = addressGroup.get('districtId')?.value;
    if (countryId) {
      this.divisionService.getByCountryId(Number(countryId)).subscribe((data) => {
        if (type === 'presentAddress') this.presentDivisions = data;
        else this.permanentDivisions = data;
        this.cdr.markForCheck();
      });
    }
    if (divisionId) {
      this.districtService.getByDivisionId(Number(divisionId)).subscribe((data) => {
        if (type === 'presentAddress') this.presentDistricts = data;
        else this.permanentDistricts = data;
        this.cdr.markForCheck();
      });
    }
    if (districtId) {
      this.policeStationService.getByDistrictId(Number(districtId)).subscribe((data) => {
        if (type === 'presentAddress') this.presentPoliceStations = data;
        else this.permanentPoliceStations = data;
        this.cdr.markForCheck();
      });
    }
  }
  onCountryChange(type: 'presentAddress' | 'permanentAddress') {
    const addressGroup = this.profileForm.get(type);
    if (!addressGroup) return;
    const countryId = addressGroup.get('countryId')?.value;
    addressGroup.patchValue({ divisionId: '', districtId: '', policeStationId: '' });
    if (type === 'presentAddress') {
      this.presentDistricts = [];
      this.presentPoliceStations = [];
    } else {
      this.permanentDistricts = [];
      this.permanentPoliceStations = [];
    }
    if (countryId) {
      this.divisionService.getByCountryId(Number(countryId)).subscribe((data) => {
        if (type === 'presentAddress') this.presentDivisions = data;
        else this.permanentDivisions = data;
        this.cdr.markForCheck();
      });
    }
  }
  onDivisionChange(type: 'presentAddress' | 'permanentAddress') {
    const addressGroup = this.profileForm.get(type);
    if (!addressGroup) return;
    const divisionId = addressGroup.get('divisionId')?.value;
    addressGroup.patchValue({ districtId: '', policeStationId: '' });
    if (type === 'presentAddress') this.presentPoliceStations = [];
    else this.permanentPoliceStations = [];
    if (divisionId) {
      this.districtService.getByDivisionId(Number(divisionId)).subscribe((data) => {
        if (type === 'presentAddress') this.presentDistricts = data;
        else this.permanentDistricts = data;
        this.cdr.markForCheck();
      });
    }
  }
  onDistrictChange(type: 'presentAddress' | 'permanentAddress') {
    const addressGroup = this.profileForm.get(type);
    if (!addressGroup) return;
    const districtId = addressGroup.get('districtId')?.value;
    addressGroup.patchValue({ policeStationId: '' });
    if (districtId) {
      this.policeStationService.getByDistrictId(Number(districtId)).subscribe((data) => {
        if (type === 'presentAddress') this.presentPoliceStations = data;
        else this.permanentPoliceStations = data;
        this.cdr.markForCheck();
      });
    }
  }
  private buildUpdatePayload(): any {
    return {
      contractNo: this.employee!.contractNo,
      joiningDate: this.employee!.joiningDate,
      employeeCode: this.employee!.employeeCode,
      employmentType: this.employee!.employmentType,
      status: this.employee!.status,
      fullName: this.employee!.fullName,
      email: this.employee!.email,
      role: this.employee!.role,
      departmentId: this.employee!.departmentId,
      designationId: this.employee!.designationId,
      officeId: this.employee!.officeId,
      managerId: this.employee!.managerId,
      ...this.profileForm.value,
    };
  }
  save() {
    if (!this.employee) return;
    this.loading = true;
    this.employeeService
      .updateEmployee(this.employee.id, this.buildUpdatePayload(), null)
      .subscribe({
        next: (data) => {
          this.loading = false;
          this.patchEmployee(data);
          this.toast.success('Profile updated successfully.');
          this.cdr.markForCheck();
        },
        error: () => {
          this.loading = false;
          this.cdr.markForCheck();
        },
      });
  }
  onPhotoSelected(event: any): void {
    const file = event.target.files[0];
    if (!file || !this.employee) return;
    this.savingPhoto = true;
    this.employeeService
      .updateEmployee(this.employee.id, this.buildUpdatePayload(), file)
      .subscribe({
        next: (data) => {
          this.savingPhoto = false;
          this.patchEmployee(data);
          this.profileEvents.avatarChanged();
          this.toast.success('Profile photo updated successfully.');
          this.cdr.markForCheck();
        },
        error: () => {
          this.savingPhoto = false;
          this.toast.error('Failed to update profile photo.');
          this.cdr.markForCheck();
        },
      });
  }
  onSignatureSelected(event: any): void {
    const file = event.target.files[0];
    const session = this.storage.getUser();
    if (!file || !session) return;
    this.savingSignature = true;
    const formData = new FormData();
    formData.append('file', file);
    this.http.post<any>(`${environment.apiUrl}upload`, formData).subscribe({
      next: (res) => {
        this.userService.getUserById(session.id).subscribe((user) => {
          this.userService
            .updateUser(session.id, { ...user, signaturePath: res.filePath })
            .subscribe({
              next: (updated) => {
                this.signatureUrl = updated.signaturePath
                  ? this.storage.appendToken(`${environment.imgUrl}${updated.signaturePath}`)
                  : '';
                this.savingSignature = false;
                this.toast.success('Signature updated successfully.');
                this.cdr.markForCheck();
              },
              error: () => {
                this.savingSignature = false;
                this.toast.error('Failed to save signature.');
                this.cdr.markForCheck();
              },
            });
        });
      },
      error: () => {
        this.savingSignature = false;
        this.toast.error('Failed to upload signature.');
        this.cdr.markForCheck();
      },
    });
  }
}
