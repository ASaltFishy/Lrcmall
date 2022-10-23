import {
  createRouter,
  createWebHistory
} from 'vue-router'
import menu from '../components/menu.vue'
import login from '../views/login/index.vue'
import home from '../views/home/index.vue'
import allbooks from '../views/allbooks/index.vue'
import cart from '../views/cart/index.vue'
import detail from '../views/detail/index.vue'
import pay from '../views/pay/index.vue'
import order from '../views/orders/index.vue'
import register from '../views/register/index.vue'
import admin from '../views/admin/index.vue'
import adminStatistics from '../views/adminStatistics/index.vue'
import userStatistics from '../views/userStatistics/index.vue'

const routes = [{
    path: '/home',
    name: 'menu',
    component: menu,
    children: [{
        path: '',
        name: 'home',
        component: home
      },
      {
        path: 'all',
        name: 'allbooks',
        component: allbooks
      },
      {
        path: 'cart',
        name: 'mycart',
        component: cart,
      },
      {
        path: 'detail',
        name: 'bookdetail',
        component: detail
      },
      {
        path: 'pay',
        name: 'pay',
        component: pay,
      },

      {
        path: 'order',
        name: 'order',
        component: order,
      },
      {
        path: 'admin',
        name: 'admin',
        component: admin,
      },
      {
        path: 'adminStatistics',
        name: 'adminStatistics',
        component: adminStatistics,
      },
      {
        path: 'userStatistics',
        name: 'userStatistics',
        component: userStatistics,
      },
    ]
  },
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'login',
    component: login
  },
  {
    path: '/register',
    name: 'register',
    component: register
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router