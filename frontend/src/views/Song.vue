<template>
    <div style="padding-top: 75px; padding-bottom: 25px">
        <div class="card shadow" style="width: 60%; margin: auto; padding: 20px; text-align: left">
            <h4 class="card-title"><b>«{{ song.artist }}: {{ song.name }}»</b></h4>

            <br/>
            <b-row>
                <b-col cols="3">
                    <img src="../assets/song.svg" width="120px" alt="Song cover" style="background: #f5f5f5;">
                    <br/>
                </b-col>
                <b-col cols="9">
                    <p class="card-text" style="text-align: left">
                        <b-row>
                            <b-col cols="8" style="text-align: right">
                                {{ song.rate_nums }} оценок
                            </b-col>
                        </b-row>
                        <br/>
                        <b-row>
                            <b-col cols="6">
                                <b>Средний рейтинг: {{ song.rating }}</b>
                            </b-col>
                        </b-row>
                        <b-row>
                            <b-col cols="6">
                                <b> Количество покупок: {{ song.buy_nums }}</b>
                            </b-col>
                        </b-row>
                        <br/>
                    </p>
                </b-col>
            </b-row>
        </div>

        <div id="buy_song">
            <button v-on:click="buySong">Приобрести песню</button>
        </div>
    </div>
</template>

<script>
    import axios from 'axios';
    import Vue from 'vue';
    export default {
        name:"Song",
        methods: {
            buySong(){
                setTimeout(() => {this.updateData()}, 50);

                this.currentPage = 0;
            },
            updateData(){
                console.log("update called");

                axios.get("/api/songs/" + this.$route.params.id)
                    .then(res => {
                        console.log(res.data);
                        this.song = Object.assign({}, this.song, res.data);
                        console.log(this.song);
                        this.$forceUpdate();
                    })
                    .catch(err => console.log(err));
            },
        },
        data(){
            return {
                song: {}
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

</style>