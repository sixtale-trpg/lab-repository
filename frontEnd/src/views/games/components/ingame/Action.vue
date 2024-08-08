<template>
  <div class="action-section" :style="backgroundStyle">
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
    <div v-if="selectedTab === 'common'" class="tab-content">
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
      <div class="action-list-container">
        <div class="action-list">
          <div v-for="action in currentActions" :key="action.id" class="action-item">
            {{ action.name }}
          </div>
        </div>
      </div>
    </div>
    <div v-if="selectedTab === 'job'" class="tab-content">
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
      <div class="action-list-container">
        <div class="action-list">
          <div v-for="action in currentActions" :key="action.id" class="action-item">
            {{ action.name }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed } from 'vue';

export default {
  setup() {
    const actionAreaBackground = require('@/assets/images/ingame/Border5.png');
    
    const backgroundStyle = {
      backgroundImage: `url(${actionAreaBackground})`,
      backgroundSize: '100% 100%',
      backgroundRepeat: 'no-repeat',
      backgroundPosition: 'center',
      display: 'flex',
      flexDirection: 'column',
      padding: '10px',
      margin: '5px',
      boxSizing: 'border-box',
      width: '100%',
      height: '100%',
      position: 'relative',
    };
    
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
      actions.value.basic = [
        { id: 1, name: '기본 액션 1' }, { id: 2, name: '기본 액션 2' },
        { id: 3, name: '기본 액션 3' }, { id: 4, name: '기본 액션 4' },
        { id: 5, name: '기본 액션 5' }, { id: 6, name: '기본 액션 6' },
        { id: 7, name: '기본 액션 7' }, { id: 8, name: '기본 액션 8' },
        { id: 9, name: '기본 액션 9' }, { id: 10, name: '기본 액션 10' },
        { id: 11, name: '기본 액션 11' }, { id: 12, name: '기본 액션 12' }
      ];
      actions.value.special = [
        { id: 13, name: '특수 액션 1' }, { id: 14, name: '특수 액션 2' },
        { id: 15, name: '특수 액션 3' }, { id: 16, name: '특수 액션 4' },
        { id: 17, name: '특수 액션 5' }, { id: 18, name: '특수 액션 6' }
      ];
      actions.value.core = [
        { id: 19, name: '핵심 액션 1' }, { id: 20, name: '핵심 액션 2' },
        { id: 21, name: '핵심 액션 3' }, { id: 22, name: '핵심 액션 4' }
      ];
      actions.value.advanced = [
        { id: 23, name: '고급 액션 1' }, { id: 24, name: '고급 액션 2' },
        { id: 25, name: '고급 액션 3' }, { id: 26, name: '고급 액션 4' }
      ];
    };

    fetchData();

    const currentActions = computed(() => actions.value[selectedSubTab.value]);

    const selectTab = (tabName) => {
      selectedTab.value = tabName;
      selectedSubTab.value = tabName === 'common' ? 'basic' : 'core';
    };

    const selectSubTab = (subTabName) => {
      selectedSubTab.value = subTabName;
    };

    return {
      backgroundStyle,
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
  height: 100%;
  display: flex;
  flex-direction: column;
}

.tabs {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  border-bottom: 2px solid #564307;
}

.sub-tabs {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
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
  height: 40px;
}

.sub-tabs button {
  flex: 1;
  margin: 0 5px;
  padding: 8px 0;
  border: none;
  background-color: #274e13;
  color: white;
  border-radius: 5px;
  cursor: pointer;
  height: 35px;
}

.tabs button.active {
  background-color: #110519;
  border-bottom: none;
}

.sub-tabs button.active {
  background-color: #800000;
  border: none;
}

.tab-content {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  overflow: hidden;
}

.action-list-container {
  flex-grow: 1;
  overflow: hidden;
}

.action-list {
  height: 100%;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  align-content: flex-start;
  gap: 10px;
  padding: 10px;
  overflow-y: auto;
  scrollbar-width: thin;
  scrollbar-color: #110519 #a56722;
}

.action-item {
  background-color: #a56722;
  color: white;
  padding: 5px;
  border-radius: 5px;
  width: calc(50% - 5px);
  text-align: center;
  cursor: pointer;
  border: 1px solid #444;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* Chrome, Edge, Safari 스크롤바 커스터마이징 */
.action-list::-webkit-scrollbar {
  width: 8px;
}

.action-list::-webkit-scrollbar-track {
  background: #a56722;
  border-radius: 5px;
}

.action-list::-webkit-scrollbar-thumb {
  background-color: #274e13;
  border-radius: 5px;
  border: 1px solid #a56722;
}
</style>