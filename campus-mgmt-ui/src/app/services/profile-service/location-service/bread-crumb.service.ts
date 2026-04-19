import { Injectable } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { BehaviorSubject, filter } from 'rxjs';

export interface Breadcrumb {
  label: string;
  routerLink: string;
  params?: any;
}

@Injectable({ providedIn: 'root' })
export class BreadcrumbService {
  private readonly breadcrumbs$ = new BehaviorSubject<Breadcrumb[]>([]);

  constructor(private router: Router, private activatedRoute: ActivatedRoute) {
    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe(() => {
        const breadcrumbs = this.buildBreadcrumbs();
        this.breadcrumbs$.next(breadcrumbs);
      });
  }

  get breadcrumbs() {
    return this.breadcrumbs$.asObservable();
  }

  private buildBreadcrumbs(): Breadcrumb[] {
    const breadcrumbs: Breadcrumb[] = [];
    const url = this.router.url;

    // Dashboard
    if (url.includes('/dashboard')) {
      breadcrumbs.push({ label: 'Dashboard', routerLink: '/dashboard' });

      if (url.includes('/dashboard/profile/')) {
        const type = url.split('/').pop();
        breadcrumbs.push({ label: `Profile (${type})`, routerLink: url });
      } else if (url.includes('/dashboard/profile-layout')) {
        breadcrumbs.push({ label: 'Enrollment', routerLink: '/dashboard/profile-layout' });
      } else if (url.includes('/dashboard/profile-saved')) {
        breadcrumbs.push({ label: 'User Details', routerLink: '/dashboard/profile-saved' });
      } else if (url.includes('/dashboard/new-users')) {
        breadcrumbs.push({ label: 'Account', routerLink: '/dashboard/new-users' });
      }
    }

    // Settings
    if (url.includes('/settings')) {
      breadcrumbs.push({ label: 'Settings', routerLink: '/settings/account' });
      if (url.includes('/settings/account')) {
        breadcrumbs.push({ label: 'Account Details', routerLink: '/settings/account' });
      }
    }

    // Student / Teacher via routeConfig
    for (const key of Object.keys(this.routeConfig)) {
      const cfg = this.routeConfig[key];
      if (url.includes(`/${key}`)) {
        breadcrumbs.push({ label: cfg.baseLabel, routerLink: cfg.basePath });
        for (const route of cfg.routes) {
          if (url.includes(route.path)) {
            breadcrumbs.push({ label: route.label, routerLink: route.path });
            break;
          }
        }
      }
    }

    return breadcrumbs;
  }

  private routeConfig: Record<string, { baseLabel: string; basePath: string; routes: { path: string; label: string }[] }> = {
    student: {
      baseLabel: 'Student Dashboard',
      basePath: '/student/dashboard',
      routes: [
        { path: '/student/attendance', label: 'Attendance' },
        { path: '/student/subjects', label: 'Subjects' },
        { path: '/student/homework', label: 'Homework' },
        { path: '/student/exams-schedule', label: 'Exams Schedule' },
        { path: '/student/registration', label: 'Student Registration' },
      ],
    },
    teacher: {
      baseLabel: 'Teacher Dashboard',
      basePath: '/teacher/dashboard',
      routes: [
        { path: '/teacher/homework', label: 'Homework' },
        { path: '/teacher/assignments', label: 'Assignments' },
        { path: '/teacher/registration', label: 'Registration' },
        { path: '/teacher/attendance', label: 'Attendance' },
      ],
    },
  };
}