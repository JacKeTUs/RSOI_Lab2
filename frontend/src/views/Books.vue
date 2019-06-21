<template>
    <div class="books" style = "padding: 100px">

        <b-pagination align="center"
                      size="md"
                      v-model="currentPage"
                      :number-of-pages=2
                      :per-page=5
                      @change="pageChange"
                      style="margin: auto; padding-top: 20px">
        </b-pagination>
        <div class="books-list" align="center"
             v-bind:key="book.id" v-for="book in books">
            <BookItem  v-bind:book="book"/>
        </div>
    </div>
</template>

<script>
    import BookItem from '../components/BookItem';
    import axios from 'axios';
    export default {
        name: "books",
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
                return "/api/books/?page=" + (pageNum) + "&size=" + this.pageSize;
            },
            updateData() {
                axios.get(this.pageLink, {
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
                pageSize: 5
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