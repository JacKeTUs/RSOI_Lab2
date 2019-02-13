import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import VueFilterDateFormat from 'vue-filter-date-format'


Vue.config.productionTip = false;

// Bootstrap
Vue.use(BootstrapVue);
Vue.use(VueFilterDateFormat);

new Vue({
  router,
    store,
  render: h => h(App)
}).$mount('#app')
