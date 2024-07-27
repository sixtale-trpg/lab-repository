<template>
  <div>
    <div class="jobs-board">
      <JobCard
        v-for="job in jobs"
        :key="job.name"
        :name="job.name"
        :image="job.image_url"
        :description="job.description"
        :selectedBy="job.selectedBy"
        @select-card="openCharacterSheet"
        @edit-card="editCard"
        @delete-card="deleteCard"
      />
    </div>
    <CharacterSheetModal v-if="showModal" :job="selectedJob" @close="closeModal" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import JobCard from './JobCard.vue';
import CharacterSheetModal from './CharacterSheetModal.vue';
import axios from 'axios';

const jobs = ref([
  { name: '전사', image_url: require('@/assets/images/character/warrior.png'), description: '전사 설명입니다.' },
  { name: '사냥꾼', image_url: require('@/assets/images/character/hunter.png'), description: '사냥꾼 설명입니다.' },
  { name: '성기사', image_url: require('@/assets/images/character/paladin.png'), description: '성기사 설명입니다.' },
  { name: '음유시인', image_url: require('@/assets/images/character/bard.png'), description: '음유시인 설명입니다.' },
  { name: '마법사', image_url: require('@/assets/images/character/mage.png'), description: '마법사 설명입니다.' },
  { name: '드루이드', image_url: require('@/assets/images/character/druid.png'), description: '드루이드 설명입니다.' },
  { name: '도적', image_url: require('@/assets/images/character/rogue.png'), description: '도적 설명입니다.' },
  { name: '사제', image_url: require('@/assets/images/character/priest.png'), description: '사제 설명입니다.' }
]);

const currentUser = ref('user123'); // 현재 사용자 아이디 (로그인된 사용자)
const showModal = ref(false);
const selectedJob = ref(null);

const fetchJobs = async () => {
  try {
    const response = await axios.get('/api/jobs'); // 백엔드 API 엔드포인트를 사용하세요
    jobs.value = response.data;
  } catch (error) {
    console.error('직업 정보를 가져오는 도중 오류 발생:', error);
  }
};

const openCharacterSheet = (jobName) => {
  selectedJob.value = jobs.value.find(j => j.name === jobName);
  showModal.value = true;
};

const closeModal = () => {
  showModal.value = false;
};

const editCard = (jobName) => {
  console.log('수정할 카드:', jobName);
  // 수정하기 로직 추가
};

const deleteCard = (jobName) => {
  console.log('삭제할 카드:', jobName);
  // 삭제하기 로직 추가
};

onMounted(fetchJobs);
</script>

<style scoped>
.jobs-board {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  justify-items: center;
  padding: 20px;
}

@media (max-width: 1200px) {
  .jobs-board {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 900px) {
  .jobs-board {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 600px) {
  .jobs-board {
    grid-template-columns: 1fr;
  }
}
</style>
