import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
@Injectable({
  providedIn: 'root',
})
export class ProfileEventsService {
  private avatarChangedSubject = new Subject<void>();
  avatarChanged$ = this.avatarChangedSubject.asObservable();
  avatarChanged(): void {
    this.avatarChangedSubject.next();
  }
  private profileUpdatedSubject = new Subject<void>();
  profileUpdated$ = this.profileUpdatedSubject.asObservable();
  profileUpdated(): void {
    this.profileUpdatedSubject.next();
  }
}
