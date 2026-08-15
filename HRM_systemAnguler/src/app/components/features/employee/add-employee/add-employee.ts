import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  inject,
  OnInit,
} from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import {
  Department,
  Designation,
  District,
  Division,
  EmployeeStatus,
  EmploymentType,
  Gender,
  Office,
  PoliceStation,
  Role,
} from '../../../../models/employee.model';
import { Country } from '../../../../models/country';
import { Router } from '@angular/router';
import { EmployeeService } from '../../../../services/employee.service';
@Component({
  selector: 'app-add-employee',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './add-employee.html',
  styleUrl: './add-employee.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AddEmployee implements OnInit {
  private fb = inject(FormBuilder);
  private employeeService = inject(EmployeeService);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);
  selectedFile: File | null = null;
  imagePreview: string | ArrayBuffer | null = null;
  employeeForm!: FormGroup;
  showPassword = false;
  departments: Department[] = [];
  designations: Designation[] = [];
  offices: Office[] = [];
  managers: any[] = [];
  countries: Country[] = [];
  presentDivisions: Division[] = [];
  presentDistricts: District[] = [];
  presentPoliceStations: PoliceStation[] = [];
  permanentDivisions: Division[] = [];
  permanentDistricts: District[] = [];
  permanentPoliceStations: PoliceStation[] = [];
  employeeStatuses = Object.values(EmployeeStatus);
  employmentTypes = Object.values(EmploymentType);
  genders = Object.values(Gender);
  roles = Object.values(Role);
  loading: boolean = false;
  errorMessage: string = '';
  sameAsPresent = false;
  ngOnInit(): void {
    this.createForm();
    this.loadDepartments();
    this.loadOffices();
    this.loadCountries();
    this.loadManagers();
    this.employeeForm.get('presentAddress')?.valueChanges.subscribe((value) => {
      if (this.sameAsPresent) {
        this.employeeForm.get('permanentAddress')?.patchValue(value, { emitEvent: false });
        this.permanentDivisions = this.presentDivisions;
        this.permanentDistricts = this.presentDistricts;
        this.permanentPoliceStations = this.presentPoliceStations;
        this.cdr.markForCheck();
      }
    });
  }
  toggleSameAsPresent(checked: boolean) {
    this.sameAsPresent = checked;
    if (checked) {
      const presentValue = this.employeeForm.get('presentAddress')?.value;
      this.employeeForm.get('permanentAddress')?.patchValue(presentValue, { emitEvent: false });
      this.permanentDivisions = this.presentDivisions;
      this.permanentDistricts = this.presentDistricts;
      this.permanentPoliceStations = this.presentPoliceStations;
    }
  }
  onFileSelected(event: any) {
    const file: File = event.target.files[0];
    if (!file) return;
    this.selectedFile = file;
    const reader = new FileReader();
    reader.onload = () => {
      this.imagePreview = reader.result;
      this.cdr.markForCheck();
    };
    reader.readAsDataURL(file);
  }
  removeSelectedFile(fileInput: HTMLInputElement) {
    this.selectedFile = null;
    this.imagePreview = null;
    fileInput.value = '';
  }
  createForm() {
    this.employeeForm = this.fb.group({
      contractNo: ['', Validators.required],
      joiningDate: ['', Validators.required],
      dateOfBirth: [''],
      status: [EmployeeStatus.ACTIVE, Validators.required],
      gender: ['', Validators.required],
      bloodGroup: [''],
      employmentType: [EmploymentType.FULL_TIME, Validators.required],
      bankName: [''],
      bankBranch: [''],
      bankAccountName: [''],
      bankAccountNumber: [''],
      image: this.selectedFile,
      fullName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      role: [Role.EMPLOYEE, Validators.required],
      departmentId: [''],
      designationId: [''],
      officeId: [''],
      managerId: [''],
      presentAddress: this.createAddressForm(),
      permanentAddress: this.createAddressForm(),
    });
  }
  createAddressForm(): FormGroup {
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
  get presentAddressForm(): FormGroup {
    return this.employeeForm.get('presentAddress') as FormGroup;
  }
  get permanentAddressForm(): FormGroup {
    return this.employeeForm.get('permanentAddress') as FormGroup;
  }
  loadDepartments() {
    this.employeeService.getDepartments().subscribe({
      next: (data) => {
        this.departments = data;
        this.cdr.markForCheck();
      },
      error: () => {
        this.errorMessage = 'Failed to load departments';
        this.cdr.markForCheck();
      },
    });
  }
  loadOffices() {
    this.employeeService.getOffices().subscribe({
      next: (data) => {
        this.offices = data;
        this.cdr.markForCheck();
      },
    });
  }
  loadManagers() {
    this.employeeService.getManagers().subscribe({
      next: (data) => {
        this.managers = data;
        this.cdr.markForCheck();
      },
    });
  }
  loadCountries() {
    this.employeeService.getCountries().subscribe({
      next: (data) => {
        this.countries = data;
        this.cdr.markForCheck();
      },
    });
  }
  onDepartmentChange() {
    const departmentId = this.employeeForm.get('departmentId')?.value;
    if (departmentId) {
      this.employeeService.getDesignationsByDepartment(departmentId).subscribe({
        next: (data) => {
          this.designations = data;
          console.log('Designations loaded:', data);
          this.cdr.markForCheck();
        },
      });
    }
  }
  onCountryChange(addressType: 'presentAddress' | 'permanentAddress') {
    const countryId = this.employeeForm.get(`${addressType}.countryId`)?.value;
    if (addressType === 'presentAddress') {
      this.presentDistricts = [];
      this.presentPoliceStations = [];
    } else {
      this.permanentDistricts = [];
      this.permanentPoliceStations = [];
    }
    if (countryId) {
      this.employeeService.getDivisionsByCountry(countryId).subscribe({
        next: (data) => {
          if (addressType === 'presentAddress') {
            this.presentDivisions = data;
          } else {
            this.permanentDivisions = data;
          }
          this.employeeForm.get(`${addressType}.divisionId`)?.reset();
          this.cdr.markForCheck();
        },
      });
    } else {
      if (addressType === 'presentAddress') {
        this.presentDivisions = [];
      } else {
        this.permanentDivisions = [];
      }
    }
  }
  onDivisionChange(addressType: 'presentAddress' | 'permanentAddress') {
    const divisionId = this.employeeForm.get(`${addressType}.divisionId`)?.value;
    if (addressType === 'presentAddress') {
      this.presentPoliceStations = [];
    } else {
      this.permanentPoliceStations = [];
    }
    if (divisionId) {
      this.employeeService.getDistrictsByDivision(divisionId).subscribe({
        next: (data) => {
          if (addressType === 'presentAddress') {
            this.presentDistricts = data;
          } else {
            this.permanentDistricts = data;
          }
          this.employeeForm.get(`${addressType}.districtId`)?.reset();
          this.cdr.markForCheck();
        },
      });
    } else {
      if (addressType === 'presentAddress') {
        this.presentDistricts = [];
      } else {
        this.permanentDistricts = [];
      }
    }
  }
  onDistrictChange(addressType: 'presentAddress' | 'permanentAddress') {
    const districtId = this.employeeForm.get(`${addressType}.districtId`)?.value;
    if (districtId) {
      this.employeeService.getPoliceStationsByDistrict(districtId).subscribe({
        next: (data) => {
          if (addressType === 'presentAddress') {
            this.presentPoliceStations = data;
          } else {
            this.permanentPoliceStations = data;
          }
          this.cdr.markForCheck();
        },
      });
    } else {
      if (addressType === 'presentAddress') {
        this.presentPoliceStations = [];
      } else {
        this.permanentPoliceStations = [];
      }
    }
  }
  saveEmployee() {
    if (this.employeeForm.invalid) {
      this.employeeForm.markAllAsTouched();
      return;
    }
    this.loading = true;
    this.employeeService.createEmployee(this.employeeForm.value, this.selectedFile).subscribe({
      next: () => {
        this.loading = false;
        this.cdr.markForCheck();
        this.router.navigate(['/employee']);
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err.error?.message ?? 'Employee creation failed';
        this.cdr.markForCheck();
      },
    });
  }
  get f() {
    return this.employeeForm.controls;
  }
}
