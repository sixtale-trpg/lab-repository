<template>
  <div class="action-section" :style="backgroundStyle">
    <div class="tabs">
      <button
        v-for="tab in tabs"
        :key="tab.name"
        @click="selectTab(tab.name)"
        :class="{ active: selectedTab === tab.name }"
        :style="tabStyle(tab.name)"
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
          :style="subTabStyle(subTab.name)"
        >
          {{ subTab.label }}
        </button>
      </div>
      <div class="action-list-container">
        <div class="action-list">
          <div
            v-for="action in currentActions"
            :key="action.id"
            :class="['action-item', { 'selected': selectedAction === action.id }]"
            @click="selectAction(action.id)"
            @dblclick="openActionModal(action)"
            :style="getActionItemStyle(action.id)"
            @mouseover="showTooltip"
            @mouseleave="hideTooltip"
          >
            {{ action.name }}
            <div class="tooltip">더블 클릭시 해당 액션 정보를 볼 수 있습니다</div>
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
          :style="subTabStyle(subTab.name)"
        >
          {{ subTab.label }}
        </button>
      </div>
      <div class="action-list-container">
        <div class="action-list">
          <div
            v-for="action in currentActions"
            :key="action.id"
            :class="['action-item', { 'selected': selectedAction === action.id }]"
            @click="selectAction(action.id)"
            @dblclick="openActionModal(action)"
            :style="getActionItemStyle(action.id)"
            @mouseover="showTooltip"
            @mouseleave="hideTooltip"
          >
            {{ action.name }}
            <div class="tooltip">더블 클릭시 해당 액션 정보를 볼 수 있습니다</div>
          </div>
        </div>
      </div>
    </div>
    <ActionModal
      v-if="isActionModalVisible"
      :action="selectedModalAction"
      @close="isActionModalVisible = false"
      @select="handleSelectAction"
    />
  </div>
</template>

