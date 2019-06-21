<template>
    <div align="center" style="width: 400px; padding: 100px">
        <div align="center" id="reg">
            <h4>Изменение профиля пользователя</h4>
            <input type="text" name="Введите имя" v-model="input.name" placeholder="Имя"  />
            <input type="textarea" name="Расскажите о себе" v-model="input.description" placeholder="Расскажите о себе"/>

            <h4>Выберите тип аккаунта</h4>
            <select v-model="input.type">
                <option v-for="option in items" v-bind:value="option.id">
                    {{ option.title }}
                </option>
            </select>
            <b-button
                    class="btn btn-info btn-sm shadowed-button" v-on:click="edit" style="margin: 10px">Сохранить изменения</b-button>

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
                input: {},
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
            edit () {
                this.input.id = this.$store.getters.get_user_id;
                axios.put('/api/users/edit', this.input, {
                    headers: {
                        Authorization: 'Basic ' + btoa("frontend:secret")
                    }
                }).then(response => {

                }).catch(error => {
                    console.log("Error reg");
                    console.log(error);
                });
            },
            updateData() {
                axios.get("/api/users/" + this.$store.getters.get_user_id, {
                    headers: {
                        Authorization: 'Bearer ' + this.$store.getters.get_token
                    }
                })
                    .then(res => {
                        console.log("!!!");
                        console.log(res.data);
                        this.input = Object.assign({}, this.input, res.data);
                        // console.log(this.user);
                        this.$forceUpdate();
                    })
                    .catch(err => console.log(err));

            },
            onChange(key) {
                this.input.type = this.items[key-1].id
            },
            created() {
                this.updateData();
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