<template>
    <div class="songs">
        <h1>Песни</h1>

        <b-pagination size="md"
                      v-model="currentPage"
                      :total-rows=pagesNum
                      :per-page=1
                      style="margin: auto; padding-top: 20px">
        </b-pagination>


        <div class="songs-list" v-bind:key="song.id" v-for="song in songs">
            <SongItem  v-bind:song="song"/>
        </div>
    </div>
</template>

<script>
    import SongItem from '../components/SongItem';
    import axios from 'axios';
    export default {
        name: "songs",
        components:{SongItem},
        computed:{
            pageLink(){
                console.log(this.currentPage);
                return this.linkGen(this.currentPage);
            }
        },
        methods: {
            linkGen(pageNum){
                return "/api/songs/?page=" + (pageNum) + "&size=" + this.pageSize;
            }
        },
        data(){
            return{
                songs:[],
                currentPage: 1,
                pagesNum: 0,
                pageSize: 5
            }
        },
        created() {
            axios.get(this.pageLink)
                .then(res => {
                    this.pagesNum = res.data.totalPages;
                    this.songs.splice(0, this.songs.length);
                    this.songs.push(...  res.data.content);
                    console.log(res.data.content);
                    console.log("-----------");
                    console.log(this.songs);})
                .catch(err => console.log(err))
        },
        updateData() {
            axios.get(this.pageLink)
                .then(res => {
                    this.pagesNum = res.data.totalPages;
                    this.songs.splice(0, this.songs.length);
                    this.songs.push(...  res.data.content);
                    console.log(res.data.content);
                    console.log("-----------");
                    console.log(this.songs);})
                .catch(err => console.log(err))
        }
    }

</script>

<style scoped>
    .songs-list{
        width: 60%;
        margin: auto;
        padding: 8px;
    }
</style>