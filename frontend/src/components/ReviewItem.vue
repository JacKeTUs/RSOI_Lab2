<template>
    <b-card style="margin: 10px;">
        <b-row>
        <b-col cols="11">

            <div style="background: #f2f7fd; padding: 10px">
                <b-row>
                    <b-col cols="2">
                        Оценка:
                    </b-col>

                </b-row>


                <br/>

            </div>

        </b-col>
        <b-col cols=1>
            <b-button @click="deleteReview" variant="outline-danger" >x</b-button>
        </b-col>
        </b-row>
    </b-card>
</template>

<script>
    import axios from 'axios';
    import StarRating from 'vue-dynamic-star-rating';
    export default{
        name: "ReviewItem",
        components: {StarRating},
        methods: {
            deleteReview(){
                axios.delete('/api/reviews/' + this.review.id)
                    .then(this.$emit('review-deleted'))
                    .catch(err => console.log(err));
            }
        },
        data(){
            return{
                user:{},
                starsConfig: {
                    rating: this.review.rating,
                    isIndicatorActive: false,
                    starStyle: {
                        fullStarColor: '#ed8a19',
                        emptyStarColor: '#737373',
                        starWidth: 20,
                        starHeight: 20
                    }
                },
                date:{}
            }
        },
        created(){
            axios.get('/api/users/' + this.review.uid)
                .then(res => {
                    this.user = res.data
                })
                .catch(err => console.log(err));

            this.date = new Date(this.review.postedTime);
        },
        props: ["review", "book"]
    }
</script>



<style scoped>

</style>