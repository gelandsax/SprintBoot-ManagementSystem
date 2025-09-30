import { createRouter, createWebHistory } from 'vue-router'
 import { useCurUserStore } from "../stores/user";
  import { storeToRefs } from "pinia"

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../views/HomeView.vue'),
      children: [
        {
          path: '/main',
          name: 'mainpage',
          component: () => import('../views/MainPage.vue'),
        },
        {
          path: '/userinfo/:name',
          name: 'userinfo',
          component: () => import('../views/UserInfo.vue'),
          meta: {
                requiresAuth: true,
              },
        },
        {
          path: '/emp_info_manage',
          name: 'emp_info_manage',
          component: () => import('../views/EmployeeInfoManage.vue'),
          meta: {
                requiresAuth: true,
              },
        },
        {
          path: '/dept_info_manage',
          name: 'dept_info_manage',
          component: () => import('../views/DeptInfoManage.vue'),
          meta: {
                requiresAuth: true,
              },
        },
        {
          path: '/job_info_manage',
          name: 'job_info_manage',
          component: () => import('../views/JobInfoManage.vue'),
          meta: {
                requiresAuth: true,
              },
        },
        {
          path: '/update_login',
          name: 'update_login',
          component: () => import('../views/UpdateLogin.vue'),
          meta: {
                requiresAuth: true,
              },
        },
        {
          path: '/time',
          name: 'time',
          component: () => import('../views/TimeView.vue'),
          meta: {
                requiresAuth: true,
              },
          children: [
            {
              path: 'attend',
              name: 'attend',
              component: () => import('../views/AttendView.vue'),
              meta: {
                requiresAuth: true,
              },
            },
            {
              path: 'attend_approval',
              name: 'attend_approval',
              component: () => import('../views/AttendApproval.vue'),
              meta: {
                requiresAuth: true,
              },
            },
            {
              path: 'attend_resign',
              name: 'attend_resign',
              component: () => import('../views/AttendResign.vue'),
              meta: {
                requiresAuth: true,
              },
            },
            {
              path: 'holiday',
              name: 'holiday',
              component: () => import('../views/HolidayApply.vue'),
              meta: {
                requiresAuth: true,
              },
            },
            {
              path: 'holidaylist',
              name: 'holidaylist',
              component: () => import('../views/HolidayList.vue'),
              meta: {
                requiresAuth: true,
              },
            },
            {
              path: 'holiday_approval',
              name: 'holiday_approval',
              component: () => import('../views/HolidayApproval.vue'),
              meta: {
                requiresAuth: true,
              },
            },
            {
              path: 'monthly_attendance',
              name: 'monthly_attendance',
              component: () => import('../views/MonthlyAttendance.vue'),
              meta: {
                requiresAuth: true,
              },
            },
            {
              path: 'calendar_setting',
              name: 'calendar_setting',
              component: () => import('../views/CalendarSetting.vue'),
              meta: {
                requiresAuth: true,
              },
            },
            {
              path: 'approver_setting',
              name: 'approver_setting',
              component: () => import('../views/ApproverSetting.vue'),
              meta: {
                requiresAuth: true,
              },
            },
            {
              path: 'schedule_setting',
              name: 'schedule_setting',
              component: () => import('../views/ScheduleSetting.vue'),
              meta: {
                requiresAuth: true,
              },
            },
            {
              path: 'schedule_type_setting',
              name: 'schedule_type_setting',
              component: () => import('../views/ScheduleTypeSetting.vue'),
              meta: {
                requiresAuth: true,
              },
            },
            {
              path: 'holiday_type_setting',
              name: 'holiday_type_setting',
              component: () => import('../views/HolidayTypeSetting.vue'),
              meta: {
                requiresAuth: true,
              },
            },
            ],
        },
      ],
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue'),
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('../views/AboutView.vue'),
    },
  ],
})
router.beforeEach((to, from) => {//导航守卫
  const store = useCurUserStore();
  const { curUser } = storeToRefs(store);
  if (to.meta.requiresAuth === true && curUser.value == null) {
    return { name: 'login' }
  }
})
export default router
