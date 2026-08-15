import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Country } from '../../../../../models/country';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CountryService } from '../../../../../services/country.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastService } from '../../../../../services/toast.service';
@Component({
  selector: 'app-country-edit',
  imports: [CommonModule, FormsModule],
  templateUrl: './country-edit.html',
  styleUrl: './country-edit.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CountryEdit implements OnInit {
  country: Country = {
    countryName: '',
    code: '',
    phoneCode: '',
  };
  id!: number;
  constructor(
    private service: CountryService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private toast: ToastService,
  ) {}
  ngOnInit() {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadCountry();
  }
  loadCountry() {
    this.service.getCountryById(this.id).subscribe((data) => {
      this.country = data;
      this.cdr.markForCheck();
    });
  }
  update() {
    this.service.updateCountry(this.id, this.country).subscribe(() => {
      this.toast.success('Country Updated Successfully');
      this.router.navigate(['/country']);
    });
  }
  reset() {
    this.loadCountry();
  }
}
