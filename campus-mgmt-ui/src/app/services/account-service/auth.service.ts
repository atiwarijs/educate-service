import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, tap, switchMap } from 'rxjs/operators';
import { BehaviorSubject, Observable, throwError, of } from 'rxjs';
import { jwtDecode } from 'jwt-decode';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';
import { API_ENDPOINTS } from '../../core/constants/api.constants';

// Enhanced token interface
interface DecodedToken {
  preferred_username?: string;
  email?: string;
  name?: string;
  exp?: number;
  sub?: string;
  given_name?: string;
  family_name?: string;
  realm_access?: {
    roles: string[];
  };
  resource_access?: {
    [clientId: string]: {
      roles: string[];
    }
  };
  [key: string]: any;
}

// User model for strongly typed user information
export interface UserInfo {
  id: string;
  username: string;
  email: string;
  fullName: string;
  firstName: string;
  lastName: string;
  roles: string[];
  isAuthenticated: boolean;
  tokenExpiration: Date | null;
  // Original token data for backward compatibility
  tokenData: DecodedToken;
  // Additional user details from API
  additionalDetails?: any;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = environment.securityServiceUrl;
  private readonly ACCESS_TOKEN_KEY = 'access_token';
  private readonly REFRESH_TOKEN_KEY = 'refresh_token';
  private readonly TOKEN_EXPIRY_KEY = 'token_expiry';
  private readonly USER_DETAILS_KEY = 'user_details';
  private readonly clientId = 'security-service';

  // Keep original DecodedToken subject for backward compatibility
  private userInfoSubject = new BehaviorSubject<DecodedToken | null>(null);
  userInfo$ = this.userInfoSubject.asObservable();

  // Add new user info subject with enhanced model
  private enhancedUserInfoSubject = new BehaviorSubject<UserInfo | null>(null);
  enhancedUserInfo$ = this.enhancedUserInfoSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {
    // this.loadUserInfoFromToken();
  }

  login(username: string, password: string): Observable<any> {
    return this.http
      .post<any>(`${this.apiUrl}` + API_ENDPOINTS.AUTH.LOGIN, { username, password })
      .pipe(
        tap((response) => {
          if (response.access_token) {
            const accessToken = response.access_token;
            const refreshToken = response.refresh_token;

            sessionStorage.setItem(this.ACCESS_TOKEN_KEY, accessToken);
            sessionStorage.setItem(this.REFRESH_TOKEN_KEY, refreshToken);

            // Calculate and store token expiry time if available
            if (response.expires_in) {
              const expiryTime = new Date().getTime() + (response.expires_in * 1000);
              sessionStorage.setItem(this.TOKEN_EXPIRY_KEY, expiryTime.toString());
            }

            // Load user info from token
            this.loadUserInfoFromToken();
          }
        }),
        switchMap((response) => {
          // After successful login and token processing, fetch additional user details
          const decodedToken = this.userInfoSubject.value;
          if (decodedToken?.preferred_username && decodedToken?.email) {
            return this.getAccountDetails(decodedToken.preferred_username, decodedToken.email).pipe(
              tap(userDetails => {
                // Store user details and update the enhanced user info
                this.storeUserDetails(userDetails);
                this.updateEnhancedUserWithDetails(userDetails);
              }),
              // Return original login response
              switchMap(() => of(response))
            );
          }
          return of(response);
        })
      );
  }

