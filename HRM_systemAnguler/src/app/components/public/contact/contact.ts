import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { StorageService } from '../../../services/storage.service';
import { ContactForm } from '../contact-form/contact-form';
@Component({
  selector: 'app-contact',
  standalone: true,
  imports: [ContactForm],
  templateUrl: './contact.html',
  styleUrl: './contact.css',
})
export class Contact implements OnInit {
  constructor(
    private storage: StorageService,
    private router: Router,
  ) {}
  ngOnInit(): void {
    if (this.storage.isLoggedIn()) {
      this.router.navigate(['/dashboard']);
    }
  }
}
