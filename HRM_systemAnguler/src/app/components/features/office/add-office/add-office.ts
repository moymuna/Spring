import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { OfficeService } from '../../../../services/office.service';
import { Router } from '@angular/router';
import { CountryService } from '../../../../services/country.service';
import { DivisionService } from '../../../../services/division.service';
import { DistrictService } from '../../../../services/district.service';
import { PolicestationService } from '../../../../services/policestation.service';
import { OfficeRequest } from '../../../../models/office.model';
@Component({
  selector: 'app-add-office',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-office.html',
  styleUrl: './add-office.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AddOffice implements OnInit {
  office: OfficeRequest = {
    officeName: '',
    officeCode: '',
    phone: '',
    email: '',
    address: {
      addressLine1: '',
      addressLine2: '',
      postOffice: '',
      postalCode: '',
      countryId: 0,
      divisionId: 0,
      districtId: 0,
      policeStationId: 0,
    },
  };
  countries: any[] = [];
  divisions: any[] = [];
  districts: any[] = [];
  policeStations: any[] = [];
  constructor(
    private officeService: OfficeService,
    private countryService: CountryService,
    private divisionService: DivisionService,
    private districtService: DistrictService,
    private policeStationService: PolicestationService,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    this.loadCountries();
  }
  loadCountries() {
    this.countryService.getAllCountries().subscribe({
      next: (res) => {
        this.countries = res;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.log(err);
      },
    });
  }
  onCountryChange() {
    const countryId = this.office.address.countryId;
    console.log(countryId);
    this.divisions = [];
    this.districts = [];
    this.policeStations = [];
    this.office.address.divisionId = 0;
    this.office.address.districtId = 0;
    this.office.address.policeStationId = 0;
    if (countryId) {
      this.divisionService.getByCountryId(countryId).subscribe({
        next: (res) => {
          this.divisions = res;
          this.cdr.markForCheck();
        },
      });
    }
  }
  onDivisionChange() {
    const divisionId = this.office.address.divisionId;
    this.districts = [];
    this.policeStations = [];
    this.office.address.districtId = 0;
    this.office.address.policeStationId = 0;
    if (divisionId) {
      this.districtService.getByDivisionId(divisionId).subscribe({
        next: (res) => {
          this.districts = res;
          this.cdr.markForCheck();
        },
      });
    }
  }
  onDistrictChange() {
    const districtId = this.office.address.districtId;
    this.policeStations = [];
    this.office.address.policeStationId = 0;
    if (districtId) {
      this.policeStationService.getByDistrictId(districtId).subscribe({
        next: (res) => {
          this.policeStations = res;
          this.cdr.markForCheck();
        },
      });
    }
  }
  save() {
    this.officeService.createOffice(this.office).subscribe({
      next: (res) => {
        console.log('Office Created', res);
        this.router.navigate(['/office']);
      },
      error: (err) => {
        console.log(err);
      },
    });
  }
}