  /**
   * Enhanced method to extract user information from JWT
   * Updates both the original token subject and the new enhanced subject
   */
  loadUserInfoFromToken(): UserInfo | null {
    const token = sessionStorage.getItem(this.ACCESS_TOKEN_KEY);
    if (!token) {
      this.userInfoSubject.next(null);
      this.enhancedUserInfoSubject.next(null);
      return null;
    }

    try {
      // Decode the token
      const decoded: DecodedToken = jwtDecode(token);

      // Update original DecodedToken subject (for backward compatibility)
      this.userInfoSubject.next(decoded);

      // Build enhanced user info object
      const expirationTime = decoded.exp ? new Date(decoded.exp * 1000) : null;

      const userInfo: UserInfo = {
        id: decoded.sub || '',
        username: decoded.preferred_username || '',
        email: decoded.email || '',
        fullName: decoded.name || '',
        firstName: decoded.given_name || '',
        lastName: decoded.family_name || '',
        roles: this.extractRoles(decoded),
        isAuthenticated: true,
        tokenExpiration: expirationTime,
        tokenData: decoded // Keep original token data for backward compatibility
      };

      // Try to retrieve any previously stored user details
      const storedDetails = this.getSavedUserDetails();
      if (storedDetails) {
        userInfo.additionalDetails = storedDetails;
      } else if (decoded.preferred_username && decoded.email) {
        // If no stored details but we have username and email, fetch them
        this.getAccountDetails(decoded.preferred_username, decoded.email).subscribe({
          next: (details) => {
            this.storeUserDetails(details);
            this.updateEnhancedUserWithDetails(details);
          },
          error: (err) => console.error('Failed to fetch user details', err)
        });
      }

      // Update the enhanced user info subject
      this.enhancedUserInfoSubject.next(userInfo);
      return userInfo;
    } catch (e) {
      console.error('Invalid token', e);
      this.userInfoSubject.next(null);
      this.enhancedUserInfoSubject.next(null);
      return null;
    }
  }

  /**
   * Helper method to extract roles from token claims
   */
  private extractRoles(decoded: DecodedToken): string[] {
    const roles: string[] = [];

    // Extract realm roles
    if (decoded.realm_access && Array.isArray(decoded.realm_access.roles)) {
      roles.push(...decoded.realm_access.roles);
    }

    // Extract client roles if client ID is provided
    if (this.clientId && decoded.resource_access &&
      decoded.resource_access[this.clientId] &&
      Array.isArray(decoded.resource_access[this.clientId].roles)) {
      roles.push(...decoded.resource_access[this.clientId].roles);
    }

    return roles;
  }

  /**
   * Check if token is expired
   */
  isTokenExpired(): boolean {
    // First check against stored expiry time
    const expiry = sessionStorage.getItem(this.TOKEN_EXPIRY_KEY);
    if (expiry && new Date(parseInt(expiry, 10)) < new Date()) {
      return true;
    }

    // Fall back to token's exp claim if available
    const token = sessionStorage.getItem('access_token');
    if (token) {
      try {
        const decoded: DecodedToken = jwtDecode(token);
        if (decoded.exp) {
          return new Date(decoded.exp * 1000) < new Date();
        }
      } catch (e) {
        console.error('Error decoding token for expiry check', e);
      }
    }

    // If we can't determine expiration, consider it expired for safety
    return true;
  }

  logout(): void {
    sessionStorage.removeItem(this.ACCESS_TOKEN_KEY);
    sessionStorage.removeItem(this.REFRESH_TOKEN_KEY);
    sessionStorage.removeItem(this.TOKEN_EXPIRY_KEY);
    sessionStorage.removeItem(this.USER_DETAILS_KEY);
    this.userInfoSubject.next(null);
    this.enhancedUserInfoSubject.next(null);
    sessionStorage.clear();
    this.router.navigate(['/login/auth']);
  }

  getToken(): string | null {
    return sessionStorage.getItem(this.ACCESS_TOKEN_KEY);
  }

  /**
   * Get current user information (original implementation)
   */
  getCurrentUser() {
    return this.userInfoSubject.value;
  }

  /**
   * Get enhanced user information
   */
  getEnhancedUser(): UserInfo | null {
    return this.enhancedUserInfoSubject.value;
  }

  /**
   * Check if the user has a specific role
   */
  hasRole(role: string): boolean {
    const userInfo = this.enhancedUserInfoSubject.value;
    return userInfo?.roles?.includes(role) || false;
  }

  /**
   * Check if user is authenticated
   */
  isLoggedIn(): boolean {
    const token = this.getToken();
    return !!token && !this.isTokenExpired();
  }

