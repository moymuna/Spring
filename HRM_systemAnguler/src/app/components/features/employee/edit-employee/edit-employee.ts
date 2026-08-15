import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { EmployeeService } from '../../../../services/employee.service';
import { DepartmentService } from '../../../../services/department.service';
import { DesignationService } from '../../../../services/designation.service';
import { OfficeService } from '../../../../services/office.service';
import { CountryService } from '../../../../services/country.service';
import { DivisionService } from '../../../../services/division.service';
import { DistrictService } from '../../../../services/district.service';
import { PolicestationService } from '../../../../services/policestation.service';
import { EmployeeResponse } from '../../../../models/employee.model';
import { environment } from '../../../../../environments/environment';
import { StorageService } from '../../../../services/storage.service';
@Component({
  selector: 'app-edit-employee',
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './edit-employee.html',
  styleUrl: './edit-employee.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EditEmployee implements OnInit {
  selectedFile: File | null = null;
  imagePreview: string | ArrayBuffer | null = null;
  imageUrl = environment.imgUrl + 'employee/';
  employeeForm!: FormGroup;
  employeeId!: number;
  loading = false;
  errorMessage = '';
  successMessage = '';
  departments: any[] = [];
  designations: any[] = [];
  offices: any[] = [];
  managers: any[] = [];
  countries: any[] = [];
  presentDivisions: any[] = [];
  presentDistricts: any[] = [];
  presentPoliceStations: any[] = [];
  permanentDivisions: any[] = [];
  permanentDistricts: any[] = [];
  permanentPoliceStations: any[] = [];
  employeeStatuses = ['ACTIVE', 'ON_LEAVE', 'SUSPENDED', 'RESIGNED', 'TERMINATED'];
  genders = ['MALE', 'FEMALE', 'OTHER'];
  employmentTypes = ['FULL_TIME', 'PART_TIME', 'CONTRACT', 'INTERN', 'FREELANCE'];
  roles = ['ADMIN', 'HR', 'MANAGER', 'EMPLOYEE'];
  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private employeeService: EmployeeService,
    private departmentService: DepartmentService,
    private designationService: DesignationService,
    private officeService: OfficeService,
    private countryService: CountryService,
    private divisionService: DivisionService,
    private districtService: DistrictService,
    private policeStationService: PolicestationService,
    private cdr: ChangeDetectorRef,
    private storage: StorageService,
  ) {}
  sameAsPresent = false;
  ngOnInit(): void {
    this.employeeId = Number(this.route.snapshot.paramMap.get('id'));
    this.createForm();
    this.loadDropdownData();
    this.loadEmployee();
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
  createForm() {
    this.employeeForm = this.fb.group({
      contractNo: [''],
      employeeCode: ['', Validators.required],
      joiningDate: [''],
      dateOfBirth: [''],
      status: ['ACTIVE'],
      gender: [''],
      bloodGroup: [''],
      employmentType: ['FULL_TIME'],
      bankName: [''],
      bankBranch: [''],
      bankAccountName: [''],
      bankAccountNumber: [''],
      fullName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: [''],
      role: ['EMPLOYEE'],
      departmentId: [''],
      designationId: [''],
      officeId: [''],
      managerId: [''],
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
  loadEmployee() {
    this.employeeService.getEmployeeById(this.employeeId).subscribe({
      next: (employee: EmployeeResponse) => {
        console.log('Employee Data:', employee);
        this.patchEmployee(employee);
        this.cdr.markForCheck();
      },
      error: (err) => {
        this.errorMessage = 'Employee data load failed';
        this.cdr.markForCheck();
      },
    });
  }
  onFileSelected(event: any) {
    const file: File = event.target.files[0];
    if (!file) {
      return;
    }
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
    fileInput.value = '';
    this.imagePreview = null;
  }
  patchEmployee(employee: EmployeeResponse) {
    if (employee.image) {
      this.imagePreview = this.storage.appendToken(this.imageUrl + employee.image);
    }
    this.employeeForm.patchValue({
      contractNo: employee.contractNo,
      employeeCode: employee.employeeCode,
      joiningDate: employee.joiningDate?.substring(0, 10),
      dateOfBirth: employee.dateOfBirth?.substring(0, 10),
      status: employee.status,
      gender: employee.gender,
      bloodGroup: employee.bloodGroup,
      employmentType: employee.employmentType,
      bankName: employee.bankName,
      bankBranch: employee.bankBranch,
      bankAccountName: employee.bankAccountName,
      bankAccountNumber: employee.bankAccountNumber,
      fullName: employee.fullName,
      email: employee.email,
      role: employee.role,
      departmentId: employee.departmentId,
      designationId: employee.designationId,
      officeId: employee.officeId,
      managerId: employee.managerId,
    });
    if (employee.departmentId) {
      this.onDepartmentChange();
    }
    if (employee.presentAddress) {
      this.employeeForm.get('presentAddress')?.patchValue(employee.presentAddress);
    }
    if (employee.permanentAddress) {
      this.employeeForm.get('permanentAddress')?.patchValue(employee.permanentAddress);
    }
    this.loadDependentAddressDropdowns('presentAddress');
    this.loadDependentAddressDropdowns('permanentAddress');
  }
  loadDependentAddressDropdowns(type: 'presentAddress' | 'permanentAddress') {
    const addressGroup = this.employeeForm.get(type);
    if (!addressGroup) {
      return;
    }
    const countryId = addressGroup.get('countryId')?.value;
    const divisionId = addressGroup.get('divisionId')?.value;
    const districtId = addressGroup.get('districtId')?.value;
    if (countryId) {
      this.divisionService.getByCountryId(Number(countryId)).subscribe((data) => {
        if (type === 'presentAddress') {
          this.presentDivisions = data;
        } else {
          this.permanentDivisions = data;
        }
        this.cdr.markForCheck();
      });
    }
    if (divisionId) {
      this.districtService.getByDivisionId(Number(divisionId)).subscribe((data) => {
        if (type === 'presentAddress') {
          this.presentDistricts = data;
        } else {
          this.permanentDistricts = data;
        }
        this.cdr.markForCheck();
      });
    }
    if (districtId) {
      this.policeStationService.getByDistrictId(Number(districtId)).subscribe((data) => {
        if (type === 'presentAddress') {
          this.presentPoliceStations = data;
        } else {
          this.permanentPoliceStations = data;
        }
        this.cdr.markForCheck();
      });
    }
  }
  loadDropdownData() {
    this.departmentService.getAll().subscribe((data) => {
      this.departments = data;
      this.cdr.markForCheck();
    });
    this.officeService.getAllOffice().subscribe((data) => {
      this.offices = data;
      this.cdr.markForCheck();
    });
    this.countryService.getAllCountries().subscribe((data) => {
      this.countries = data;
      this.cdr.markForCheck();
    });
    this.employeeService.getManagers().subscribe((data) => {
      this.managers = data;
      this.cdr.markForCheck();
    });
  }
  onDepartmentChange() {
    let departmentId = this.employeeForm.get('departmentId')?.value;
    if (departmentId) {
      this.designationService.getByDepartment(departmentId).subscribe((data) => {
        this.designations = data;
        this.cdr.markForCheck();
      });
    }
  }
  onCountryChange(type: string) {
    const addressGroup = this.employeeForm.get(type);
    if (!addressGroup) {
      return;
    }
    const countryId = addressGroup.get('countryId')?.value;
    addressGroup.patchValue({
      divisionId: '',
      districtId: '',
      policeStationId: '',
    });
    if (type === 'presentAddress') {
      this.presentDistricts = [];
      this.presentPoliceStations = [];
    } else {
      this.permanentDistricts = [];
      this.permanentPoliceStations = [];
    }
    if (countryId) {
      this.divisionService.getByCountryId(Number(countryId)).subscribe((data) => {
        if (type === 'presentAddress') {
          this.presentDivisions = data;
        } else {
          this.permanentDivisions = data;
        }
        this.cdr.markForCheck();
      });
    } else {
      if (type === 'presentAddress') {
        this.presentDivisions = [];
      } else {
        this.permanentDivisions = [];
      }
    }
  }
  onDivisionChange(type: string) {
    const addressGroup = this.employeeForm.get(type);
    if (!addressGroup) {
      return;
    }
    const divisionId = addressGroup.get('divisionId')?.value;
    addressGroup.patchValue({
      districtId: '',
      policeStationId: '',
    });
    if (type === 'presentAddress') {
      this.presentPoliceStations = [];
    } else {
      this.permanentPoliceStations = [];
    }
    if (divisionId) {
      this.districtService.getByDivisionId(Number(divisionId)).subscribe((data) => {
        if (type === 'presentAddress') {
          this.presentDistricts = data;
        } else {
          this.permanentDistricts = data;
        }
        this.cdr.markForCheck();
      });
    } else {
      if (type === 'presentAddress') {
        this.presentDistricts = [];
      } else {
        this.permanentDistricts = [];
      }
    }
  }
  onDistrictChange(type: string) {
    const addressGroup = this.employeeForm.get(type);
    if (!addressGroup) {
      return;
    }
    const districtId = addressGroup.get('districtId')?.value;
    addressGroup.patchValue({ policeStationId: '' });
    if (districtId) {
      this.policeStationService.getByDistrictId(Number(districtId)).subscribe((data) => {
        if (type === 'presentAddress') {
          this.presentPoliceStations = data;
        } else {
          this.permanentPoliceStations = data;
        }
        this.cdr.markForCheck();
      });
    } else {
      if (type === 'presentAddress') {
        this.presentPoliceStations = [];
      } else {
        this.permanentPoliceStations = [];
      }
    }
  }
  updateEmployee() {
    if (this.employeeForm.invalid) {
      this.employeeForm.markAllAsTouched();
      return;
    }
    this.loading = true;
    this.employeeService
      .updateEmployee(this.employeeId, this.employeeForm.value, this.selectedFile)
      .subscribe({
        next: () => {
          this.loading = false;
          this.successMessage = 'Employee updated successfully';
          this.cdr.markForCheck();
          setTimeout(() => {
            this.router.navigate(['/employee']);
          }, 1000);
        },
        error: () => {
          this.loading = false;
          this.errorMessage = 'Employee update failed';
          this.cdr.markForCheck();
        },
      });
  }
}
