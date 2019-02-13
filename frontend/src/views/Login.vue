<template>
    <div style="width: 400px; padding: 100px">
        <div id="login">
            <h1>Авторизация</h1>
            <input type="text" name="Введите логин" v-model="input.username" placeholder="Логин" />
            <input type="password" name="Введите пароль" v-model="input.password" placeholder="Пароль" />
            <button type="button" v-on:click="login()">Login</button>
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
                    this.$router.push("/")
                }).catch(error => {
                    console.log("Error login")
                    console.log(error)
                })
                this.dialog = false
            }
        },
    }
</script>

<style>
    #login {
        display: flex;
        flex-direction: column;
        width: 300px;
        padding: 100px;
    }
</style>