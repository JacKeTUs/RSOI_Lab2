import { LOGIN_SUCCESS } from '../actions/user'
import Vue from 'vue'

const state = {
    token: null
};

const getters = {
    isAuthenticated: state => !!state.token,
    get_token: (state, getters) => {
        return state.token.toString();
    },
};

const mutations = {
    LOGIN_SUCCESS(state, responce) {
        state.token = responce.data.access_token;
        console.log(responce);
    },
    UNLOGIN_SUCCESS(state) {
        state.token = '';
    }
};

export default {
  state,
    getters,
  mutations,
}
