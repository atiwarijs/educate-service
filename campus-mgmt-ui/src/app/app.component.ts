import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { SharedPrimeNgModule } from './shared/shared.primeng-module';
import { LoaderComponent } from './components/common/loader/loader.component';
import { AuthService } from './services/account-service/auth.service';
// import { SharedModule } from './shared/shared-module.module';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, SharedPrimeNgModule, LoaderComponent],
  standalone: true,
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = 'educampus-mgmt-ui';
  visibleSidebar = false;

  showSidebar() {
    this.visibleSidebar = true;
  }

  constructor(private router: Router, private authService: AuthService) {

  }
  ngOnInit(): void {
    this.authService.loadUserInfoFromToken();

    const publicRoutes = [
      '/login/auth',
      '/account/forgot-password',
      '/auth/reset-password',
      '/account/update-password',
    ];

    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        const currentUrl = event.urlAfterRedirects.split('?')[0];

        if (!this.authService.getToken() || this.authService.isTokenExpired()) {
          if (!publicRoutes.some(url => currentUrl.startsWith(url))) {
            this.router.navigate(['/login/auth']);
          }
        } else {
          if (currentUrl === '' || currentUrl === '/' || currentUrl === '/login/auth') {
            this.router.navigate(['/dashboard']);
          }
        }
      }
    });
  }
}
