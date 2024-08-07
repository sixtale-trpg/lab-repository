<template>
  <div class="modal-overlay" v-if="isVisible">
    <div class="modal-content" :style="modalContentStyle">
      <button class="close-button" @click="closeModal" aria-label="닫기">&times;</button>
      <div class="modal-header">
        <div class="modal-title-container">
          <img src="@/assets/images/character_sheet/title.png" alt="제목" class="modal-title-image">
          <h2 class="modal-title-text">상세 설정</h2>
        </div>
      </div>
      <div class="modal-tabs">
        <button
          class="tab-button"
          v-for="tab in tabs"
          :key="tab"
          :class="{ active: activeTab === tab }"
          @click="activeTab = tab"
          :style="getTabButtonStyle(tab)"
        >{{ tabLabels[tab] }}</button>
      </div>
      <div class="modal-body" :style="modalBodyStyle">
        <component 
          :is="activeComponent" 
          :character-data="characterData" 
          :equipment-data="equipmentData" 
          :stats-data="statsData"
          @update-stats="updateStatsData"
        ></component>
      </div>
      <div class="modal-footer" :style="modalFooterStyle">
        <button class="footer-button" :style="closeButtonStyle" @click="closeModal">닫기</button>
        <button class="footer-button" :style="saveButtonStyle" @click="saveForm">저장</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, defineProps, defineEmits, onMounted } from 'vue';
import axios from 'axios';
import CharacterInfo from './CharacterInfo.vue';
import Equipment from './Equipment.vue';
import Stats from './CharacterStats2.vue';

const props = defineProps(['roomID', 'playMemberID']);
const emit = defineEmits(['close']);

const isVisible = ref(true);
const activeTab = ref('character');
const tabs = ['character', 'equipment', 'stats'];
const tabLabels = {
  character: '캐릭터 정보',
  equipment: '장비',
  stats: '능력치'
};

const characterData = ref({});
const equipmentData = ref([]);
const statsData = ref({});

// 모달이 열릴 때 데이터 요청
onMounted(async () => {
  try {
    // const response = await axios.get(`/rooms/${props.roomID}/sheets/${props.playMemberID}`);
    // const data = response.data.data;
    // 캐릭터 정보 데이터
    // characterData.value = {
    //   image: data.imageURL,
    //   name: data.name,
    //   race: data.raceName,
    //   job: data.jobName,
    //   background: data.background,
    // };
    // 장비 데이터
    // equipmentData.value = data.characterEquipment.map(equip => ({
    //   name: equip.name,
    //   description: equip.description,
    //   currentCount: equip.currentCount
    // }));
    // statsData.value = {
    //   currentHp: data.currentHp,
    //   glove: data.glove,
    //   inspirationScore: data.inspirationScore,
    //   level: data.level,
    //   exp: data.exp,
    //   attributes: data.stat.reduce((acc, stat) => {
    //     acc[stat.statID] = {
    //       value: stat.statValue,
    //       weight: stat.statWeight
    //     };
    //     return acc;
    //   }, {})
    // };
  } catch (error) {
    console.error('Error fetching character sheet:', error);
  }
});

// 자식 컴포넌트에서 업데이트된 능력치 데이터를 부모로 전달받기 위한 함수
const updateStatsData = (updatedData) => {
  statsData.value = updatedData;
};

const saveForm = async () => {
  try {
    // const response = await axios.put(`/rooms/${props.roomID}/sheets/${props.playMemberID}`, {
    //   jobId: characterData.value.jobId,
    //   raceId: characterData.value.raceId,
    //   beliefId: characterData.value.beliefId,
    //   name: characterData.value.name,
    //   appearance: characterData.value.appearance,
    //   background: characterData.value.background,
    //   stat: Object.entries(statsData.value.attributes).map(([key, value]) => ({ statID: key, ...value })),
    //   characterAction: characterData.value.characterAction,
    //   characterEquipment: equipmentData.value,
    //   currentWeight: statsData.value.currentWeight,
    //   currentHp: statsData.value.currentHp,
    //   currentMoney: statsData.value.currentMoney,
    //   limitWeight: statsData.value.limitWeight,
    //   limitHp: statsData.value.limitHp,
    //   glove: statsData.value.glove,
    //   inspirationScore: statsData.value.inspirationScore,
    //   level: statsData.value.level,
    //   exp: statsData.value.exp,
    //   imageURL: characterData.value.imageURL
    // });
    // console.log('Save successful:', response.data);
  } catch (error) {
    console.error('Error saving form data:', error);
  }
};

