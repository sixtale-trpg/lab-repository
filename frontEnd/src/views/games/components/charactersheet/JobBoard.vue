<template>
  <div>
    <div class="jobs-board">
      <JobCard
        v-for="job in jobs"
        :key="job.id"
        :name="job.name"
        :image="job.imageURL"
        :description="job.description"
        :selectedBy="job.selectedBy"
        :roomId="Number(roomID)"
        :disabled="isJobDisabled(job.id)"
        @select-card="!isJobDisabled(job.id) && openCharacterSheet(job.id)"
        @delete-card="handleDeleteCard(job.id)"
      />
    </div>
    <CharacterSheetModal
      v-if="showModal"
      :job="selectedJob"
      @close="closeModal"
      @save-success="handleSaveSuccess"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import JobCard from './JobCard.vue';
import CharacterSheetModal from './CharacterSheetModal.vue';
import { fetchJobList } from '@/common/api/JobAPI.js';
import { getRoomInfo } from '@/common/api/RoomsAPI.js';

const props = defineProps({
  createdJobs: {
    type: Array,
    required: true,
  },
});

const emit = defineEmits(['job-selected']);

const route = useRoute();
const roomID = route.params.roomId;

const jobs = ref([]);
const showModal = ref(false);
const selectedJob = ref(null);
const ruleId = ref(null);

const fetchRoomInfo = async () => {
  try {
    if (!roomID) throw new Error("roomID가 정의되지 않았습니다.");
    const roomInfo = await getRoomInfo(roomID);
    ruleId.value = roomInfo.ruleID;
    await loadJobs();
  } catch (error) {
    console.error('Error fetching room info or job list:', error);
  }
};

const loadJobs = async () => {
  if (!ruleId.value) return;
  try {
    jobs.value = await fetchJobList(ruleId.value);
    console.log('Fetched Jobs:', jobs.value);
  } catch (error) {
    console.error('직업 목록을 불러오는 중 오류가 발생했습니다.', error);
  }
};

const openCharacterSheet = (jobID) => {
  selectedJob.value = jobs.value.find(j => j.id === jobID);
  showModal.value = true;
};

const closeModal = () => {
  showModal.value = false;
};

const handleSaveSuccess = (jobId) => {
  emit('job-selected', jobId);
  closeModal();
};

const handleDeleteCard = (jobId) => {
  // 삭제된 직업 ID를 createdJobs에서 제거
  const index = props.createdJobs.indexOf(jobId);
  if (index !== -1) {
    props.createdJobs.splice(index, 1);
  }
  emit('job-deleted', jobId); // 삭제 후 상위 컴포넌트에 알림
};

const isJobDisabled = (jobId) => {
  return props.createdJobs.includes(jobId);
};

onMounted(fetchRoomInfo);
</script>



<style scoped>
.jobs-board {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 2%;
  justify-items: center;
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
