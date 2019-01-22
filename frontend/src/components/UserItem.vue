<template>
    <div class = "user-item shadow" style="margin-bottom: 10px">
        <b-card >
            <p class="card-text">
                <strong>Имя:</strong> {{user.name}} {{user.lastName}}<br/>
                <strong>Логин:</strong> {{user.login}}<br/>
                <strong>Количество оценок:</strong> {{user.reviewsNum}}<br/>
            </p>

        </b-card>

        <div class="songs-list" v-bind:key="song.id" v-for="song in songs">
            <SongItem  v-bind:song="song"/>
        </div>
    </div>
</template>

<script>
    import SongItem from '../components/SongItem';
    import axios from 'axios';
    export default{
        name: "UserItem",
        props: ["user"],
        methods:{
            updateData() {
                axios.get("/api/users/"+ this.user.id+"/songs")
                    .then(res => {
                        this.songs.splice(0, this.songs.length);
                        this.songs.push(...  res.data.content);})
                    .catch(err => console.log(err));


                axios.get('api/users/' + this.userID)
                    .then(res => {
                        this.user=res.data.content;
                    })
                    .catch(err => console.log(err));
            },
        },
        data() {
            return {
                userID: 1,
                songs: [],
                user: user,
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
