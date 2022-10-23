<template>
  <div class="main">
    <booklist />
  </div>
</template>

<script>
import booklist from "../../components/booklist.vue";
export default {
  data() {
    return {};
  },
//   created() {
//     console.log(this.$route.query.id);
//   },
  name: "allbooks pages",
  components: {
    booklist,
  },
};
</script>

<style scoped>
.main {
  width: 85vw;
  margin-left: 4vw;
}
</style>