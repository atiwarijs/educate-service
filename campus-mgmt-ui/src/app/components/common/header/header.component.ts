import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../services/account-service/auth.service';

interface ProfileMenuItem {
  label: string;
  icon: string;
  action: string;
  separator?: boolean;
}

@Component({
  selector: 'app-header',
  imports: [CommonModule, RouterModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
  standalone: true,
})
export class HeaderComponent implements OnInit {
  @Output() toggleSidebar = new EventEmitter<boolean>();

  userInfo: any = null;
  isProfileMenuOpen = false;
  isNotificationsOpen = false;
  notifications: any[] = [];
  unreadNotificationCount = 0;

  profileMenuItems: ProfileMenuItem[] = [
    { label: 'Profile', icon: 'pi pi-user', action: 'profile' },
    // { label: 'Settings', icon: 'pi pi-cog', action: 'settings' },
    { label: 'Help & Support', icon: 'pi pi-question-circle', action: 'help' },
    { label: 'Logout', icon: 'pi pi-sign-out', action: 'logout', separator: true }
  ];

  constructor(
    private router: Router,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.loadUserInfo();
    this.loadNotifications();
  }

  toggleSidebarMenu(): void {
    this.toggleSidebar.emit();
  }

  loadUserInfo(): void {
    // Mock user data - replace with actual user service call
    this.authService.userInfo$.subscribe((user) => {
      this.userInfo = user;
    });
  }

  loadNotifications(): void {
    // Mock notifications - replace with actual notification service call
    this.notifications = [
      {
        id: 1,
        title: 'New Student Registration',
        message: 'A new student has registered for admission',
        time: '5 minutes ago',
        read: false,
        type: 'info'
      },
      {
        id: 2,
        title: 'Fee Payment Reminder',
        message: 'Monthly fee payment is due tomorrow',
        time: '1 hour ago',
        read: false,
        type: 'warning'
      },
      {
        id: 3,
        title: 'Parent Meeting',
        message: 'Parent-teacher meeting scheduled for today',
        time: '2 hours ago',
        read: true,
        type: 'info'
      }
    ];

    this.unreadNotificationCount = this.notifications.filter(n => !n.read).length;
  }

  toggleProfileMenu(): void {
    this.isProfileMenuOpen = !this.isProfileMenuOpen;
    this.isNotificationsOpen = false; // Close notifications if open
  }

  toggleNotifications(): void {
    this.isNotificationsOpen = !this.isNotificationsOpen;
    this.isProfileMenuOpen = false; // Close profile menu if open
  }

  closeMenus(): void {
    this.isProfileMenuOpen = false;
    this.isNotificationsOpen = false;
  }

  onProfileMenuAction(action: string): void {
    switch (action) {
      case 'profile':
        this.router.navigate(['/settings/account']);
        break;
      case 'settings':
        this.router.navigate(['/settings']);
        break;
      case 'help':
        this.router.navigate(['/help']);
        break;
      case 'logout':
        this.authService.logout();
        break;
    }
    this.closeMenus();
  }

  markNotificationAsRead(notification: any): void {
    notification.read = true;
    this.unreadNotificationCount = this.notifications.filter(n => !n.read).length;
  }

  markAllNotificationsAsRead(): void {
    this.notifications.forEach(n => n.read = true);
    this.unreadNotificationCount = 0;
  }

  navigateToSearch(): void {
    this.router.navigate(['/search']);
  }

  navigateToSettings(): void {
    this.router.navigate(['/settings']);
  }

  getNotificationIcon(type: string): string {
    switch (type) {
      case 'warning':
        return 'pi pi-exclamation-triangle';
      case 'success':
        return 'pi pi-check-circle';
      case 'error':
        return 'pi pi-times-circle';
      default:
        return 'pi pi-info-circle';
    }
  }

  getNotificationClass(type: string): string {
    return `notification-${type}`;
  }
}