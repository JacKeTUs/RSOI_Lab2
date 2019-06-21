<template>
    <div style="padding-top: 75px; padding-bottom: 25px">
        <div class="card shadow" style="width: 60%; margin: auto; padding: 20px; text-align: left">
            <h4 class="card-title"><b>«{{ book.author }}: {{ book.name }}»</b></h4>

            <br/>
            <b-row>
                <b-col cols="3">
                    <img src="../assets/book.svg" width="120px" alt="Book cover" style="background: #f5f5f5;">
                    <br/>
                </b-col>
                <b-col cols="9">
                    <p class="card-text" style="text-align: right">
                        <b-row>
                            <b-col cols="8" style="text-align: right">
                                {{ book.description }}
                            </b-col>
                        </b-row>
                        <br/>
                        <b-row>
                            <b-col cols="8">
                                <b> Жанр: {{ book.genre }}</b>
                            </b-col>
                        </b-row>
                        <b-row>
                            <b-col cols="8">
                                <b> Количество читателей: {{ book.buy_nums }}</b>
                            </b-col>
                        </b-row>
                        <br/>
                    </p>
                </b-col>
            </b-row>
        </div>

        <div id="buy_book">
            <b-button
                    v-b-toggle.collapse1 class="btn btn-info btn-sm shadowed-button" v-on:click="download" v-if="license_ok" style="margin: 10px">Загрузить книгу</b-button>
            <b-button
                    v-b-toggle.collapse1 class="btn btn-info btn-sm shadowed-button" v-else v-on:click="buyBook" style="margin: 10px">Арендовать книгу</b-button>

        </div>


    </div>
</template>

<script>
    import axios from 'axios';
    import Vue from 'vue';
    export default {
        name:"Book",
        methods: {
            buyBook(){
                console.log("Buy book!");
                setTimeout(() => {this.updateData()}, 200);

                this.license.userID = this.$store.getters.get_user_id; //Object.assign(this.license, "userID", 1);
                this.license.bookID = this.book.id; //= Object.assign(this.license, "bookID", this.book.id);
                axios.post("/api/license", this.license, {
                    headers: {
                        Authorization: 'Bearer ' + this.$store.getters.get_token
                    }
                })
                    .then(res => {
                        this.$forceUpdate();
                    })
                    .catch(err => console.log(err));
            },
            updateData(){
                console.log("update called");
                axios.get("/api/books/" + this.$route.params.id, {
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

                axios.get("/api/users/" + this.$store.getters.get_user_id + "/books/" + this.$route.params.id, {
                    headers: {
                        Authorization: 'Bearer ' + this.$store.getters.get_token
                    }
                })
                    .then(res => {
                        console.log("get lic");
                        if (res.data.license !== "false") {
                            this.license_ok = true;
                        } else {
                            this.license = JSON.parse(res.data.license);
                            this.license_ok = false;
                        }
                        console.log(this.license);
                        this.$forceUpdate();
                    })
                    .catch(err => console.log(err));
            },
            download(){
                console.log("Download DRM! License: ");
                console.log(this.license);
                axios({
                    url: '/api/books/' + this.book.id + '/download',
                    method: 'GET',
                    responseType: 'blob', // important
                    headers: {
                        Authorization: 'Bearer ' + this.$store.getters.get_token
                    }
                }).then((response) => {
                    console.log("RESPONCE OK");
                    const url = window.URL.createObjectURL(new Blob([response.data]));
                    console.log(url);
                    const link = document.createElement('a');
                    console.log(link);
                    link.href = url;
                    link.setAttribute('download', this.book.id + '.fb2e'); //or any other extension
                    document.body.appendChild(link);
                    link.click();
                });
            }
        },
        data(){
            return {
                book: {},
                license: {},
                license_ok: false,
            }
        },
        created() {
            this.updateData();
        },
    }
</script>

<style scoped>
    .card-item{
        width: 60%;
        margin-top: 25px;
        margin-left: auto;
        margin-right: auto;
        text-align: left
    }

    .card-top{
        background: #f5f5f5;
        margin: 0;
        padding-left: 20px;
        padding-right: 20px;
        padding-top: 10px
    }

    .card-content{
        margin: 0;
        padding: 20px
    }

    .shadowed-button {
        box-shadow: 0 4px 5px 0 rgba(0, 0, 0, .14), 0 1px 10px 0 rgba(0, 0, 0, .12), 0 2px 4px -1px rgba(0, 0, 0, .2)
    }
    .shadowed-button:hover{
        box-shadow: 0 2px 2px 0 rgba(0, 0, 0, .14), 0 3px 1px -2px rgba(0, 0, 0, .2), 0 1px 5px 0 rgba(0, 0, 0, .12)
    }
</style>