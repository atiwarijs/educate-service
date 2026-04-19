import { Component } from '@angular/core';
import { LoaderService } from '../../../shared/common-service/loader-service.service';
import { SharedModule } from '../../../shared/shared-module.module';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-loader',
  imports: [CommonModule],
  standalone: true,
  templateUrl: './loader.component.html',
  styleUrl: './loader.component.css'
})
export class LoaderComponent {
  loading$: any;
  constructor(public loaderService: LoaderService) {
    this.loading$ = this.loaderService.loading$;
  }
}
