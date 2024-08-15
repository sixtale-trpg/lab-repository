<template>
  <div class="home">
    <CarouselSection class="carousel-no-padding"/>
    <PopularScenario class="popular-scenario" />
    <NewScenario class="new-scenario"/>
    <IntroductionSection class="introduction-section" />
    <InformationSection class="information-section"/>
  </div>
</template>

<script setup>
import CarouselSection from './components/CarouselSection.vue';
import IntroductionSection from './components/IntroductionSection.vue';
import InformationSection from './components/InformationSection.vue';
import PopularScenario from './components/PopularScenario.vue';
import NewScenario from './components/NewScenario.vue';

let scrollTimeout;

// const handleScroll = () => {
//   if (scrollTimeout) {
//     clearTimeout(scrollTimeout);
//   }

//   scrollTimeout = setTimeout(() => {
//     const sections = document.querySelectorAll('.home > *');
//     let currentSectionIndex = 0;

//     // Only check the first three sections
//     for (let i = 0; i < 3; i++) {
//       const rect = sections[i].getBoundingClientRect();
//       if (rect.top <= window.innerHeight / 2 && rect.bottom >= window.innerHeight / 2) {
//         currentSectionIndex = i;
//         break;
//       }
//     }

//     // Only apply automatic scrolling if we're in one of the first three sections
//     if (currentSectionIndex < 3) {
//       window.scrollTo({
//         top: sections[currentSectionIndex].offsetTop,
//         behavior: 'smooth'
//       });
//     }
//   }, 100);
// };

// onMounted(() => {
//   window.addEventListener('scroll', handleScroll);
// });

// onUnmounted(() => {
//   window.removeEventListener('scroll', handleScroll);
// });


</script>

<style scoped>
.home {
  padding: 0;
  display: flex;
  flex-direction: column;
  min-height: calc(100vh - var(--header-height, 70px));
}

.carousel-no-padding, 
.popular-scenario, 
.new-scenario,
.introduction-section, 
.information-section {
  min-height: 100vh;
}

.carousel-no-padding {
  background: linear-gradient(270deg, rgba(26, 26, 26, 0.45) 65%, #0a0a10 100%), linear-gradient(89.84deg, rgba(60, 60, 60, 0.9) 65.72%, #0a0a10 100%);
}
</style>