<template>
  <div class="background">
    <div class="container">
      <h1 class="menu-title">시나리오</h1>
      <div style="background-color: gray; height: 90vh">
        <ScenarioCard v-for="(item, index) in scenarios" :key="index"/>
        
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from "vue";
import ScenarioCard from "./components/ScenarioCard.vue";

onMounted();

export default {
  name: "Scenarios",
  components: {
    ScenarioCard,
  }
};
</script>

<style scoped>
.background {
  width: 100%;
  height: 100vh;
  background: radial-gradient(circle, #262626 0%, #232428 100%);
}
.container {
  max-width: 1200px;
  padding-top: 80px;
}
.menu-title {
  color: white;
  margin: 50px 0;
}
</style>