  refreshToken(): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/api/refresh`, {}).pipe(
      tap((response) => {
        sessionStorage.setItem(this.ACCESS_TOKEN_KEY, response.access_token);
        if (response.refresh_token) {
          sessionStorage.setItem(this.REFRESH_TOKEN_KEY, response.refresh_token);
        }
        if (response.expires_in) {
          const expiryTime = new Date().getTime() + (response.expires_in * 1000);
          sessionStorage.setItem(this.TOKEN_EXPIRY_KEY, expiryTime.toString());
        }
        // Reload user info from new token
        this.loadUserInfoFromToken();
      })
    );
  }

  updateAccount(data: any): Observable<any> {
    return this.http.put(`${this.apiUrl}` + API_ENDPOINTS.AUTH.UPDATE_USER, data).pipe(
      tap(() => {
        // After updating account, refresh user details
        const userInfo = this.userInfoSubject.value;
        if (userInfo?.preferred_username && userInfo?.email) {
          this.getAccountDetails(userInfo.preferred_username, userInfo.email).subscribe(
            details => {
              this.storeUserDetails(details);
              this.updateEnhancedUserWithDetails(details);
            }
          );
        }
      })
    );
  }

  /**
   * Update the user's password
   * @param oldPassword The user's current password
   * @param newPassword The new password
   */
  updatePassword(oldPassword: string, newPassword: string): Observable<any> {
    const userName = this.getCurrentUser()?.['preferred_username'];
    const userId = this.userInfoSubject.value?.['sub'];
    if (!userId) {
      return throwError(() => new Error('User not authenticated'));
    }

    const payload = {
      userId,
      newPassword,
      userName,
      oldPassword
    }
    return this.http.post(`${this.apiUrl}` + API_ENDPOINTS.AUTH.UPDATE_PASSWORD, payload, { responseType: 'text' });
  }

  /**
   * Send forgot password request
   * @param email The user's email
   */
  forgotPassword(email: string): Observable<any> {
    return this.http.post(`${this.apiUrl}` + API_ENDPOINTS.AUTH.FORGOT_PASSWORD, { email }, { responseType: 'text' });
  }

  /**
   * Reset password using token
   * @param token The reset token
   * @param newPassword The new password
   */
  resetPassword(token: string, newPassword: string): Observable<any> {
    const payload = {
      token,
      newPassword
    };
    return this.http.post<any>(`${this.apiUrl}` + API_ENDPOINTS.AUTH.RESET_PASSWORD, payload);
  }

  createUserProfile(payload: any): Observable<any> {
    console.log('Sending request to:', this.apiUrl + API_ENDPOINTS.AUTH.REGISTER, 'with payload:', payload);
    return this.http.post(`${this.apiUrl}` + API_ENDPOINTS.AUTH.REGISTER, payload, { responseType: 'text' });
  }

  getAccountDetails(username: any, email: any): Observable<any> {
    console.log("Fetching user details for:", username, email);
    return this.http.get<any[]>(`${this.apiUrl}` + API_ENDPOINTS.AUTH.USER_DETAILS(username, email));
  }

  /**
   * Store user details in local storage
   */
  private storeUserDetails(details: any): void {
    if (details) {
      sessionStorage.setItem(this.USER_DETAILS_KEY, JSON.stringify(details));
    }
  }

  /**
   * Get saved user details from local storage
   */
  private getSavedUserDetails(): any {
    const storedDetails = sessionStorage.getItem(this.USER_DETAILS_KEY);
    return storedDetails ? JSON.parse(storedDetails) : null;
  }

  /**
   * Update the enhanced user info with additional details
   */
  private updateEnhancedUserWithDetails(details: any): void {
    const currentUser = this.enhancedUserInfoSubject.value;
    if (currentUser) {
      const updatedUser: UserInfo = {
        ...currentUser,
        additionalDetails: details
      };
      this.enhancedUserInfoSubject.next(updatedUser);
    }
  }

  // updateAccountSection(payload:any): Observable<any> {
  //   console.log("Fetching user details for:", payload);
  //   return this.http.post<any[]>(`${this.apiUrl}`, payload);
  // }
}