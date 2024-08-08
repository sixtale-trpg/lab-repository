<template>
  <section class="popular-scenario">
    <div class="container">
      
      <div class="header">
        <h3>인기 시나리오</h3>
      </div>
      <div class="scenarios-grid">
        <div class="scenario-card" v-for="(scenario, index) in scenarios" :key="index">
          <div class="image-container">
            <img :src="require(`@/assets/images/popular/${scenario.image}`)" :alt="scenario.title" class="static-image" />
            <img :src="require(`@/assets/images/popular/${scenario.gif}`)" :alt="scenario.title" class="gif-image" />
          </div>
          <div class="info">
            <h3>{{ scenario.title }}</h3>
            <p>{{ scenario.genre }}</p>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref } from 'vue';

const scenarios = ref([
  {
    image: 'card1.jpg',
    gif: 'card1.gif',
    title: 'Scenario 1',
    genre: 'Genre 1'
  },
  {
    image: 'card2.jpg',
    gif: 'card2.gif',
    title: 'Scenario 2',
    genre: 'Genre 2'
  },
  {
    image: 'card3.jpg',
    gif: 'card3.gif',
    title: 'Scenario 3',
    genre: 'Genre 3'
  },
  {
    image: 'card4.jpg',
    gif: 'card4.gif',
    title: 'Scenario 4',
    genre: 'Genre 4'
  },
  {
    image: 'card5.jpg',
    gif: 'card5.gif',
    title: 'Scenario 5',
    genre: 'Genre 5'
  }
]);
</script>

<style scoped>
.popular-scenario {
  background: linear-gradient(270deg, rgba(38, 38, 38, 0.45) 65%, #0e0e15 100%), linear-gradient(89.84deg, rgba(56, 56, 56, 0.9) 65.72%, #0e0e15 100%);
  color: white;
  padding:2%; 
}

.container {
  padding: 40px 20px; 
  border-radius: 0; 
  max-width: 1200px; 
  margin: 0 auto;
  text-align: center;
}

.header {
  margin-top: 20px;
  margin-bottom: 20px;
  display: flex;
}

.header h2 {
  font-size: 2.5rem; 
  margin: 0;
}

.scenarios-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 20px;
}

.scenario-card {
  padding: 20px;
  border-radius: 10px;
  overflow: hidden;
  transition: transform 0.3s;
  text-align: center;
}

.scenario-card:hover {
  transform: scale(1.05);
}

.image-container {
  position: relative;
  width: 100%;
  padding-top: 75%; 
  overflow: hidden;
}

.static-image,
.gif-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: opacity 0.3s;
}

.gif-image {
  opacity: 0;
}

.scenario-card:hover .gif-image {
  opacity: 1;
}

.scenario-card:hover .static-image {
  opacity: 0;
}

.info {
  padding: 10px;
  text-align: center;
}

.info h3 {
  margin-bottom: 10px;
  font-size: 1.5rem; 
}

.info p {
  margin: 0;
}
</style>
