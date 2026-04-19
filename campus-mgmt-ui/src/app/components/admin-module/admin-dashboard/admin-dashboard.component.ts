import { Component } from '@angular/core';
import { SharedPrimeNgModule } from '../../../shared/shared.primeng-module';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-dashboard',
  imports: [SharedPrimeNgModule],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css',
  standalone: true,
})
export class AdminDashboardComponent {
  
  constructor(private router: Router) { }

  goToStudentRegistration() {
    this.router.navigate(['/student/registration']);
  }

  goToTeacherRegistration() {
    this.router.navigate(['/teacher/registration']);
  }
}
