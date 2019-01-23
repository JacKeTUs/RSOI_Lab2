import Vue from 'vue'
import Router from 'vue-router'
import Home from './views/Home.vue'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home
    },
    {
      path: '/user',
      name: 'user',
      component: () => import('./views/Users.vue')
    },
    {
        path: '/songs',
        name: 'songs',
        component: () => import('./views/Songs.vue')
    },
    {
        path: '/song/:id',
        name: 'Song',
        component: () => import('./views/Song.vue')
    }
  ]
})
