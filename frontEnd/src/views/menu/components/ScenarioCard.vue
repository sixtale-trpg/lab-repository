<template>
  <div>
    <div class="card-background">
      <div class="image-container">
        <img class="card-image" :src="scenario.imageURL" alt="" />
        <!-- <i class="fa-regular fa-heart"></i> -->
      </div>
      <div class="text-container">
        <div class="text-title">{{ scenario.title }}</div>
        <div class="text-genres">
          <span class="text-genre" v-for="genre in scenario.genreList">
            #{{ genre.name }}
          </span>
        </div>
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
      required: true,
    },
  },
};
</script>

<style scoped>
.card-background {
  background-color: #3c3d41;
  width: 230px;
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
  white-space: nowrap;
  overflow: hidden;
  color: white;
  margin: 10px 0 0 0;
}
.text-genres {
  white-space: nowrap;
  overflow: hidden;
  width: 100%;
  display: block;
  position: relative;
}
.text-genre {
  display: inline-block;
  margin-right: 5px;
  color: white;
  font-size: smaller;
}
</style>
