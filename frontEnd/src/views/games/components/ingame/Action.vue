<template>
  <div class="action-section" :style="{ backgroundImage: `url(${backgroundImageUrl})` }">
    <div class="tabs">
      <button
        v-for="tab in tabs"
        :key="tab.name"
        @click="selectTab(tab.name)"
        :class="{ active: selectedTab === tab.name }"
      >
        {{ tab.label }}
      </button>
    </div>
    <div v-if="selectedTab === 'common'">
      <div class="sub-tabs">
        <button
          v-for="subTab in commonSubTabs"
          :key="subTab.name"
          @click="selectSubTab(subTab.name)"
          :class="{ active: selectedSubTab === subTab.name }"
        >
          {{ subTab.label }}
        </button>
      </div>
      <div class="action-list">
        <div v-for="action in currentActions" :key="action.id" class="action-item">
          {{ action.name }}
        </div>
      </div>
    </div>
    <div v-if="selectedTab === 'job'">
      <div class="sub-tabs">
        <button
          v-for="subTab in jobSubTabs"
          :key="subTab.name"
          @click="selectSubTab(subTab.name)"
          :class="{ active: selectedSubTab === subTab.name }"
        >
          {{ subTab.label }}
        </button>
      </div>
      <div class="action-list">
        <div v-for="action in currentActions" :key="action.id" class="action-item">
          {{ action.name }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue';
// import axios from 'axios';

export default {
  setup() {
    const backgroundImageUrl = require('@/assets/images/ingame/Border5.png');
    const tabs = ref([
      { name: 'common', label: '공통' },
      { name: 'job', label: '직업' }
    ]);
    const commonSubTabs = ref([
      { name: 'basic', label: '기본' },
      { name: 'special', label: '특수' }
    ]);
    const jobSubTabs = ref([
      { name: 'core', label: '핵심' },
      { name: 'advanced', label: '고급' }
    ]);
    const selectedTab = ref('common');
    const selectedSubTab = ref('basic');

    const actions = ref({
      basic: [],
      special: [],
      core: [],
      advanced: []
    });

    const fetchData = async () => {
      // 실제 백엔드 연동시 사용될 코드 (현재는 주석 처리)
      /*
      try {
        const { data: basicData } = await axios.get('/api/actions?is_basic=1');
        const { data: specialData } = await axios.get('/api/actions?is_basic=0');
        const { data: coreData } = await axios.get('/api/job-actions?is_core=1');
        const { data: advancedData } = await axios.get('/api/job-actions?is_core=0');

        actions.value.basic = basicData;
        actions.value.special = specialData;
        actions.value.core = coreData;
        actions.value.advanced = advancedData;
      } catch (error) {
        console.error('Error fetching data:', error);
      }
      */

      // 임시 데이터 설정
      actions.value.basic = [
        { id: 1, name: '기본 액션 1' }, { id: 2, name: '기본 액션 2' },
        { id: 3, name: '기본 액션 3' }, { id: 4, name: '기본 액션 4' },
        { id: 5, name: '기본 액션 5' }, { id: 6, name: '기본 액션 6' }
      ];
      actions.value.special = [
        { id: 7, name: '특수 액션 1' }, { id: 8, name: '특수 액션 2' },
        { id: 9, name: '특수 액션 3' }, { id: 10, name: '특수 액션 4' },
        { id: 11, name: '특수 액션 5' }, { id: 12, name: '특수 액션 6' }
      ];
      actions.value.core = [
        { id: 13, name: '핵심 액션 1' }, { id: 14, name: '핵심 액션 2' },
        { id: 15, name: '핵심 액션 3' }, { id: 16, name: '핵심 액션 4' }
      ];
      actions.value.advanced = [
        { id: 17, name: '고급 액션 1' }, { id: 18, name: '고급 액션 2' },
        { id: 19, name: '고급 액션 3' }, { id: 20, name: '고급 액션 4' }
      ];
    };

    onMounted(fetchData);

    const currentActions = computed(() => actions.value[selectedSubTab.value]);

    const selectTab = (tabName) => {
      selectedTab.value = tabName;
      selectedSubTab.value = tabName === 'common' ? 'basic' : 'core';
    };

    const selectSubTab = (subTabName) => {
      selectedSubTab.value = subTabName;
    };

    return {
      backgroundImageUrl,
      tabs,
      commonSubTabs,
      jobSubTabs,
      selectedTab,
      selectedSubTab,
      currentActions,
      selectTab,
      selectSubTab
    };
  }
};
</script>

<style scoped>
.action-section {
  background-size: cover;
  background-repeat: no-repeat;
  padding: 10px; 
}

.tabs {
  display: flex;
  justify-content: space-between;
  margin-bottom: 0; 
  border-bottom: 2px solid #564307; 
}

.sub-tabs {
  display: flex;
  justify-content: space-between;
  margin: 10px 0;
}

.tabs button {
  flex: 1;
  margin: 0;
  padding: 8px 0;
  border: none;
  border-bottom: 3px solid transparent; 
  background-color: #564307;
  color: white;
  cursor: pointer;
  height: 40px; /* 높이 조정 */
}

.sub-tabs button {
  flex: 1;
  margin: 0 5px;
  padding: 8px 0;
  border: none;
  background-color: #274e13; /* 어두운색 초록 */
  color: white;
  margin-top: 5px;
  border-radius: 5px;
  cursor: pointer;
  height: 35px; /* 서브 탭 높이 조정 */
}

.tabs button.active {
  background-color: rgb(43, 15, 15); 
  border-bottom: none;
}

.sub-tabs button.active {
  background-color: #800000; /* 어두운색 자주 */
  border: none;
}

.action-list {
  display: flex;
  flex-wrap: wrap;
  justify-content: center; /* 가운데 정렬 */
  column-gap: 10px; /* 가로 간격 */
  row-gap: 5px; /* 세로 간격 */
  padding: 10px;
  height: 300px; /* 고정 높이 */
  overflow-y: auto; /* 스크롤 생성 */
}

.action-item {
  background-color: #a56722; /* 황토색 배경 */
  color: white;
  padding: 5px; /* 내부 여백 조정 */
  border-radius: 5px;
  width: calc(50% - 20px); /* 두 개씩 배치되도록 */
  text-align: center;
  cursor: pointer;
  border: 1px solid #444;
  height: 50px; /* 고정 높이 설정 */
  display: flex;
  align-items: center; /* 수직 정렬 */
  justify-content: center; /* 수평 정렬 */
}


</style>
