<template>
  <div class="background">
    <div class="container">
      <h1 class="menu-title">시나리오</h1>
      <div style="background-color: gray; height: 90vh">
        <ScenarioCard />
      </div>
    </div>
  </div>
</template>

<script>
import component from "element-plus/es/components/tree-select/src/tree-select-option.mjs";
import ScenarioCard from "./components/ScenarioCard.vue";

export default {
  name: "Scenarios",
  components: {
    ScenarioCard,
  },
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
