<template>
    <div class="books" style = "padding: 100px">

        <div align="center">
            <p><textarea v-model="searchData.author" placeholder="Автор"></textarea></p>
            <p><input v-model="searchData.name" placeholder="Название" /></p>
            <p><textarea v-model="searchData.description" placeholder="Описание"></textarea></p>
            <p><textarea v-model="searchData.genre" placeholder="Жанр"></textarea></p>
            <b-button
                    v-b-toggle.collapse1 class="btn btn-info btn-sm shadowed-button" v-on:click="updateData" style="margin: 10px">Искать</b-button>

        </div>

        <div align="center" class="books-list" v-bind:key="book.id" v-for="book in books">
            <BookItem  v-bind:book="book"/>
        </div>

        <b-pagination align="center" size="md"
                      v-model="currentPage"
                      :number-of-pages=2
                      :per-page=50
                      @change="pageChange"
                      style="margin: auto; padding-top: 20px">
        </b-pagination>
    </div>
</template>

<script>
    import BookItem from '../components/BookItem';
    import axios from 'axios';
    export default {
        name: "search",
        components:{
            BookItem
        },
        computed:{
            pageLink(){
                console.log(this.currentPage);
                return this.linkGen(this.currentPage-1);
            }
        },
        methods: {
            linkGen(pageNum){
                return "/api/search/?page=" + (pageNum) + "&size=" + this.pageSize;
            },
            updateData() {
                axios.post(this.pageLink, this.searchData,
                {
                    headers: {
                        Authorization: 'Bearer ' + this.$store.getters.get_token
                    }
                })
                    .then(res => {
                        console.log(res.data);
                        this.pagesNum = res.data.totalPages;
                        this.books.splice(0, this.books.length);
                        this.books.push(...  res.data.content);
                        console.log(this.books);
                        this.$forceUpdate();
                    })
                    .catch(err => console.log(err))
            },
            pageChange(page){
                console.log(page);
                this.currentPage = page;
                this.updateData();
            }
        },
        data(){
            return{
                books: [],
                currentPage: 1,
                pagesNum: 0,
                pageSize: 50,
                searchData: {}
            }
        },
        created() {
            this.updateData();
            this.$on('input', (page) => {
                console.log(page);
                this.currentPage = page;
                this.updateData();
            })
        },

    }

</script>

<style scoped>
    .books-list{
        width: 60%;
        margin: auto;
        padding: 8px;
    }
</style>