<template>
    <div class="jobs-board">
      <JobCard
        v-for="job in jobs"
        :key="job.title"
        :title="job.title"
        :image="job.image"
      />
    </div>
  </template>
  
  <script>
  import JobCard from './JobCard.vue';
  
  export default {
    components: {
      JobCard
    },
    data() {
      return {
        jobs: [
          { title: '전사', image: 'path/to/warrior.jpg' },
          { title: '사냥꾼', image: 'path/to/hunter.jpg' },
          { title: '성기사', image: 'path/to/paladin.jpg' },
          { title: '음유시인', image: 'path/to/bard.jpg' },
          { title: '마법사', image: 'path/to/mage.jpg' },
          { title: '드루이드', image: 'path/to/druid.jpg' },
          { title: '도적', image: 'path/to/rogue.jpg' },
          { title: '사제', image: 'path/to/priest.jpg' }
        ]
      };
    }
  };
  </script>
  
  <style scoped>
  .jobs-board {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 20px;
    justify-items: center;
    padding: 20px;
  }
  </style>
  