<script>
import { ref, watch, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { selectedPlayMemberID, selectedUserJob } from '@/store/state';
import { getCommonActions, getCharacterActions } from '@/common/api/ActionAPI.js';
import { getRoomInfo } from '@/common/api/RoomsAPI.js';
import ActionModal from '@/views/games/components/Modal/ActionModal.vue'; // ActionModal 컴포넌트 경로

export default {
  components: {
    ActionModal
  },
  setup() {
    const actionAreaBackground = require('@/assets/images/ingame/Border5.png');
    const coreActionImage = require('@/assets/images/ingame/CoreAction.png');
    const advancedActionImage = require('@/assets/images/ingame/AdvancedAction.png');
    const coreActionSelectImage = require('@/assets/images/ingame/CoreActionSelect.png');
    const advancedActionSelectImage = require('@/assets/images/ingame/AdvancedActionSelect.png');
    const actionListImage = require('@/assets/images/ingame/ActionList.png');
    const actionListHoverImage = require('@/assets/images/ingame/ActionListHover.png');
    const actionListSelectImage = require('@/assets/images/ingame/ActionListSelect.png');
    const tabImage = require('@/assets/images/ingame/Tab.png');

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
      { name: 'job', label: selectedUserJob.value || '직업' }
    ]);

    const commonSubTabs = ref([
      { name: 'basic', label: '기본액션' },
      { name: 'special', label: '특수액션' }
    ]);

    const jobSubTabs = ref([
      { name: 'core', label: '핵심액션' },
      { name: 'advanced', label: '고급액션' }
    ]);

    const selectedTab = ref('common');
    const selectedSubTab = ref('basic');
    const selectedAction = ref(null);
    const currentActions = ref([]);

    const isActionModalVisible = ref(false);
    const selectedModalAction = ref(null);

    const route = useRoute();
    const roomId = ref(route.params.roomId);
    const ruleID = ref(null);

    const fetchRoomInfo = async () => {
      try {
        const roomInfo = await getRoomInfo(roomId.value);
        ruleID.value = roomInfo.ruleID;
        await updateCurrentActions(); // 룰 아이디가 설정된 후에 액션을 불러옴
      } catch (error) {
        console.error('Error fetching room info:', error);
      }
    };

    const updateCurrentActions = async () => {
      try {
        if (selectedTab.value === 'job') {
          const jobActions = await getCharacterActions(roomId.value, selectedPlayMemberID.value);
          currentActions.value = jobActions[selectedSubTab.value] || [];
        } else {
          if (ruleID.value !== null) {
            const commonActions = await getCommonActions(ruleID.value);
            if (selectedSubTab.value === 'basic') {
              currentActions.value = commonActions.basicActions || [];
            } else if (selectedSubTab.value === 'special') {
              currentActions.value = commonActions.specialActions || [];
            }
          }
        }
      } catch (error) {
        console.error('Error fetching actions:', error);
      }
    };

    const openActionModal = (action) => {
      selectedModalAction.value = {
        category: selectedTab.value === 'common' ? commonSubTabs.value.find(subTab => subTab.name === selectedSubTab.value).label : jobSubTabs.value.find(subTab => subTab.name === selectedSubTab.value).label,
        ...action
      };
      isActionModalVisible.value = true;
    };

    onMounted(() => {
      fetchRoomInfo();
    });

    watch([selectedTab, selectedSubTab], updateCurrentActions);

    const tabStyle = (tabName) => {
      return {
        background: selectedTab.value === tabName
          ? 'none'
          : `url(${tabImage}) no-repeat center center`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        border: 'none',
        padding: '0',
        margin: '0',
      };
    };

    const selectTab = (tabName) => {
      selectedTab.value = tabName;
      selectedSubTab.value = tabName === 'common' ? 'basic' : 'core';
      selectedAction.value = null;
      updateCurrentActions();
    };

    const selectSubTab = (subTabName) => {
      selectedSubTab.value = subTabName;
      selectedAction.value = null;
      updateCurrentActions();
    };

    const selectAction = (actionId) => {
      selectedAction.value = actionId;
    };

    const subTabStyle = (subTabName) => {
      const isCoreOrBasic = subTabName === 'core' || subTabName === 'basic';
      const isSelected = selectedSubTab.value === subTabName;
      const imagePath = isSelected
        ? (isCoreOrBasic ? coreActionSelectImage : advancedActionSelectImage)
        : (isCoreOrBasic ? coreActionImage : advancedActionImage);
      return {
        backgroundImage: `url(${imagePath})`,
        backgroundSize: 'cover',
        backgroundRepeat: 'no-repeat',
      };
    };

    const getActionItemStyle = (actionId) => {
      const isSelected = selectedAction.value === actionId;
      const baseStyle = {
        backgroundSize: '100% 100%',
        backgroundPosition: 'center',
        width: '45%',
        backgroundRepeat: 'no-repeat',
      };

      if (isSelected) {
        return {
          ...baseStyle,
          backgroundImage: `url(${actionListSelectImage})`,
        };
      }

      return {
        ...baseStyle,
        backgroundImage: `url(${actionListImage})`,
        ':hover': {
          backgroundImage: `url(${actionListHoverImage})`,
        },
      };
    };

    const showTooltip = (event) => {
      const tooltip = event.target.querySelector('.tooltip');
      if (tooltip) {
        tooltip.style.visibility = 'visible';
        tooltip.style.opacity = 1;
        tooltip.style.top = `${event.target.offsetHeight}px`;
        tooltip.style.left = `50%`;
        tooltip.style.transform = `translateX(-50%)`;
      }
    };

    const hideTooltip = (event) => {
      const tooltip = event.target.querySelector('.tooltip');
      if (tooltip) {
        tooltip.style.visibility = 'hidden';
        tooltip.style.opacity = 0;
      }
    };

    const handleSelectAction = (action) => {
      console.log('Selected action:', action);
      // 추가 로직 작성...
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
      selectSubTab,
      selectedAction,
      selectAction,
      subTabStyle,
      tabStyle,
      getActionItemStyle,
      isActionModalVisible,
      selectedModalAction,
      openActionModal,
      handleSelectAction,
      showTooltip,
      hideTooltip,
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
  margin-bottom: 15px;
  padding: 0; /* 여백 제거 */
  height: 40px; /* 탭의 높이를 설정 */
}

.tabs button {
  flex: 1;
  padding: 0; 
  border: none;
  background-color: transparent;
  background-size: cover; 
  background-position: center; 
  color: white;
  cursor: pointer;
  font-size: 1.2rem; 
  display: flex;
  align-items: center; 
  justify-content: center; 
  height: 100%;
  box-sizing: border-box; 
}

.tabs button.active {
  background-color: #000000; 
}

.tabs button:not(.active) {
  background-size: cover;
  background-position: center;
}

.tab-content {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  overflow: hidden;
}

.sub-tabs {
  display: flex;
  justify-content: space-between;
  margin-top: 15px;
  margin-bottom: 15px; 
  padding: 0 10px; 
}

.sub-tabs button {
  flex: 1;
  margin: 0 5px;
  padding: 8px 0;
  border: none;
  color: white;
  border-radius: 5px;
  cursor: pointer;
  width: 45%;
  height: 35px;
  transition: border 0.3s;
  font-size: 1rem; 
}

.action-list-container {
  flex-grow: 1;
  overflow: hidden;
  height: 200px; /* 필요에 따라 높이 값을 조정 */
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
  scrollbar-color: #855e2fee #201805;
}

.action-item {
  color: white;
  border-radius: 5px;
  width: calc(50% - 15px);
  text-align: center;
  cursor: pointer;
  border: 1px solid #444;
  height: 35px; 
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.8rem; /* 텍스트 크기 조정 */
  transition: border-color 0.3s ease, transform 0.3s ease;
  background-size: 100% 100%;
  background-position: center;
  background-repeat: no-repeat;
  box-sizing: border-box;
  position: relative;
}

.tooltip {
  visibility: hidden;
  width: 150px;
  background-color: rgba(0, 0, 0, 0.8);
  color: #fff;
  text-align: center;
  border-radius: 3px;
  padding: 5px;
  position: absolute; /* 고정 위치 */
  z-index: 10;
  opacity: 0;
  transition: opacity 0.3s;
  font-size: 0.8rem;
}

.tooltip::after {
  content: '';
  position: absolute;
  top: -5px;
  left: 50%;
  margin-left: -5px;
  border-width: 5px;
  border-style: solid;
  border-color: rgba(0, 0, 0, 0.8) transparent transparent transparent;
}


/* Chrome, Edge, Safari 스크롤바 커스터마이징 */
.action-list::-webkit-scrollbar {
  width: 8px;
}

.action-list::-webkit-scrollbar-track {
  background: #a56722;
  border-radius: 30px; 
}

.action-list::-webkit-scrollbar-thumb {
  background-color: #6b3d01;
  border-radius: 30px; 
  border: 1px solid #5e3b13;
}
</style>
