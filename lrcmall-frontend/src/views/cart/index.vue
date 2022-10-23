<template>
<div class="main">
  <mycart></mycart>
</div>
</template>

<script>
import mycart from "../../components/mycart.vue";
export default {
  name: 'cartpage',
  components: {
    mycart,
  },
};
</script>

<style scoped>
.main {
  width: 85vw;
  margin-left: 4vw;
}
</style>