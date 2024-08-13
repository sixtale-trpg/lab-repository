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
        @select-card="openCharacterSheet(job.id)"  
        
      />
    </div>
    <CharacterSheetModal v-if="showModal"  :job="selectedJob" @close="closeModal" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import JobCard from './JobCard.vue';
import CharacterSheetModal from './CharacterSheetModal.vue';
import { fetchJobList } from '@/common/api/JobAPI.js';
import { getCharacterSheet } from '@/common/api/CharacterSheetAPI.js';
import { getRoomInfo } from '@/common/api/RoomsAPI.js';

const route = useRoute();
const roomID = route.params.roomId;
const playMemberID = route.params.playMemberID;

const jobs = ref([]);
const showModal = ref(false);
const selectedJob = ref(null);
const existingCharacterSheet = ref(null);
const ruleId = ref(null);

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

const openCharacterSheet = async (jobID) => {
  try {
    const characterSheet = await getCharacterSheet(roomID, playMemberID);
    existingCharacterSheet.value = characterSheet;

    if (characterSheet) {
      if (characterSheet.jobID !== jobID) {
        alert('이미 다른 직업으로 캐릭터 시트를 작성하였습니다.');
        return;
      }
      selectedJob.value = characterSheet;
    } else {
      selectedJob.value = jobs.value.find(j => j.id === jobID);
    }

    showModal.value = true;
  } catch (error) {
    console.error('Error checking character sheet:', error);
    alert('캐릭터 시트를 확인하는 중 오류가 발생했습니다.');
  }
};

const closeModal = () => {
  showModal.value = false;
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
