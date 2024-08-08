<template>
  <div class="card-background">
    <div class="image-container">
      <img class="card-image" src="@/assets/images/popular/card1.jpg" alt="" />
      <!-- <i class="fa-regular fa-heart"></i> -->
    </div>
    <div class="text-container">
      <p class="text-title">{{ scenario.title }}</p>
      <span class="text-detail" v-for="(item, index) in scenario.genres" :key="index">
        #{{ item }}
      </span>
    </div>
  </div>
</template>

<script>
import { ref } from "vue";

export default {
  name: "ScenarioCard",
  setup() {
    const scenario = ref({
      title: "아름다운 마을의 더러운 날들",
      genres: ["판타지", "스릴러", "모험"]
    });
    return { scenario };
  }
};
</script>

<style scoped>
.card-background {
  background-color: #3c3d41;
  width: 250px;
  height: 300px;
  border-radius: 1ch;
  padding: 15px;
}
.image-container {
  height: 210px;
}
.card-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 1ch;
}
.text-title {
  color: white;
  margin: 10px 0 0 0;
}
.text-detail {
  color: white;
  font-size: smaller;
}
</style>
