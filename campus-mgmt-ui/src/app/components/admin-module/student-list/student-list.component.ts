import { Component } from '@angular/core';
import { SharedPrimeNgModule } from '../../../shared/shared.primeng-module';

@Component({
  selector: 'app-student-list',
  imports: [SharedPrimeNgModule],
  templateUrl: './student-list.component.html',
  styleUrl: './student-list.component.css',
  standalone: true,
})
export class StudentListComponent {

}
