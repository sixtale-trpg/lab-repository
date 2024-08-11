<template>
  <div>
    <div class="card-background">
      <div class="image-container">
        <img class="card-image" src="@/assets/images/popular/card1.jpg" alt="" />
        <!-- <i class="fa-regular fa-heart"></i> -->
      </div>
      <div class="text-container">
        <p class="text-title">{{ scenario.title }}</p>
        <span class="text-detail" v-for="genre in scenario.genres">
          #{{ genre }}
        </span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ScenarioCard",
  props: {
    scenario: {
      type: Object,
      required: true
    }
  }
};
</script>

<style scoped>
.card-background {
  background-color: #3c3d41;
  width: 250px;
  height: 225px;
  border-radius: 1ch;
  padding: 15px;
}
.image-container {
  height: 140px;
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
