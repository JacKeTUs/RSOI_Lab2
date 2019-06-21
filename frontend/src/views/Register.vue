<template>
    <div style="width: 400px; padding: 100px">
        <div id="reg">
            <h1>Регистрация</h1>
            <input type="text" name="Введите логин" v-model="input.username" placeholder="Логин" />
            <input type="password" name="Введите пароль" v-model="input.password" placeholder="Пароль" />
            <input type="text" name="Расскажите о себе" v-model="input.description" placeholder="Расскажите о себе" />

            <select v-model="input.type">
                <option v-for="option in items" v-bind:value="option.id">
                    {{ option.title }}
                </option>
            </select>
            <span>Выбрано: {{ input.type }}</span>
            <b-button
                    class="btn btn-info btn-sm shadowed-button" v-if="authenticated" v-on:click="reg" style="margin: 10px">Зарегистрироваться</b-button>

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
                    type: 0,
                },
                items: [
                    {
                        id: 0,
                        title: 'Читатель',
                    },
                    {
                        id: 1,
                        title: 'Автор',
                    },
                ],
                selected: '',
                _user: ''
            }
        },
        methods: {
            reg () {
                // this._user = this.input;
                /*axios.get('/api/login?username='+this.input.username+'&password='+this.input.password, {
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
                this.dialog = false*/

                axios.post('/api/register_user', this.input, {
                    headers: {
                        Authorization: 'Basic ' + btoa("frontend:secret")
                    }
                }).then(response => {

                    axios.get('/api/login?username='+this.input.username+'&password='+this.input.password, {
                        headers: {
                            Authorization: 'Basic ' + btoa("frontend:secret")
                        }
                    }).then(response => {
                        this.$store.commit('LOGIN_SUCCESS', response);

                        axios.post('/api/users', this.input, {
                            headers: {
                                Authorization: 'Bearer ' + this.$store.getters.get_token
                            }
                        }).then(response => {

                            console.log("Succ 2");
                        }).catch(error => {
                            console.log("Error reg");
                            console.log(error);
                        });

                        axios.get('/api/users/find?username='+this.input.username, {
                            headers: {
                                Authorization: 'Bearer ' + this.$store.getters.get_token
                            }
                        }).then(response => {
                            this.$store.commit('USER_SUCCESS', response);
                        });



                    }).catch(error => {
                        console.log("Error login");
                        console.log(error)
                    })


                }).catch(error => {
                    console.log("Error reg");
                    console.log(error);
                });



            },

            onChange(key) {
                this.input.type = this.items[key-1].id
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
    .shadowed-button {
         box-shadow: 0 4px 5px 0 rgba(0, 0, 0, .14), 0 1px 10px 0 rgba(0, 0, 0, .12), 0 2px 4px -1px rgba(0, 0, 0, .2)
     }
    .shadowed-button:hover{
        box-shadow: 0 2px 2px 0 rgba(0, 0, 0, .14), 0 3px 1px -2px rgba(0, 0, 0, .2), 0 1px 5px 0 rgba(0, 0, 0, .12)
    }
</style>