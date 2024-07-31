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
          <div
            v-for="action in currentActions"
            :key="action.id"
            :class="['action-item', { 'selected': selectedAction === action.id }]"
            @click="selectAction(action.id)"
          >
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
          :class="['sub-tab-button', { active: selectedSubTab === subTab.name }]"
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
          >
            {{ action.name }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, watch } from 'vue';
import { selectedPlayMemberID, selectedUserJob } from '@/store/state'; // 전역 상태 가져오기

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
      { name: 'job', label: selectedUserJob.value || '직업' }
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
    const selectedAction = ref(null); // 선택된 액션을 추적하는 변수

    const actions = ref({
      basic: [
        { id: 1, name: '기본 액션 1' }, { id: 2, name: '기본 액션 2' },
        { id: 3, name: '기본 액션 3' }, { id: 4, name: '기본 액션 4' },
        { id: 5, name: '기본 액션 5' }, { id: 6, name: '기본 액션 6' },
        { id: 7, name: '기본 액션 7' }, { id: 8, name: '기본 액션 8' },
        { id: 9, name: '기본 액션 9' }, { id: 10, name: '기본 액션 10' },
        { id: 11, name: '기본 액션 11' }, { id: 12, name: '기본 액션 12' }
      ],
      special: [
        { id: 13, name: '특수 액션 1' }, { id: 14, name: '특수 액션 2' },
        { id: 15, name: '특수 액션 3' }, { id: 16, name: '특수 액션 4' },
        { id: 17, name: '특수 액션 5' }, { id: 18, name: '특수 액션 6' }
      ],
      core: [
        { id: 19, name: '핵심 액션 1' }, { id: 20, name: '핵심 액션 2' },
        { id: 21, name: '핵심 액션 3' }, { id: 22, name: '핵심 액션 4' }
      ],
      advanced: [
        { id: 23, name: '고급 액션 1' }, { id: 24, name: '고급 액션 2' },
        { id: 25, name: '고급 액션 3' }, { id: 26, name: '고급 액션 4' }
      ]
    });

    // 이 아래 존재하는 모든 스킬들은 임시 더미 데이터
