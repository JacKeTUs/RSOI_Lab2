<template>
    <div class="card flex-row flex-wrap shadow" style="padding: 10px">
        <b-row>
            <b-col md="2">
                <img src="../assets/book.svg" width="120" alt="Book cover" style="background: #f5f5f5;">
            </b-col>
            <b-col class="card-block px-2" style="margin-left: 120px; text-align: right">
                <p class="card-text">
                    <strong>Автор:</strong> {{ book.author }}<br/>
                    <strong>Название:</strong> {{ book.name }}<br/>
                </p>
                <router-link :to="'book/' + this.book.id" class="btn btn-primary shadowed-button" style="margin-top: 20px">Подробнее...</router-link>
            </b-col>
        </b-row>
        <div class="w-100"></div>
    </div>
</template>

<script>
    import truncate from './truncate';
    import axios from 'axios';
    export default{
        name: "UserBookItem",
        components: {truncate},
        data() {
            return {
                purchase: {},
                book: {}
            }
        },
        created() {
            this.updateData();
        },
        methods: {
            updateData() {
                axios.get("/api/books/" + this.purchase.bookID, {
                    headers: {
                        Authorization: 'Bearer ' + this.$store.getters.get_token
                    }
                })
                    .then(res => {
                        console.log(res.data);
                        this.book = Object.assign({}, this.book, res.data);
                        console.log(this.book);
                        this.$forceUpdate();
                    })
                    .catch(err => console.log(err));
            },
        },
        /*{
        "id": 2,
        "userID": 1,
        "bookID": 2,
        "rating": 5
        }*/
        props: ["purchase"]
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