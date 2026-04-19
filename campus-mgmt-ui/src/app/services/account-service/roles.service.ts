import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

// Define interfaces for better type safety
export interface Role {
    label: string;
    value: string;
}

export type RoleTypeKey = 'academic' | 'administrative' | 'support' | 'student';

export type UrlRoleParam = 'academics' | 'administrative' | 'support' | 'student';

@Injectable({
    providedIn: 'root'
})
export class RoleService {
    // All possible roles organized by type
    private roles: Record<RoleTypeKey, Role[]> = {
        academic: [
            { label: 'Instructor', value: 'ROLE_INSTRUCTOR' },
            { label: 'Teacher', value: 'ROLE_TEACHER' },
            { label: 'Department Head', value: 'ROLE_DEPARTMENT_HEAD' },
            { label: 'Academic Coordinator', value: 'ROLE_ACADEMIC_COORDINATOR' },
            { label: 'Lab Technician', value: 'ROLE_LAB_TECHNICIAN' },
            { label: 'Librarian', value: 'ROLE_LIBRARIAN' },
            { label: 'Curriculum Developer', value: 'ROLE_CURRICULUM_DEVELOPER' },
            { label: 'Academic Advisor', value: 'ROLE_ACADEMIC_ADVISOR' }
        ],
        administrative: [
            { label: 'Principal', value: 'ROLE_PRINCIPAL' },
            { label: 'Director', value: 'ROLE_DIRECTOR' },
            { label: 'Registrar', value: 'ROLE_REGISTRAR' },
            { label: 'Administrative Assistant', value: 'ROLE_ADMINISTRATIVE_ASSISTANT' },
            { label: 'Receptionist', value: 'ROLE_RECEPTIONIST' },
            { label: 'HR Manager', value: 'ROLE_HR_MANAGER' },
            { label: 'HR Assistant', value: 'ROLE_HR_ASSISTANT' },
            { label: 'Finance Officer', value: 'ROLE_FINANCE_OFFICER' },
            { label: 'Accountant', value: 'ROLE_ACCOUNTANT' },
            { label: 'Admissions Officer', value: 'ROLE_ADMISSIONS_OFFICER' },
            { label: 'Examination Coordinator', value: 'ROLE_EXAMINATION_COORDINATOR' },
            { label: 'Office Manager', value: 'ROLE_OFFICE_MANAGER' },
            { label: 'Data Entry Operator', value: 'ROLE_DATA_ENTRY_OPERATOR' },
            { label: 'Records Manager', value: 'ROLE_RECORDS_MANAGER' }
        ],
        support: [
            { label: 'Maintenance Supervisor', value: 'ROLE_MAINTENANCE_SUPERVISOR' },
            { label: 'Maintenance Worker', value: 'ROLE_MAINTENANCE_WORKER' },
            { label: 'Electrician', value: 'ROLE_ELECTRICIAN' },
            { label: 'Plumber', value: 'ROLE_PLUMBER' },
            { label: 'Carpenter', value: 'ROLE_CARPENTER' },
            { label: 'Janitor', value: 'ROLE_JANITOR' },
            { label: 'Groundskeeper', value: 'ROLE_GROUNDSKEEPER' },
            { label: 'Gardener', value: 'ROLE_GARDENER' },
            { label: 'Driver', value: 'ROLE_DRIVER' },
            { label: 'Security Guard', value: 'ROLE_SECURITY_GUARD' },
            { label: 'Security Officer', value: 'ROLE_SECURITY_OFFICER' },
            { label: 'Peon', value: 'ROLE_PEON' },
            { label: 'Attendant', value: 'ROLE_ATTENDANT' },
            { label: 'Messenger', value: 'ROLE_MESSENGER' },
            { label: 'Cafeteria Worker', value: 'ROLE_CAFETERIA_WORKER' },
            { label: 'Cook', value: 'ROLE_COOK' },
            { label: 'Kitchen Helper', value: 'ROLE_KITCHEN_HELPER' },
            { label: 'IT Support Technician', value: 'ROLE_IT_SUPPORT_TECHNICIAN' },
            { label: 'Network Administrator', value: 'ROLE_NETWORK_ADMINISTRATOR' }
        ],
        student: [
            { label: 'Student', value: 'ROLE_STUDENT' }
        ]
    };

    // Mapping from URL parameters to role type keys
    private roleTypeMap: Record<string, RoleTypeKey> = {
        'academic': 'academic',
        'administrative': 'administrative',
        'support': 'support',
        'student': 'student'
    };

    // BehaviorSubject to track the current selected role
    private selectedRoleSubject = new BehaviorSubject<string | null>(null);
    selectedRole$ = this.selectedRoleSubject.asObservable();

    constructor() { }

    /**
     * Get roles by type
     * @param roleType The role type from URL or other source
     * @returns Array of role objects for the specified type
     */
    getRolesByType(roleType: string): Role[] {
        const roleKey = this.roleTypeMap[roleType] as RoleTypeKey;
        return roleKey ? this.roles[roleKey] : [];
    }

    /**
     * Get all available role types
     * @returns Array of role type keys
     */
    getRoleTypes(): RoleTypeKey[] {
        return Object.keys(this.roles) as RoleTypeKey[];
    }

    /**
     * Get the normalized role type key from URL parameter
     * @param paramValue The value from URL parameter
     * @returns The normalized role type key
     */
    getNormalizedRoleType(paramValue: string): RoleTypeKey | null {
        return this.roleTypeMap[paramValue] || null;
    }

    /**
     * Check if a role type exists
     * @param roleType The role type to check
     * @returns Boolean indicating if the role type exists
     */
    isValidRoleType(roleType: string): boolean {
        const roleKey = this.roleTypeMap[roleType];
        return !!roleKey && !!this.roles[roleKey];
    }

    /**
     * Get all roles as a flat array
     * @returns Array of all role objects
     */
    getAllRoles(): Role[] {
        return Object.values(this.roles).flat();
    }

    /**
     * Set the selected role
     * @param roleValue The role value to set
     */
    setSelectedRole(roleValue: string | null): void {
        this.selectedRoleSubject.next(roleValue);
    }
}