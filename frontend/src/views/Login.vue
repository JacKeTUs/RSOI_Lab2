<template>
    <div style="width: 600px; padding: 100px">
        <div id="login">
            <h1>Авторизация</h1>
            <input type="text" name="Введите логин" v-model="input.username" placeholder="Логин" />
            <input type="password" name="Введите пароль" v-model="input.password" placeholder="Пароль" />

            <b-button
                    v-b-toggle.collapse1 class="btn btn-info btn-sm shadowed-button" v-on:click="login" style="margin: 10px">Войти</b-button>

            <b-button
                    v-b-toggle.collapse1 class="btn btn-info btn-sm shadowed-button" v-on:click="reg" style="margin: 10px">Зарегистрироваться</b-button>

            <b-button
                    v-b-toggle.collapse1 class="btn btn-info btn-sm shadowed-button" v-on:click="google" style="margin: 10px">Войти через Google</b-button>

        </div>
    </div>
</template>

<script>

    import * as Base64 from "iconv-lite";
    import axios from 'axios';

    export default {
        name: 'login',
        data () {
            return {
                input: {
                    username: '',
                    password: '',
                }
            }
        },
        methods: {
            login () {

                axios.get('/api/login?username='+this.input.username+'&password='+this.input.password, {
                    headers: {
                        Authorization: 'Basic ' + btoa("frontend:secret")
                    }
                }).then(response => {
                    this.$store.commit('LOGIN_SUCCESS', response);

                    axios.get('/api/users/find?username='+this.input.username, {
                        headers: {
                            Authorization: 'Bearer ' + this.$store.getters.get_token
                        }
                    }).then(response => {
                        this.$store.commit('USER_SUCCESS', response);
                    });

                    this.$router.push("/")
                }).catch(error => {
                    console.log("Error login")
                    console.log(error)
                })
                this.dialog = false
            },
            reg () {
                this.$router.push("/register")
            },
            google() {
                this.$router.push("http://oauth.google.com")
            }
        },
    }
</script>

<style>
    #login {
        display: flex;
        flex-direction: column;
        width: 600px;
        padding: 100px;
    }
    .shadowed-button {
        box-shadow: 0 4px 5px 0 rgba(0, 0, 0, .14), 0 1px 10px 0 rgba(0, 0, 0, .12), 0 2px 4px -1px rgba(0, 0, 0, .2)
    }
    .shadowed-button:hover{
        box-shadow: 0 2px 2px 0 rgba(0, 0, 0, .14), 0 3px 1px -2px rgba(0, 0, 0, .2), 0 1px 5px 0 rgba(0, 0, 0, .12)
    }

</style>

