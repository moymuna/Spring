import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Country } from '../../../../../models/country';
import { Router } from '@angular/router';
import { CountryService } from '../../../../../services/country.service';
import { ToastService } from '../../../../../services/toast.service';
import { ModalOutlet } from '../../../../shared/modal-outlet/modal-outlet';
@Component({
  selector: 'app-country-list',
  imports: [CommonModule, ModalOutlet],
  templateUrl: './country-list.html',
  styleUrl: './country-list.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CountryList implements OnInit {
  countries: Country[] = [];
  constructor(
    private service: CountryService,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private toast: ToastService,
  ) {}
  ngOnInit() {
    this.loadCountries();
  }
  loadCountries() {
    this.service.getAllCountries().subscribe((data) => {
      this.countries = data;
      this.cdr.markForCheck();
      console.log(data);
    });
  }
  edit(id: number) {
    this.router.navigate(['/country/edit', id]);
  }
  delete(id: number) {
    if (confirm('Delete this country?')) {
      this.service.deleteCountry(id).subscribe(() => {
        this.toast.success('Country Deleted Successfully');
        this.loadCountries();
      });
    }
  }
}
