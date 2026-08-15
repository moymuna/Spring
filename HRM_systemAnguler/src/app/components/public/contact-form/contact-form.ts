import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { ToastService } from '../../../services/toast.service';
@Component({
  selector: 'app-contact-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './contact-form.html',
  styleUrl: './contact-form.css',
})
export class ContactForm {
  dto = {
    name: '',
    email: '',
    subject: '',
    message: '',
  };
  submitting = false;
  constructor(private toast: ToastService) {}
  submit(form: NgForm): void {
    if (form.invalid) {
      Object.values(form.controls).forEach((c) => c.markAsTouched());
      return;
    }
    this.submitting = true;
    setTimeout(() => {
      this.submitting = false;
      this.toast.success(
        `Thanks ${this.dto.name.split(' ')[0]}, your message has been received. We'll get back to you soon.`,
      );
      this.dto = { name: '', email: '', subject: '', message: '' };
      form.resetForm();
    }, 600);
  }
}
