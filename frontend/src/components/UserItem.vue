<template>
    <div class = "user-item shadow" style="margin-bottom: 10px">
        <b-card >
            <p class="card-text">
                <strong>Имя:</strong> {{ user.name }} {{ user.lastName }}<br/>
                <strong>Логин:</strong> {{ user.login }}<br/>
                <strong>О себе:</strong> {{ user.description }}<br/>
                <strong>Количество книг:</strong> {{ user.buy_num }}<br/>
            </p>

            <h3>Книги пользователя</h3>
            <div class="books-list" v-bind:key="book.id" v-for="book in user.books">
                <UserBookItem v-bind:purchase="book"></UserBookItem>
            </div>
        </b-card>


    </div>
</template>

<script>
    import UserBookItem from '../components/UserBookItem';
    import axios from 'axios';
    export default{
        name: "UserItem",
        components: {UserBookItem},
        props: ["user"],
        methods:{
            updateData() {

                axios.get('/api/users/' + this.$store.getters.get_user_id, {
                    headers: {
                        Authorization: 'Bearer ' + this.$store.getters.get_token
                    }
                })
                    .then(res => {
                        //this.user = Object.assign({}, this.user, res.data.content);
                        console.log(this.user);
                        this.$forceUpdate();
                    })
                    .catch(err => console.log(err));
            },
        },
        data() {
            return {
                books: [],
                user: {}
            }
        },
        created() {
            this.updateData();
        }
    }
</script>

<style scoped>
    .user-item{
        background: #f4f4f4;
        padding: 5px;
        border-bottom: 1px #ccc solid;
        text-align: left;
        width: 50%;
        margin: auto;
    }

    .shadowed-button {
        box-shadow: 0 4px 5px 0 rgba(0, 0, 0, .14), 0 1px 10px 0 rgba(0, 0, 0, .12), 0 2px 4px -1px rgba(0, 0, 0, .2)
    }
    .shadowed-button:hover{
        box-shadow: 0 2px 2px 0 rgba(0, 0, 0, .14), 0 3px 1px -2px rgba(0, 0, 0, .2), 0 1px 5px 0 rgba(0, 0, 0, .12)
    }

</style>
