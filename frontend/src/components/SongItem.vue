<template>
    <div class="card flex-row flex-wrap shadow" style="padding: 10px">
        <b-row>
            <b-col md="2">
                <img src="../assets/song.svg" width="120" alt="Song cover" style="background: #f5f5f5;">
            </b-col>
            <b-col class="card-block px-2" style="margin-left: 20px; text-align: left">
                <p class="card-text">
                    <strong>Автор:</strong> {{ song.artist }}<br/>
                    <strong>Название:</strong> {{ song.name }}<br/>
                </p>
                <router-link :to="'song/' + this.song.id" class="btn btn-primary shadowed-button" style="margin-top: 20px">Подробнее...</router-link>
            </b-col>
        </b-row>
        <div class="w-100"></div>
    </div>
</template>

<script>
    import truncate from './truncate';
    export default{
        name: "SongItem",
        components: {truncate},
        data() {
            return {
                song: {}
            }
        },
        props: ["song"]
    }
</script>

<!--TODO make it a global style-->
<style scoped>
    .shadowed-button {
        box-shadow: 0 4px 5px 0 rgba(0, 0, 0, .14), 0 1px 10px 0 rgba(0, 0, 0, .12), 0 2px 4px -1px rgba(0, 0, 0, .2)
    }
    .shadowed-button:hover{
        box-shadow: 0 2px 2px 0 rgba(0, 0, 0, .14), 0 3px 1px -2px rgba(0, 0, 0, .2), 0 1px 5px 0 rgba(0, 0, 0, .12)
    }
</style>