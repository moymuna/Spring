import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  inject,
  OnInit,
} from '@angular/core';
import { UserService } from '../../../../services/user.service';
import { Router, RouterModule } from '@angular/router';
import { User } from '../../../../models/user.model';
import { CommonModule } from '@angular/common';
import { ModalOutlet } from '../../../shared/modal-outlet/modal-outlet';
@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule, RouterModule, ModalOutlet],
  templateUrl: './user-list.html',
  styleUrl: './user-list.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class UserList implements OnInit {
  private userService = inject(UserService);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);
  users: User[] = [];
  loading = false;
  ngOnInit(): void {
    this.loadUsers();
  }
  loadUsers() {
    this.loading = true;
    this.userService.getAllUsers().subscribe({
      next: (response) => {
        this.users = response;
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error(err);
        this.loading = false;
        this.cdr.markForCheck();
      },
    });
  }
  addUser() {
    this.router.navigate(['/user/add']);
  }
  editUser(id: number) {
    this.router.navigate(['/user/edit', id]);
  }
  details(id: number) {
    this.router.navigate(['/user/edit', id]);
  }
  deleteUser(id: number) {
    if (!confirm('Delete this user?')) {
      return;
    }
    this.userService.deleteUser(id).subscribe({
      next: () => {
        this.loadUsers();
      },
      error: console.error,
    });
  }
}
