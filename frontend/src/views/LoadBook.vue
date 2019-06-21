<template>
    <div style="padding-top: 75px; padding-bottom: 25px">

            <form id="uploadForm" name="uploadForm" enctype="multipart/form-data">
                <p><input v-model="book.name" placeholder="Введите название книги" /></p>
                <p><textarea v-model="book.description" placeholder="Введите краткое описание книги"></textarea></p>
                <p><input type="file" id="file" ref="file" v-on:change="handleFileUpload()"></p>
            </form>

            <b-button
                v-b-toggle.collapse1 class="btn btn-info btn-sm shadowed-button" v-on:click="uploadFiles" style="margin: 10px">Загрузить</b-button>


    </div>
</template>

<script>
    import axios from 'axios';
    import Vue from 'vue';
    export default {
        name:"LoadBook",
        methods: {
            uploadFiles () {
                const data = new FormData();

                axios.post('/api/books', this.book, {
                    headers: {
                        Authorization: 'Bearer ' + this.$store.getters.get_token
                    }
                })
                .then(response => {
                    console.log(response);
                    this.book.id = response.data.id;

                    data.append('file', this.bookfile);
                    data.append('book_id', this.book.id);
                    data.append('book_name', this.book.name);
                    data.append('book_descr', this.book.description);

                    axios.post('/api/upload', data, {
                        headers: {
                            Authorization: 'Bearer ' + this.$store.getters.get_token,
                            'Content-Type': 'multipart/form-data'
                        }
                    })
                        .then(response => {
                            this.$router.push("/books/"+this.book.id)
                        })
                        .catch(error => {
                            console.log(error.response)
                        })
                });


            },
            created() {
                console.log("loadbook__");
            },
            handleFileUpload() {
                this.bookfile = this.$refs.file.files[0];
            }
        },
        data(){
            return {
                book: {},
                purchase: {},
                license: false,
                bookfile: ''
            }
        }
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