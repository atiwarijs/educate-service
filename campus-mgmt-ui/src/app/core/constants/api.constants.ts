export const API_ENDPOINTS = {
    AUTH: {
        LOGIN: '/auth/login',
        REGISTER: '/auth/register-user',
        REFRESH_TOKEN: '/auth/refresh-token',
        FORGOT_PASSWORD: '/auth/forgot-password',
        UPDATE_PASSWORD: '/auth/update-password',
        RESET_PASSWORD: '/auth/reset-password',
        UPDATE_USER: '/auth/update-user',
        PROFILE_EMAILID: (email: string) => `/user/email-check/${email}`,
        USER_DETAILS: (username: string, email: string) => `/user/userinfo/${username}/${email}`
    },
    USERS: {
        PROFILE: '/personal',
        PROFILE_LIST: '/personal/list',
        PROFILE_USERID: (userId: number) => `/personal/${userId}`,
        BY_ID: (id: string) => `/users/${id}`,
        UPDATE_BASIC: '/personal',
        UPDATE_FAMILY: '/family',
        UPDATE_ADDRESS: '/address',
        UPDATE_EDUCATION: '/education',
        UPDATE_MEDICAL: '/medical',
        UPDATE_EXPERIENCE: '/experience',
        UPDATE_INSURANCE: '/insurance',
        UPDATE_VEHICLE: '/vehicle'
    },
    LOCATION: {
        STATE: '/states'
    },
    STUDENT: {
        CREATE_STUDENT: '/student',
        GET_STUDENTS: '/student/fetch/list',
        UPDATE_STUDENT: `/student/update`,
        GET_STUDENT_BY_ID: (id: number) => `/student/${id}`,
        DELETE_STUDENT: (id: number) => `/student/${id}`
    },
    TEACHER: {
        CREATE_TEACHER: '/teacher',
        GET_TEACHERS: '/teacher/list',
        UPDATE_TEACHER: `/teacher`,
        GET_TEACHER_BY_ID: (id: number) => `/teacher/${id}`,
        DELETE_TEACHER: (id: number) => `/teacher/${id}`,
        DELETE_MANY_TEACHERS: (ids: number[]) => `/teacher/${ids}`
    },
    CLASS: {
        CREATE_CLASS: '/classes/create',
        GET_CLASSES: '/classes/list',
        UPDATE_CLASS: `/classes/update`,
        GET_CLASS_BY_ID: (id: number) => `/classes/${id}`,
        DELETE_CLASS: (id: number) => `/classes/${id}`,
        DELETE_MANY_CLASSES: (ids: number[]) => `/classes/${ids}`
    },
}