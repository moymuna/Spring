import { ChangeDetectionStrategy, ChangeDetectorRef, Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, Validators } from '@angular/forms';
import { CountryService } from '../../../../../services/country.service';
import { Country } from '../../../../../models/country';
import { CommonModule } from '@angular/common';
import { ToastService } from '../../../../../services/toast.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-country-add',
  imports: [CommonModule, FormsModule],
  templateUrl: './country-add.html',
  styleUrl: './country-add.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CountryAdd {
  country: Country = {
    countryName: '',
    code: '',
    phoneCode: '',
  };
  constructor(
    private service: CountryService,
    private cdr: ChangeDetectorRef,
    private toast: ToastService,
    private router: Router,
  ) {}
  save() {
    this.service.saveCountry(this.country).subscribe({
      next: () => {
        this.toast.success('Country Saved Successfully');
        this.reset();
        this.cdr.markForCheck();
        setTimeout(() => {
          this.router.navigate(['/country']);
        }, 800);
      },
      error: (err) => {
        console.error(err);
        this.toast.error('Country Save Failed.');
      },
    });
  }
  reset() {
    this.country = {
      countryName: '',
      code: '',
      phoneCode: '',
    };
  }
}
