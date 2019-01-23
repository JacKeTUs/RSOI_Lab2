<template>
    <div class="users" >
        <h1>Профиль</h1>
        <b-button
            v-b-toggle.collapse1 class="btn btn-info btn-sm shadowed-button" style="margin: 10px">Добавить пользователя</b-button>
        <b-collapse id="collapse1" v-model="showCollapse" class="mt-2">
            <AddUserForm style="width: 50%; margin: auto"
                         v-on:hide-add-user-form="hideAddUserForm"
                         v-on:add-user="addUser"/>
        </b-collapse>


        <div class="userItem" v-for="user">
            <UserItem v-bind:user="user"></UserItem>
        </div>

    </div>
</template>

<script>
    import UserItem from "../components/UserItem";
    import AddUserForm from '../components/AddUserForm'
    import axios from 'axios';
    export default {
        name: "users",
        components: {UserItem, AddUserForm},
        data(){
            return{
                userID: 1,
                user: {},
                showCollapse: false
            }
        },
        methods: {
            addUser(_user){

                setTimeout(() => {this.updateData()}, 500);

                axios.post('/api/users', _user)
                    .then(this.hideAddUserForm)
                    .catch(err => console.log(err));
            },
            updateData() {

                axios.get("/api/users/" + this.userID + "/songs")
                    .then(res => {
                        console.log(res.data);
                        this.user = Object.assign({}, this.user, res.data);
                        console.log(this.user);
                        this.$forceUpdate();
                    })
                    .catch(err => console.log(err));

            },
            hideAddUserForm(){
                this.showCollapse = false;
            }
        },
        created() {
            this.updateData();
            this.$on('add-user', (user) => {
                //this.user = user;
                this.addUser(user);
            })
        }
    }
</script>

<style scoped>
    .shadowed-button {
        box-shadow: 0 4px 5px 0 rgba(0, 0, 0, .14), 0 1px 10px 0 rgba(0, 0, 0, .12), 0 2px 4px -1px rgba(0, 0, 0, .2)
    }
    .shadowed-button:hover{
        box-shadow: 0 2px 2px 0 rgba(0, 0, 0, .14), 0 3px 1px -2px rgba(0, 0, 0, .2), 0 1px 5px 0 rgba(0, 0, 0, .12)
    }
</style>