<template>
  <div class="home">
    <CarouselSection class="carousel-no-padding"/>
    <IntroductionSection />
    <InformationSection />
    <PopularScenario />
    <NewScenario />
  </div>
</template>

<script setup>
import CarouselSection from './components/CarouselSection.vue';
import IntroductionSection from './components/IntroductionSection.vue';
import InformationSection from './components/InformationSection.vue';
import PopularScenario from './components/PopularScenario.vue';
import NewScenario from './components/NewScenario.vue';
</script>

<style scoped>
.home {
  padding: 0;
  display: flex;
  flex-direction: column;
  min-height: calc(100vh - var(--header-height, 70px)); 
}


.carousel-no-padding {
  padding: 0;
  margin: 0;
}
</style>