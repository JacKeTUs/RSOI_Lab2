import { LOGIN_SUCCESS } from '../actions/user'
import Vue from 'vue'

const state = {
    token: null,
    user_id: null,
};

const getters = {
    isAuthenticated: state => !!state.token,
    get_token: (state, getters) => {
        return state.token.toString();
    },
    get_user_id: (state, getters) => {
        return state.user_id;
    },
};

const mutations = {
    LOGIN_SUCCESS(state, responce) {
        state.token = responce.data.access_token;
        console.log(responce);
    },
    USER_SUCCESS(state, responce) {
        state.user_id = responce.data.id;
        console.log("responce");
    },
    UNLOGIN_SUCCESS(state) {
        state.token = '';
        state.user_id = '';
    }
};

export default {
  state,
    getters,
  mutations,
}
