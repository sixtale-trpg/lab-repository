<template>
  <div class="main-wrapper">
    <MainHeader />
    <div class="main-container">
      <main class="main-content">
        <router-view></router-view>
      </main>
    </div>
    <MainFooter v-if="!isHomePage" />
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import MainHeader from './components/main-header.vue';
import MainFooter from './components/main-footer.vue';

const route = useRoute();
const isHomePage = computed(() => route.name === 'home');
</script>

<style scoped>
.main-wrapper {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.main-container {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
  padding: 0;
}
</style>