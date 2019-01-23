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
                    <p class="card-text" style="text-align: right">
                        <b-row>
                            <b-col cols="8" style="text-align: right">
                                {{ song.rateNum }} оценок
                            </b-col>
                        </b-row>
                        <br/>
                        <b-row>
                            <b-col cols="8">
                                <b>Ссылка на песню: {{ song.link }}</b>
                            </b-col>
                        </b-row>
                        <b-row>
                            <b-col cols="8">
                                <b>Средний рейтинг: {{ song.rating }}</b>
                            </b-col>
                        </b-row>
                        <b-row>
                            <b-col cols="8">
                                <b> Количество покупок: {{ song.buy_nums }}</b>
                            </b-col>
                        </b-row>
                        <br/>
                    </p>
                </b-col>
            </b-row>
        </div>

        <div id="buy_song">
            <b-button
                    v-b-toggle.collapse1 class="btn btn-info btn-sm shadowed-button" v-if="license"  style="margin: 10px">Песня уже куплена</b-button>
            <b-button
                    v-b-toggle.collapse1 class="btn btn-info btn-sm shadowed-button" v-else v-on:click="buySong" style="margin: 10px">Приобрести песню</b-button>

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
                console.log("Buy song!");
                setTimeout(() => {this.updateData()}, 200);

                this.purchase.userID = 1; //Object.assign(this.purchase, "userID", 1);
                this.purchase.songID = this.song.id; //= Object.assign(this.purchase, "songID", this.song.id);
                axios.post("/api/purchase", this.purchase)
                    .then(res => {
                        this.$forceUpdate();
                    })
                    .catch(err => console.log(err));
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

                axios.get("/api/users/" + 1 + "/songs/" + this.$route.params.id)
                    .then(res => {
                        console.log(res.data);
                        if (res.data.license !== "true") {
                            this.purchase = Object.assign({}, this.purchase, res.data.purchase);
                            this.license = false;
                        } else {
                            this.license = true;
                        }
                        this.$forceUpdate();
                    })
                    .catch(err => console.log(err));
            },
        },
        data(){
            return {
                song: {},
                purchase: {},
                license: false,
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