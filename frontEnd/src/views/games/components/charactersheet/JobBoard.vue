<template>
  <div>
    <div class="jobs-board">
      <JobCard
        v-for="job in jobs"
        :key="job.name"
        :name="job.name"
        :image="job.imageURL" 
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
import { fetchJobList } from '@/common/api/JobAPI.js';

const jobs = ref([]);
const showModal = ref(false);
const selectedJob = ref(null);
const ruleId = ref(1); // 예시로 1번 룰 사용

const loadJobs = async () => {
  try {
    jobs.value = await fetchJobList(ruleId.value);
    console.log('Fetched Jobs:', jobs.value);
  } catch (error) {
    console.error('직업 목록을 불러오는 중 오류가 발생했습니다.', error);
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

onMounted(loadJobs);
</script>


<style scoped>
.jobs-board {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 2%;
  justify-items: center;
  padding: 2.5%;
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
