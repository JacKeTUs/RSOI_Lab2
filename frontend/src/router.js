import Vue from 'vue'
import Router from 'vue-router'
import Home from './views/Home.vue'
import store from './store'

Vue.use(Router)

const ifNotAuthenticated = (to, from, next) => {
    if (!store.getters.isAuthenticated) {
        next()
        return
    }
    next('/')
}

const ifAuthenticated = (to, from, next) => {
    if (store.getters.isAuthenticated) {
        next()
        return
    }
    next('/login')
}

export default new Router({
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home,
        beforeEnter: ifAuthenticated,
    },
    {
        path: '/login',
        name: 'login',
        component: () => import('./views/Login.vue'),
        beforeEnter: ifNotAuthenticated,
    },
    {
      path: '/user',
      name: 'user',
      component: () => import('./views/Users.vue'),
        beforeEnter: ifAuthenticated,
    },
    {
        path: '/songs',
        name: 'songs',
        component: () => import('./views/Songs.vue'),
        beforeEnter: ifAuthenticated,
    },
    {
        path: '/song/:id',
        name: 'Song',
        component: () => import('./views/Song.vue'),
        beforeEnter: ifAuthenticated,
    }
  ]
})
