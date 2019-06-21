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
        path: '/books',
        name: 'books',
        component: () => import('./views/Books.vue'),
        beforeEnter: ifAuthenticated,
    },
    {
        path: '/book/:id',
        name: 'Book',
        component: () => import('./views/Book.vue'),
        beforeEnter: ifAuthenticated,
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('./views/Register.vue'),
      beforeEnter: ifNotAuthenticated,
    },
    {
      path: '/books/upload',
      name: 'LoadBook',
      component: () => import('./views/LoadBook.vue'),
      beforeEnter: ifAuthenticated,
    },
    {
      path: '/search',
      name: 'Search',
      component: () => import('./views/Search.vue'),
      beforeEnter: ifAuthenticated,
    },
      {
          path: '/users/edit',
          name: 'UsersEdit',
          component: () => import('./views/EditUser.vue'),
          beforeEnter: ifAuthenticated,
      }
  ]
})
