import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

// 解决导航栏或者底部导航tabBar中的vue-router在3.0版本以上频繁点击菜单报错的问题。
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}

const routes = [
  {
    path: '/',
    name: 'Manager',
    component: () => import('../views/Manager.vue'),
    redirect: '/home',  // 重定向到主页
    children: [
      { path: '403', name: 'NoAuth', meta: { name: '无权限' }, component: () => import('../views/manager/403') },
      { path: 'home', name: 'Home', meta: { name: '系统首页' }, component: () => import('../views/manager/Home') },
      { path: 'admin', name: 'Admin', meta: { name: '管理员信息' }, component: () => import('../views/manager/Admin') },
      { path: 'user', name: 'User', meta: { name: '学生信息' }, component: () => import('../views/manager/User') },
      { path: 'adminPerson', name: 'AdminPerson', meta: { name: '个人信息' }, component: () => import('../views/manager/AdminPerson') },
      { path: 'userPerson', name: 'UserPerson', meta: { name: '个人信息' }, component: () => import('../views/manager/UserPerson') },
      { path: 'password', name: 'Password', meta: { name: '修改密码' }, component: () => import('../views/manager/Password') },
      { path: 'department', name: 'Department', meta: { name: '社团信息' }, component: () => import('../views/manager/Department') },
      { path: 'apply', name: 'Apply', meta: { name: '申请审批' }, component: () => import('../views/manager/Apply') },
      { path: 'member', name: 'Member', meta: { name: '社团成员' }, component: () => import('../views/manager/Member') },
      { path: 'activity', name: 'Activity', meta: { name: '社团活动' }, component: () => import('../views/manager/Activity') },
      { path: 'comment', name: 'Comment', meta: { name: '社团活动评论' }, component: () => import('../views/manager/Comment') },
      { path: 'participation', name: 'Participation', meta: { name: '参与活动成员' }, component: () => import('../views/manager/Participation') },
    ]
  },
  {
    path: '/front',
    name: 'Front',
    component: () => import('../views/Front.vue'),
    children: [
      { path: 'home', name: 'Home', meta: { name: '系统首页' }, component: () => import('../views/front/Home') },
      { path: 'person', name: 'Person', meta: { name: '个人信息' }, component: () => import('../views/front/Person') },
      { path: 'departmentDetail', name: 'DepartmentDetail', meta: { name: '社团信息' }, component: () => import('../views/front/DepartmentDetail') },
      { path: 'activityDetail', name: 'ActivityDetail', meta: { name: '活动信息' }, component: () => import('../views/front/ActivityDetail') },
      { path: 'apply', name: 'Apply', meta: { name: '申请的社团' }, component: () => import('../views/front/Apply') },
      { path: 'participation', name: 'Participation', meta: { name: '我加入的社团活动' }, component: () => import('../views/front/Participation') },
    ]
  },
  { path: '/login', name: 'Login', meta: { name: '登录' }, component: () => import('../views/Login.vue') },
  { path: '/register', name: 'Register', meta: { name: '注册' }, component: () => import('../views/Register.vue') },
  { path: '*', name: 'NotFound', meta: { name: '无法访问' }, component: () => import('../views/404.vue') },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
