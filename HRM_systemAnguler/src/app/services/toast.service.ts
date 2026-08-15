import { Injectable, signal } from '@angular/core';
export type ToastType = 'success' | 'error' | 'info' | 'warning';
export interface Toast {
  id: number;
  message: string;
  type: ToastType;
}
@Injectable({
  providedIn: 'root',
})
export class ToastService {
  private nextId = 1;
  readonly toasts = signal<Toast[]>([]);
  show(message: string, type: ToastType = 'info', durationMs = 4000): void {
    const toast: Toast = { id: this.nextId++, message, type };
    this.toasts.update((list) => [...list, toast]);
    setTimeout(() => this.dismiss(toast.id), durationMs);
  }
  success(message: string): void {
    this.show(message, 'success');
  }
  error(message: string): void {
    this.show(message, 'error');
  }
  info(message: string): void {
    this.show(message, 'info');
  }
  warning(message: string): void {
    this.show(message, 'warning');
  }
  dismiss(id: number): void {
    this.toasts.update((list) => list.filter((t) => t.id !== id));
  }
}
