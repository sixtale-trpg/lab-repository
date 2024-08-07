<template>
    <div class="action-tab" :style="tabBackgroundStyle">
      <div class="inner-tabs">
        <button
          class="inner-tab-button"
          v-for="tab in innerTabs"
          :key="tab"
          :class="{ active: activeInnerTab === tab }"
          @click="activeInnerTab = tab"
          :style="getTabButtonStyle(tab)"
        >{{ innerTabLabels[tab] }}</button>
      </div>
      <div class="inner-tab-content">
        <div v-if="activeInnerTab === 'common'">
          <div class="action-categories-horizontal">
            <div class="action-category">
              <h3>기본 액션</h3>
              <div class="action-cards">
                <div class="action-card" v-for="action in commonActions.basic" :key="action.id">
                  <strong>{{ action.name }}</strong>: {{ action.description }}
                </div>
              </div>
            </div>
            <div class="action-category">
              <h3>특수 액션</h3>
              <div class="action-cards">
                <div class="action-card" v-for="action in commonActions.special" :key="action.id">
                  <strong>{{ action.name }}</strong>: {{ action.description }}
                </div>
              </div>
            </div>
          </div>
        </div>
        <div v-if="activeInnerTab === 'character'">
          <div class="action-categories-horizontal">
            <div class="action-category">
              <h3>핵심 액션</h3>
              <div class="action-cards">
                <div class="action-card" v-for="action in characterActions.core" :key="action.id">
                  <strong>{{ action.name }}</strong>: {{ action.description }}
                </div>
              </div>
            </div>
            <div class="action-category">
              <h3>고급 액션</h3>
              <div class="action-cards">
                <div class="action-card" v-for="action in characterActions.advanced" :key="action.id">
                  <strong>{{ action.name }}</strong>: {{ action.description }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted, computed } from 'vue';
  import axios from 'axios';
  
  const commonActions = ref({
    basic: [
      { id: 1, name: '기본 액션 1', description: '기본 액션 설명 1' },
      { id: 2, name: '기본 액션 2', description: '기본 액션 설명 2' }
    ],
    special: [
      { id: 3, name: '특수 액션 1', description: '특수 액션 설명 1' },
      { id: 4, name: '특수 액션 2', description: '특수 액션 설명 2' }
    ]
  });
  
  const characterActions = ref({
    core: [
      { id: 5, name: '핵심 액션 1', description: '핵심 액션 설명 1' },
      { id: 6, name: '핵심 액션 2', description: '핵심 액션 설명 2' }
    ],
    advanced: [
      { id: 7, name: '고급 액션 1', description: '고급 액션 설명 1' },
      { id: 8, name: '고급 액션 2', description: '고급 액션 설명 2' }
    ]
  });
  
  const props = defineProps(['roomID', 'playMemberID']);
  
  // 내부 탭 상태와 라벨 정의
  const activeInnerTab = ref('common');
  const innerTabs = ['common', 'character'];
  const innerTabLabels = {
    common: '공통 액션',
    character: '캐릭터 액션',
  };
  
  // 공통 및 캐릭터 액션 데이터 가져오기
  // onMounted(async () => {
  //   try {
  //     // 공통 액션 데이터 가져오기
  //     const commonResponse = await axios.get(`/actions/common`);
  //     commonActions.value = {
  //       basic: commonResponse.data.data.filter(action => action.type === 'basic'),
  //       special: commonResponse.data.data.filter(action => action.type === 'special')
  //     };
  
  //     // 캐릭터 액션 데이터 가져오기
  //     const characterResponse = await axios.get(`/actions/character/${props.playMemberID}`);
  //     characterActions.value = {
  //       core: characterResponse.data.data.filter(action => action.type === 'core'),
  //       advanced: characterResponse.data.data.filter(action => action.type === 'advanced')
  //     };
  //   } catch (error) {
  //     console.error('Error fetching actions:', error);
  //   }
  // });
  
  const tabBackgroundStyle = computed(() => ({
    background: `url(${require('@/assets/images/character_sheet/tab_background.png')}) no-repeat center center`,
    backgroundSize: 'cover',
  }));
  
  function getTabButtonStyle(tab) {
    const active = activeInnerTab.value === tab;
    const imagePath = active
      ? require('@/assets/images/character_sheet/clicked_tab.png')
      : require('@/assets/images/character_sheet/tabButton.png');
    return {
      background: `url(${imagePath}) no-repeat center center`,
      backgroundSize: 'cover',
      color: active ? 'white' : '#ccc',
      fontSize: '16px'
    };
  }
  </script>
  
  <style scoped>
  .action-tab {
    padding: 20px;
    color: #fff;
  }
  
  .inner-tabs {
    display: flex;
    justify-content: center;
    margin-bottom: 20px;
  }
  
  .inner-tab-button {
    padding: 20px 40px; /* 크기를 키움 */
    border: none;
    cursor: pointer;
    margin: 0 10px;
    position: relative;
    background-size: cover;
    color: #ccc; /* 선택되지 않은 탭 폰트 색상 */
  }
  
  .inner-tab-button.active {
    background-size: cover;
    color: white; /* 선택된 탭 폰트 색상 */
  }
  
  .inner-tab-content {
    padding: 20px;
    background-color: transparent; /* 바디 색상과 일치하도록 투명 처리 */
    border-radius: 5px;
  }
  
  .action-categories-horizontal {
    display: flex;
    justify-content: space-between;
    gap: 20px;
  }
  
  .action-category {
    flex: 1;
    background-color: transparent; /* 바디 색상과 일치하도록 투명 처리 */
  }
  
  .action-cards {
    display: flex;
    flex-direction: column;
  }
  
  .action-card {
    background-color: #333;
    border: 1px solid #444;
    border-radius: 5px;
    padding: 10px;
    margin-bottom: 10px;
    color: #fff;
  }
  
  h3 {
    margin-bottom: 10px;
    text-align: center;
  }
  </style>
  