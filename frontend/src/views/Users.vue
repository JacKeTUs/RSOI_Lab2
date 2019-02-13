<template>
    <div class="users" >
        <h1>Профиль</h1>
        <b-button
                class="btn btn-info btn-sm shadowed-button" v-if="authenticated" v-on:click="unlogin" style="margin: 10px">Разлогиниться</b-button>

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
                showCollapse: false,
                authenticated: false,
            }
        },
        methods: {
            unlogin() {
                this.$store.commit('UNLOGIN_SUCCESS');
                this.$router.push("/");
            },
            addUser(_user){

                setTimeout(() => {this.updateData()}, 500);

                axios.post('/api/users', _user, {
                    headers: {
                        Authorization: 'Bearer ' + this.$store.getters.get_token
                    }
                })
                    .then(this.hideAddUserForm)
                    .catch(err => console.log(err));
            },
            updateData() {
                if (this.$store.getters.isAuthenticated) {
                    this.authenticated = true;
                } else {
                    this.authenticated = false;
                };
                axios.get("/api/users/" + this.userID + "/songs", {
                    headers: {
                        Authorization: 'Bearer ' + this.$store.getters.get_token
                    }
                })
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