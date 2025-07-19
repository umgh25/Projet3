import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { AuthService } from '../features/auth/services/auth.service';
import { User } from '../interfaces/user.interface';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  public isLogged = false;
  public user: User | undefined;

  private isLoggedSubject = new BehaviorSubject<boolean>(this.isLogged);

  constructor(private authService: AuthService, private router: Router) {}

  public $isLogged(): Observable<boolean> {
    return this.isLoggedSubject.asObservable();
  }

  public logIn(user: User): void {
    this.user = user;
    this.isLogged = true;
    this.next();
  }

  public logOut(): void {
    const token = localStorage.getItem('token');
    if (token) {
      this.authService.logout().subscribe({
        next: (_res: any) => {
          localStorage.removeItem('token');
          this.user = undefined;
          this.isLogged = false;
          this.next();
          this.router.navigate(['/login']);
        },
        error: (_err: any) => {
          localStorage.removeItem('token');
          this.user = undefined;
          this.isLogged = false;
          this.next();
          this.router.navigate(['/login']);
        }
      });
    } else {
      localStorage.removeItem('token');
      this.user = undefined;
      this.isLogged = false;
      this.next();
      this.router.navigate(['/login']);
    }
  }

  private next(): void {
    this.isLoggedSubject.next(this.isLogged);
  }
}
