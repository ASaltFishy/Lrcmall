<template>
  <zoumadeng />
  <bookcards />
</template>

<script>
import zoumadeng from "../../components/zoumadeng.vue";
import bookcards from "../../components/bookcards.vue";
import { ElMessage } from "element-plus";

export default {
  name: "home",
  components: {
    zoumadeng,
    bookcards,
  },
};
</script>

<style>
</style>