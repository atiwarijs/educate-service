import { Component } from '@angular/core';
import { share } from 'rxjs';
import { SharedPrimeNgModule } from '../../../shared/shared.primeng-module';

@Component({
  selector: 'app-teacher-list',
  imports: [SharedPrimeNgModule],
  templateUrl: './teacher-list.component.html',
  styleUrl: './teacher-list.component.css',
  standalone: true,
})
export class TeacherListComponent {

}
