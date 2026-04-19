import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, NavigationEnd, RouterModule } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { filter } from 'rxjs';
import { SharedPrimeNgModule } from '../../../shared/shared.primeng-module';
import { HeaderComponent } from '../header/header.component';
import { SidebarComponent } from '../sidebar/sidebar.component';

// Import your components

@Component({
  selector: 'app-main-component',
  imports: [
    CommonModule,
    RouterModule,
    SharedPrimeNgModule,
    HeaderComponent,
    SidebarComponent
  ],
  templateUrl: './main-component.component.html',
  styleUrls: ['./main-component.component.css'],
  standalone: true,
})
export class MainComponent implements OnInit {
  sidebarVisible: boolean = true;
  breadcrumbItems: MenuItem[] = [];
  homeItem: MenuItem = { icon: 'pi pi-home', routerLink: '/admin/dashboard' };

  constructor(private router: Router) { }

  ngOnInit(): void {
    this.updateBreadcrumb();

    // Update breadcrumb on route changes
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => {
        this.updateBreadcrumb();
      });
  }

  onSidebarToggle(visible?: boolean): void {
    if (typeof visible === 'boolean') {
      this.sidebarVisible = visible;
    } else {
      this.sidebarVisible = !this.sidebarVisible;
    }
    console.log('Sidebar toggle:', this.sidebarVisible); // Debug log
  }

  private updateBreadcrumb(): void {
    const url = this.router.url;
    const segments = url.split('/').filter(segment => segment);

    this.breadcrumbItems = [];

    let currentPath = '';
    segments.forEach((segment, index) => {
      currentPath += '/' + segment;
      console.log('Processing segment:', segment, 'Current path:', currentPath); // Debug log
      // Skip query parameters
      const cleanSegment = segment.split('?')[0];

      this.breadcrumbItems.push({
        label: this.formatBreadcrumbLabel(cleanSegment),
        routerLink: currentPath,
        disabled: index === segments.length - 1 // Disable last item
      });
    });
  }

  private formatBreadcrumbLabel(segment: string): string {
    return segment
      .split('-')
      .map(word => word.charAt(0).toUpperCase() + word.slice(1))
      .join(' ');
  }
}