const closeModal = () => {
  isVisible.value = false;
  emit('close');
};

// activeComponent를 정의합니다.
const activeComponent = computed(() => {
  switch (activeTab.value) {
    case 'character': return CharacterInfo;
    case 'equipment': return Equipment;
    case 'stats': return Stats;
    default: return CharacterInfo;
  }
});

const modalContentStyle = computed(() => ({
  background: `url(${require('@/assets/images/character_sheet/main_background.png')}) no-repeat center center`,
  backgroundSize: 'cover',
}));

const modalBodyStyle = computed(() => ({
  background: `url(${require('@/assets/images/character_sheet/tab_background.png')}) no-repeat center center`,
  backgroundSize: 'cover',
  marginTop: '10px',
}));

const modalFooterStyle = computed(() => ({
  background: `url(${require('@/assets/images/character_sheet/main_background.png')}) no-repeat center center`,
  backgroundSize: 'cover',
}));

// 닫기 버튼 스타일
const closeButtonStyle = computed(() => ({
  background: `url(${require('@/assets/images/maps/background/close.png')}) no-repeat center center`,
  backgroundSize: 'cover',
}));

// 저장 버튼 스타일
const saveButtonStyle = computed(() => ({
  background: `url(${require('@/assets/images/maps/background/save.png')}) no-repeat center center`,
  backgroundSize: 'cover',
}));

function getTabButtonStyle(tab) {
  const active = activeTab.value === tab;
  const imagePath = active
    ? require('@/assets/images/character_sheet/clicked_tab.png')
    : require('@/assets/images/character_sheet/tabButton.png');
  return {
    background: `url(${imagePath}) no-repeat center center`,
    backgroundSize: 'cover',
  };
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: transparent;
  padding: 0;
  border-radius: 10px;
  width: 800px;
  max-width: 90%;
  height: 600px;
  max-height: 90%;
  position: relative;
  color: #fff;
  overflow: hidden;
}

.close-button {
  position: absolute;
  top: 10px;
  right: 10px;
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: #fff;
}

.modal-header {
  text-align: center;
  margin-bottom: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  width: 100%;
  overflow: hidden;
}

.modal-title-container {
  position: relative;
  width: 100%;
}

.modal-title-image {
  width: 100%;
  height: auto;
  object-fit: cover;
}

.modal-title-text {
  position: absolute;
  top: 50%;
  left: 15%;
  transform: translate(-50%, -50%);
  color: white;
  font-size: 1.5rem;
}

.modal-tabs {
  display: flex;
  justify-content: space-between;
  margin-bottom: 0px;
}

.tab-button {
  flex: 1;
  padding: 10px;
  background-size: cover;
  border: none;
  color: #fff;
  cursor: pointer;
  margin-right: 5px;
  position: relative;
}

.tab-button.active {
  background-size: cover;
  color: white;
}

.modal-body {
  padding: 20px;
  border-radius: 0 0 10px 10px;
  flex: 1;
  overflow-y: auto;
  margin-top: 10px;
  scrollbar-color: #855e2fee #201805;
}

.modal-body::-webkit-scrollbar {
  width: 8px;
}

.modal-body::-webkit-scrollbar-track {
  background: #201805;
  border-radius: 5px;
}

.modal-body::-webkit-scrollbar-thumb {
  background-color: #855e2fee;
  border-radius: 5px;
  border: 2px solid #201805;
}

.modal-footer {
  display: flex;
  justify-content: center;
  padding: 20px;
  border-radius: 0 0 10px 10px;
}

.footer-button {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  background-size: cover;
  color: #fff;
  cursor: pointer;
  margin: 0 10px;
}

.footer-button:hover {
  transform: translateY(-2px); /* 버튼 호버 시 살짝 위로 움직이는 효과 */
}

.footer-button.save-button {
  background-size: cover;
}

.footer-button.save-button:hover {
  background-size: cover;
}
</style>
