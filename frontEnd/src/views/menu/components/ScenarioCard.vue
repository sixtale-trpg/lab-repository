<template>
  <div class="card-background">
    <div class="image-container">
      <img class="card-image" src="@/assets/images/popular/card1.jpg" alt="" />
    </div>
  </div>
</template>

<script>
export default {
  name: "ScenarioCard",
};
</script>

<style scoped>
.card-background {
  background-color: #3c3d41;
  width: 300px;
  height: 400px;
  border-radius: 1ch;
  padding: 20px;
}
.image-container {
  height: 270px;
}
.card-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 1ch;
}
</style>
