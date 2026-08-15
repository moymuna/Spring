import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ToastService } from '../../../services/toast.service';
@Component({
  selector: 'app-toast-container',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './toast.html',
  styleUrl: './toast.css',
})
export class ToastContainer {
  constructor(public toastService: ToastService) {}
}
