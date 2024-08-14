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
        :disabled="isJobDisabled(job.id)"  
        @select-card="!isJobDisabled(job.id) && openCharacterSheet(job.id)" 
      />
    </div>
    <!-- CharacterSheetModal에서 save-success 이벤트를 수신 -->
    <CharacterSheetModal v-if="showModal" :job="selectedJob" @close="closeModal" @save-success="handleSaveSuccess" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import JobCard from './JobCard.vue';
import CharacterSheetModal from './CharacterSheetModal.vue';
import { fetchJobList } from '@/common/api/JobAPI.js';
import { getRoomInfo } from '@/common/api/RoomsAPI.js';

const route = useRoute();
const roomID = route.params.roomId;
const playMemberID = route.params.playMemberID;

const jobs = ref([]);
const showModal = ref(false);
const selectedJob = ref(null);
const ruleId = ref(null);

// 캐릭터 시트가 작성된 직업 ID를 저장하는 배열로 변경
const createdCharacterSheetJobs = ref([]);

const fetchRoomInfo = async () => {
  try {
    if (!roomID) throw new Error("roomID가 정의되지 않았습니다.");
    const roomInfo = await getRoomInfo(roomID);
    ruleId.value = roomInfo.ruleID;
    await loadJobs(); // ruleId를 가져온 후 직업 목록을 불러옴
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

// 캐릭터 시트 저장 성공 시 처리
const handleSaveSuccess = () => {
  // 캐릭터 시트가 작성된 직업 ID를 배열에 추가
  if (selectedJob.value && !createdCharacterSheetJobs.value.includes(selectedJob.value.id)) {
    createdCharacterSheetJobs.value.push(selectedJob.value.id);
  }
  closeModal(); // 모달을 닫음
};

// 직업이 비활성화되어야 하는지 여부를 반환
const isJobDisabled = (jobID) => {
  return createdCharacterSheetJobs.value.includes(jobID); // 작성된 직업만 비활성화
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
