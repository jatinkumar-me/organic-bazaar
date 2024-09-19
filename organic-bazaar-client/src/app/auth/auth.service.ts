import { Injectable } from '@angular/core';
import { environment } from '../environment';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { LoginRequest, LoginResponse, RegisterRequest, User } from './auth.model';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = `${environment.apiUrl}/users`;
  private tokenKey = '';
  private currentUserSubject: BehaviorSubject<User | null>;
  public currentUser: Observable<User | null>;

  constructor(private http: HttpClient, private router: Router) {
    this.currentUserSubject = new BehaviorSubject<User | null>(null);
    this.currentUser = this.currentUserSubject.asObservable();
  }

  login(loginRequest: LoginRequest): Observable<LoginResponse> {
    const loginUrl = `${this.apiUrl}/login`;

    return this.http.post<LoginResponse>(loginUrl, loginRequest).pipe(
      tap((response: LoginResponse) => {
        if (response && response.token) {
          this.storeToken(response.token);
          this.currentUserSubject.next(response.user);
        }
      })
    );
  }

  logout(): void {
    this.removeToken();
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  register(registerRequest: RegisterRequest): Observable<User> {
    const registerUrl = `${this.apiUrl}/register`;
    return this.http.post<User>(registerUrl, registerRequest);
  }

  private storeToken(token: string): void {
    localStorage.setItem(this.tokenKey, token);
  }

  private getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  private removeToken(): void {
    localStorage.removeItem(this.tokenKey);
  }

  isLoggedIn(): boolean {
    return this.getToken() != null;
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }
}
