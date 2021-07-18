<template>
    <div class="wrapper">
        <v-head></v-head>
        <div class="front-content-box">
            <div class="content">
                        <router-view></router-view>
            </div>
        </div>

    </div>

</template>

<script>
import vHead from './Header.vue';
export default {
    components: {
        vHead,
    },
  created() {

  }
};
</script>
<style scoped>
.content{
    overflow-y: hidden;
}
</style>
