import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  EventEmitter,
  HostListener,
  Input,
  Output,
} from '@angular/core';
import { ActivatedRoute, Router, RouterOutlet } from '@angular/router';
/**
 * Hosts a list page's child form route as a dialog.
 *
 * Every add/edit route is registered as a child of its list route, so activating
 * one renders the form into this outlet instead of navigating away from the list.
 * The outlet must stay in the DOM for the router to activate it, so the backdrop
 * is toggled with a class rather than *ngIf.
 *
 * Usage:  <app-modal-outlet (closed)="loadThings()"></app-modal-outlet>
 */
@Component({
  selector: 'app-modal-outlet',
  standalone: true,
  imports: [RouterOutlet],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="modal-backdrop-host" [class.open]="open" (click)="onBackdropClick($event)">
      <div class="modal-panel" [class.modal-panel-wide]="wide">
        <button type="button" class="modal-close" (click)="close()" aria-label="Close">
          &times;
        </button>
        <router-outlet (activate)="onActivate()" (deactivate)="onDeactivate()"></router-outlet>
      </div>
    </div>
  `,
})
export class ModalOutlet {
  /** Widens the dialog for form-heavy screens such as Employee and Salary. */
  @Input() wide = false;
  /** Fires when the form closes, so the list can refresh itself. */
  @Output() closed = new EventEmitter<void>();
  open = false;
  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef,
  ) {}
  onActivate(): void {
    this.open = true;
    this.cdr.markForCheck();
  }
  onDeactivate(): void {
    this.open = false;
    this.cdr.markForCheck();
    this.closed.emit();
  }
  /** Navigating back to the parent route deactivates the child and closes the dialog. */
  close(): void {
    this.router.navigate(['.'], { relativeTo: this.route });
  }
  onBackdropClick(event: MouseEvent): void {
    if (event.target === event.currentTarget) {
      this.close();
    }
  }
  @HostListener('document:keydown.escape')
  onEscape(): void {
    if (this.open) {
      this.close();
    }
  }
}
