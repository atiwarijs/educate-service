import { Component, Input, OnInit } from '@angular/core';
import { NavigationEnd, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { filter } from 'rxjs';
import { AuthService } from '../../../services/account-service/auth.service';
import { FormSubmissionService } from '../../../services/form-submission.service';
import { AlertService } from '../../../shared/common-service/alert-notify.service';

interface MenuItem {
  label: string;
  icon: string;
  route?: string;
  children?: MenuItem[];
  isExpanded?: boolean;
  isActive?: boolean;
  command?: () => void;
}

@Component({
  selector: 'app-sidebar',
  imports: [CommonModule, RouterModule],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css'],
  standalone: true,
})
export class SidebarComponent implements OnInit {
  @Input() visible: boolean = true;

  menuItems: MenuItem[] = [];
  activeRoute: string = '';

  constructor(
    private router: Router,
    private notifyService: AlertService,
    private authService: AuthService,
    private roleSharingService: FormSubmissionService
  ) { }

  ngOnInit(): void {
    this.buildMenu();
    this.router.events
      .pipe(filter((e) => e instanceof NavigationEnd))
      .subscribe(() => {
        this.buildMenu();
        this.updateActiveStates();
      });
  }

  buildMenu() {
    const currentUrl = this.router.url;

    this.menuItems = [
      {
        label: 'Admin Dashboard',
        icon: 'pi pi-shield',
        isExpanded: currentUrl.startsWith('/admin'),
        children: [
          {
            label: 'Dashboard',
            icon: 'pi pi-chart-line',
            route: '/admin/dashboard'
          },
          {
            label: 'Class/Section',
            icon: 'pi pi-users',
            route: '/admin/class-section'
          },
          {
            label: 'Teachers',
            icon: 'pi pi-users',
            route: '/admin/teachers'
          },
          {
            label: 'Students',
            icon: 'pi pi-user',
            route: '/admin/students'
          }
        ]
      },
      {
        label: 'Teachers',
        icon: 'pi pi-graduation-cap',
        isExpanded: currentUrl.startsWith('/teacher'),
        children: [
          {
            label: 'Dashboard',
            icon: 'pi pi-home',
            route: '/teacher/dashboard'
          },
          {
            label: 'Registration',
            icon: 'pi pi-user-plus',
            route: '/teacher/registration'
          },
          {
            label: 'Homework',
            icon: 'pi pi-book',
            route: '/teacher/homework'
          },
          {
            label: 'Assignments',
            icon: 'pi pi-file-edit',
            route: '/teacher/assignments'
          },
          {
            label: 'Attendance',
            icon: 'pi pi-calendar-times',
            route: '/teacher/attendance'
          }
        ]
      },
      {
        label: 'Dashboard',
        icon: 'pi pi-th-large',
        isExpanded: currentUrl.startsWith('/dashboard'),
        children: [
          {
            label: 'Active Users',
            icon: 'pi pi-users',
            route: '/dashboard'
          },
          {
            label: 'Enrollment',
            icon: 'pi pi-id-card',
            children: [
              {
                label: 'Student',
                icon: 'pi pi-user',
                route: '/dashboard/profile/student'
              },
              {
                label: 'Academic',
                icon: 'pi pi-book',
                route: '/dashboard/profile/academic'
              },
              {
                label: 'Administrative',
                icon: 'pi pi-briefcase',
                route: '/dashboard/profile/administrative'
              },
              {
                label: 'Support',
                icon: 'pi pi-headphones',
                route: '/dashboard/profile/support'
              }
            ]
          },
          {
            label: 'Manage Users',
            icon: 'pi pi-user-edit',
            route: '/dashboard/profile-saved'
          },
          {
            label: 'Account',
            icon: 'pi pi-cog',
            route: '/dashboard/new-users'
          }
        ]
      },
      {
        label: 'Students',
        icon: 'pi pi-users',
        isExpanded: currentUrl.startsWith('/student'),
        children: [
          {
            label: 'Dashboard',
            icon: 'pi pi-home',
            route: '/student/dashboard'
          },
          {
            label: 'Registration',
            icon: 'pi pi-user-plus',
            route: '/student/registration'
          },
          {
            label: 'Attendance',
            icon: 'pi pi-calendar-times',
            route: '/student/attendance'
          },
          {
            label: 'Homework',
            icon: 'pi pi-book',
            route: '/student/homework'
          },
          {
            label: 'Subjects',
            icon: 'pi pi-list',
            route: '/student/subjects'
          },
          {
            label: 'Exams Schedule',
            icon: 'pi pi-calendar',
            route: '/student/exams-schedule'
          }
        ]
      },
      {
        label: 'Exams',
        icon: 'pi pi-file-o',
        isExpanded: currentUrl.startsWith('/exam'),
        children: [
          {
            label: 'Dashboard',
            icon: 'pi pi-home',
            route: '/exam/dashboard'
          },
          {
            label: 'Grades',
            icon: 'pi pi-star',
            route: '/exam/grades'
          },
          {
            label: 'Schedule',
            icon: 'pi pi-calendar',
            route: '/exam/schedule'
          },
          {
            label: 'Results',
            icon: 'pi pi-chart-bar',
            route: '/exam/results'
          }
        ]
      },
      {
        label: 'Finance',
        icon: 'pi pi-dollar',
        isExpanded: currentUrl.startsWith('/finance'),
        children: [
          {
            label: 'Events Expenses',
            icon: 'pi pi-calendar-plus',
            route: '/finance/events-expenses'
          },
          {
            label: 'Exam Expenses',
            icon: 'pi pi-money-bill',
            route: '/finance/exam-expenses'
          },
          {
            label: 'Fees Management',
            icon: 'pi pi-credit-card',
            route: '/finance/fees-management'
          },
          {
            label: 'School Expenses',
            icon: 'pi pi-building',
            route: '/finance/school-expenses'
          },
          {
            label: 'Transport Expenses',
            icon: 'pi pi-car',
            route: '/finance/transport-expenses'
          }
        ]
      },
      {
        label: 'Notices',
        icon: 'pi pi-bell',
        isExpanded: currentUrl.startsWith('/notices'),
        children: [
          {
            label: 'All Notices',
            icon: 'pi pi-list',
            route: '/notices/all-notices'
          },
          {
            label: 'Academic',
            icon: 'pi pi-book',
            route: '/notices/academic'
          },
          {
            label: 'Announcements',
            icon: 'pi pi-megaphone',
            route: '/notices/announcements'
          },
          {
            label: 'Holidays',
            icon: 'pi pi-calendar-minus',
            route: '/notices/holidays'
          },
          {
            label: 'Events',
            icon: 'pi pi-calendar-plus',
            route: '/notices/events'
          }
        ]
      },
      {
        label: 'Settings',
        icon: 'pi pi-cog',
        isExpanded: currentUrl.startsWith('/settings'),
        children: [
          {
            label: 'Preferences',
            icon: 'pi pi-sliders-h',
            route: '/settings/preferences'
          },
          {
            label: 'Account',
            icon: 'pi pi-user',
            route: '/settings/account'
          },
          {
            label: 'Logout',
            icon: 'pi pi-sign-out',
            command: () => this.authService.logout()
          }
        ]
      }
    ];

    this.updateActiveStates();
  }

  toggleMenuItem(item: MenuItem) {
    if (item.children) {
      item.isExpanded = !item.isExpanded;
      // Close other expanded items (accordion behavior)
      this.menuItems.forEach(menuItem => {
        if (menuItem !== item && menuItem.children) {
          menuItem.isExpanded = false;
        }
      });
    }
  }

  navigateToRoute(item: MenuItem) {
    if (item.command) {
      item.command();
    } else if (item.route) {
      this.router.navigate([item.route]);
    }
  }

  isRouteActive(route: string): boolean {
    const currentUrl = this.router.url;
    return currentUrl === route ||
      (route !== '/' && currentUrl.startsWith(route + '?')) ||
      (route !== '/' && currentUrl.startsWith(route + '/'));
  }

  private updateActiveStates() {
    const updateItemStates = (items: MenuItem[]) => {
      items.forEach(item => {
        if (item.route) {
          item.isActive = this.isRouteActive(item.route);
        }
        if (item.children) {
          updateItemStates(item.children);
          // If any child is active, expand parent
          const hasActiveChild = item.children.some(child =>
            child.isActive || (child.children && child.children.some(grandchild => grandchild.isActive))
          );
          if (hasActiveChild) {
            item.isExpanded = true;
          }
        }
      });
    };
    updateItemStates(this.menuItems);
  }
}