const jobActions = ref({
  Warrior: {
    core: [
      { id: 1, name: '강타' },
      { id: 2, name: '방패 돌진' },
      { id: 3, name: '전투의 함성' },
      { id: 4, name: '방어 태세' },
      { id: 5, name: '무적' },
      { id: 6, name: '분노의 격노' },
      { id: 7, name: '철벽 방어' },
      { id: 8, name: '결의' }
    ],
    advanced: [
      { id: 9, name: '전투의 절정' },
      { id: 10, name: '광전사의 격노' },
      { id: 11, name: '강철의 의지' },
      { id: 12, name: '대지의 힘' }
    ]
  },
  Mage: {
    core: [
      { id: 13, name: '파이어볼' },
      { id: 14, name: '아이스 스피어' },
      { id: 15, name: '라이트닝 볼트' },
      { id: 16, name: '아케인 미사일' },
      { id: 17, name: '소환: 고대 정령' },
      { id: 18, name: '블리자드' },
      { id: 19, name: '매혹의 눈' },
      { id: 20, name: '타임 스톱' }
    ],
    advanced: [
      { id: 21, name: '마법 방어' },
      { id: 22, name: '무한한 지식' },
      { id: 23, name: '엘리멘탈 마스터리' },
      { id: 24, name: '현자의 통찰' }
    ]
  },
  Rogue: {
    core: [
      { id: 25, name: '은신' },
      { id: 26, name: '치명적인 공격' },
      { id: 27, name: '독의 칼날' },
      { id: 28, name: '섀도우 스텝' },
      { id: 29, name: '어둠의 달인' },
      { id: 30, name: '비열한 기습' },
      { id: 31, name: '은신의 달인' },
      { id: 32, name: '서펜트의 이빨' }
    ],
    advanced: [
      { id: 33, name: '맹독 칼날' },
      { id: 34, name: '섀도우 댄스' },
      { id: 35, name: '그림자의 속삭임' },
      { id: 36, name: '치명타 전문가' }
    ]
  },
  Cleric: {
    core: [
      { id: 37, name: '치유' },
      { id: 38, name: '신성한 방어' },
      { id: 39, name: '축복의 빛' },
      { id: 40, name: '희망의 기도' },
      { id: 41, name: '정화' },
      { id: 42, name: '수호자의 방패' },
      { id: 43, name: '강화의 기도' },
      { id: 44, name: '신성한 반격' }
    ],
    advanced: [
      { id: 45, name: '천사의 날개' },
      { id: 46, name: '생명의 기원' },
      { id: 47, name: '신성한 돌진' },
      { id: 48, name: '영원의 보호막' }
    ]
  },
  Ranger: {
    core: [
      { id: 49, name: '정밀 사격' },
      { id: 50, name: '속사' },
      { id: 51, name: '짐승의 우두머리' },
      { id: 52, name: '자연의 조화' },
      { id: 53, name: '함정 설치' },
      { id: 54, name: '은밀한 사냥꾼' },
      { id: 55, name: '독화살' },
      { id: 56, name: '정밀 타격' }
    ],
    advanced: [
      { id: 57, name: '폭풍의 화살' },
      { id: 58, name: '자연의 힘' },
      { id: 59, name: '자연의 보호' },
      { id: 60, name: '야수의 마음' }
    ]
  },
  Paladin: {
    core: [
      { id: 61, name: '성스러운 심판' },
      { id: 62, name: '신성한 방어' },
      { id: 63, name: '성역' },
      { id: 64, name: '구원의 손길' },
      { id: 65, name: '빛의 보호막' },
      { id: 66, name: '정의의 심판' },
      { id: 67, name: '천상의 빛' },
      { id: 68, name: '불굴의 의지' }
    ],
    advanced: [
      { id: 69, name: '성역의 수호자' },
      { id: 70, name: '성기사의 결의' },
      { id: 71, name: '빛의 용사' },
      { id: 72, name: '천상의 희생' }
    ]
  },
  Druid: {
    core: [
      { id: 73, name: '자연의 부름' },
      { id: 74, name: '변신' },
      { id: 75, name: '대지의 노래' },
      { id: 76, name: '야수의 친구' },
      { id: 77, name: '자연의 분노' },
      { id: 78, name: '바람의 춤' },
      { id: 79, name: '대지의 수호자' },
      { id: 80, name: '자연의 회복' }
    ],
    advanced: [
      { id: 81, name: '대지의 영혼' },
      { id: 82, name: '천변' },
      { id: 83, name: '자연의 분노' },
      { id: 84, name: '자연의 소환' }
    ]
  },
  Bard: {
    core: [
      { id: 85, name: '매혹의 노래' },
      { id: 86, name: '위대한 연주' },
      { id: 87, name: '모험가의 노래' },
      { id: 88, name: '용기의 송가' },
      { id: 89, name: '희망의 연주' },
      { id: 90, name: '유혹의 리듬' },
      { id: 91, name: '서정시' },
      { id: 92, name: '용맹의 노래' }
    ],
    advanced: [
      { id: 93, name: '환상곡' },
      { id: 94, name: '전설의 이야기' },
      { id: 95, name: '신비의 멜로디' },
      { id: 96, name: '심연의 노래' }
    ]
  }
});

    // 실제 백엔드 연동 시 사용할 함수 (주석 처리)
    /*
    const fetchRuleData = async (ruleID) => {
      try {
        const response = await axios.get(`/rules/${ruleID}`);
        if (response.data.statusCode === 200) {
          const commonActions = response.data.data.commonAction;

          // commonActions에서 기본/특수 액션 구분하여 저장
          actions.value.basic = commonActions.filter(action => action.isBasic);
          actions.value.special = commonActions.filter(action => !action.isBasic);
        } else {
          console.error('Failed to fetch rule data:', response.data.responseMessage);
        }
      } catch (error) {
        console.error('Error fetching rule data:', error);
      }
    };
    */

    // 현재 액션 목록을 선택된 탭과 전역 상태의 직업에 따라 필터링
    const currentActions = computed(() => {
      if (selectedTab.value === 'job') {
        const job = jobActions.value[selectedUserJob.value];
        if (job) {
          return job[selectedSubTab.value] || [];
        }
      } else {
        return actions.value[selectedSubTab.value] || [];
      }
      return [];
    });

    const selectTab = (tabName) => {
      selectedTab.value = tabName;
      selectedSubTab.value = tabName === 'common' ? 'basic' : 'core';
      selectedAction.value = null; // 탭을 변경할 때 선택된 액션 초기화
    };

    const selectSubTab = (subTabName) => {
      selectedSubTab.value = subTabName;
      selectedAction.value = null; // 서브탭을 변경할 때 선택된 액션 초기화
    };

    const selectAction = (actionId) => {
      selectedAction.value = actionId;
    };
    
    // 전역 상태의 변경을 감지하여 액션 목록 업데이트
    watch(selectedPlayMemberID, async (newID) => {
      console.log('Selected play member ID changed to:', newID);
      // 필요한 추가 로직이 있으면 여기에 작성

      // 실제 API 요청을 보내는 로직 (주석 처리)
      /*
      try {
        const response = await axios.get(`/rooms/${roomID}/sheets/${newID}`);
        if (response.data.statusCode === 200) {
          // 전역 상태에 플레이어의 직업 정보를 저장하는 로직
          const { jobName, characterAction } = response.data.data;
          selectedUserJob.value = jobName;
          jobActions.value[jobName] = {
            core: characterAction.filter(action => action.isCore),
            advanced: characterAction.filter(action => !action.isCore)
          };
        }
      } catch (error) {
        console.error('Error fetching user job:', error);
      }
      */
    });

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
      selectAction
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
  border: 1px solid #ffffff;

}

.sub-tabs button.active {
  background-color: #800000;
  border: 1px solid #ffffff;
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
  transition: background-color 0.3s ease, border-color 0.3s ease;
}

.action-item:hover {
  background-color: #274e13; /* 호버 시 배경색 변경 */
  border-color: #ffffff; /* 호버 시 테두리 색 변경 */
}

.action-item.selected {
  background-color: #800000; /* 선택된 액션의 배경색 변경 */
  border: 1px solid #ffffff; /* 선택된 액션 강조 */
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
