import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { OfficeService } from '../../../../services/office.service';
import { OfficeRequest } from '../../../../models/office.model';
import { DistrictService } from '../../../../services/district.service';
import { DivisionService } from '../../../../services/division.service';
import { CountryService } from '../../../../services/country.service';
import { PolicestationService } from '../../../../services/policestation.service';
import { ToastService } from '../../../../services/toast.service';
@Component({
  selector: 'app-edit-office',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './edit-office.html',
  styleUrl: './edit-office.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EditOffice implements OnInit {
  officeForm!: FormGroup;
  officeId!: number;
  countries: any[] = [];
  divisions: any[] = [];
  districts: any[] = [];
  policeStations: any[] = [];
  constructor(
    private fb: FormBuilder,
    private countryService: CountryService,
    private divisionService: DivisionService,
    private districtService: DistrictService,
    private policeStationService: PolicestationService,
    private officeService: OfficeService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private toast: ToastService,
  ) {}
  ngOnInit() {
    this.createForm();
    this.officeId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadCountries();
    if (this.officeId) {
      this.loadOffice();
    } else {
      console.error('Invalid Office ID provided in route');
    }
  }
  createForm() {
    this.officeForm = this.fb.group({
      officeName: ['', Validators.required],
      officeCode: ['', Validators.required],
      phone: [''],
      email: ['', Validators.email],
      addressLine1: [''],
      addressLine2: [''],
      postOffice: [''],
      postalCode: [''],
      countryId: [null],
      divisionId: [null],
      districtId: [null],
      policeStationId: [null],
    });
  }
  loadOffice() {
    this.officeService.getOfficeById(this.officeId).subscribe({
      next: (office) => {
        const countryId = office.address?.countryId;
        const divisionId = office.address?.divisionId;
        const districtId = office.address?.districtId;
        this.officeForm.patchValue({
          officeName: office.officeName,
          officeCode: office.officeCode,
          phone: office.phone,
          email: office.email,
          addressLine1: office.address?.addressLine1,
          addressLine2: office.address?.addressLine2,
          postOffice: office.address?.postOffice,
          postalCode: office.address?.postalCode,
          countryId: countryId,
          divisionId: divisionId,
          districtId: districtId,
          policeStationId: office.address?.policeStationId,
        });
        if (countryId) {
          this.loadDivisions(countryId);
          this.cdr.markForCheck();
        }
        if (divisionId) {
          this.loadDistricts(divisionId);
          this.cdr.markForCheck();
        }
        if (districtId) {
          this.loadPoliceStations(districtId);
          this.cdr.markForCheck();
        }
      },
      error: (err) => console.error(err),
    });
  }
  loadCountries() {
    this.countryService.getAllCountries().subscribe({
      next: (res) => {
        this.countries = res;
        this.cdr.markForCheck();
      },
      error: (err) => console.error(err),
    });
  }
  loadDivisions(countryId: number) {
    this.divisionService.getByCountryId(countryId).subscribe({
      next: (res) => {
        this.divisions = res;
        this.cdr.markForCheck();
      },
      error: (err) => console.error(err),
    });
  }
  loadDistricts(divisionId: number) {
    this.districtService.getByDivisionId(divisionId).subscribe({
      next: (res) => {
        this.districts = res;
        this.cdr.markForCheck();
      },
      error: (err) => console.error(err),
    });
  }
  loadPoliceStations(districtId: number) {
    this.policeStationService.getByDistrictId(districtId).subscribe({
      next: (res) => {
        this.policeStations = res;
        this.cdr.markForCheck();
      },
      error: (err) => console.error(err),
    });
  }
  onCountryChange() {
    const countryId = this.officeForm.get('countryId')?.value;
    this.divisions = [];
    this.districts = [];
    this.policeStations = [];
    this.officeForm.patchValue({
      divisionId: null,
      districtId: null,
      policeStationId: null,
    });
    if (countryId) {
      this.loadDivisions(countryId);
    }
  }
  onDivisionChange() {
    const divisionId = this.officeForm.get('divisionId')?.value;
    this.districts = [];
    this.policeStations = [];
    this.officeForm.patchValue({
      districtId: null,
      policeStationId: null,
    });
    if (divisionId) {
      this.loadDistricts(divisionId);
      this.cdr.markForCheck();
    }
  }
  onDistrictChange() {
    const districtId = this.officeForm.get('districtId')?.value;
    this.policeStations = [];
    this.officeForm.patchValue({ policeStationId: null });
    if (districtId) {
      this.loadPoliceStations(districtId);
      this.cdr.markForCheck();
    }
  }
  updateOffice() {
    if (this.officeForm.invalid) return;
    const value = this.officeForm.value;
    const officeData: OfficeRequest = {
      officeName: value.officeName,
      officeCode: value.officeCode,
      phone: value.phone,
      email: value.email,
      address: {
        addressLine1: value.addressLine1,
        addressLine2: value.addressLine2,
        postOffice: value.postOffice,
        postalCode: value.postalCode,
        countryId: value.countryId,
        divisionId: value.divisionId,
        districtId: value.districtId,
        policeStationId: value.policeStationId,
      },
    };
    this.officeService.updateOffice(this.officeId, officeData).subscribe({
      next: () => {
        this.toast.success('Office updated successfully');
        this.router.navigate(['/office']);
      },
      error: (err) => console.error(err),
    });
  }
}
