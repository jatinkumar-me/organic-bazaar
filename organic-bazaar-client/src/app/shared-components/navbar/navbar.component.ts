import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../auth/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit {
  isLoggedIn: boolean = false;
  username: string | undefined;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
      this.authService.currentUser.subscribe(user => {
      this.isLoggedIn = user != null;
      this.username = user?.name;
    })
  }
}
