<template>
  <div v-if="isOpen" class="modal-overlay" @click.self="closeModal">
    <div class="modal-content" :style="modalStyle">
      <div class="modal-header">
        <h2>룰북</h2>
      </div>
      <div class="modal-body">
        <div class="rulebook">
          <div class="tabs">
            <div class="tab" v-for="(tab, index) in tabs" :key="index" @click="selectTab(index)">
              <img :src="index === selectedTab ? activeTabImage : tab.image" />
              <span>{{ tab.label }}</span>
            </div>
          </div>
          <div class="page-content">
            <transition name="page-flip" mode="out-in">
              <div class="page left" :key="selectedTab + '-' + currentPage">
                <p v-for="(content, index) in leftPages[selectedTab]" :key="index" v-show="currentPage === index">{{ content }}</p>
              </div>
            </transition>
            <transition name="page-flip" mode="out-in">
              <div class="page right" :key="selectedTab + '-' + currentPage">
                <p v-for="(content, index) in rightPages[selectedTab]" :key="index" v-show="currentPage === index">{{ content }}</p>
              </div>
            </transition>
            <button v-if="currentPage > 0" @click="prevPage" class="nav-button prev">
              <img :src="prevArrow" alt="Previous" />
            </button>
            <button v-if="currentPage < maxPage - 1" @click="nextPage" class="nav-button next">
              <img :src="nextArrow" alt="Next" />
            </button>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button @click="closeModal">닫기</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';

const props = defineProps({
  isOpen: Boolean,
});

const emit = defineEmits(['close']);

const closeModal = () => {
  emit('close');
};

// 이미지 경로 설정
const rulebookImage = require('@/assets/images/room/rulebook_modal/book.png');
const tabImage = require('@/assets/images/room/rulebook_modal/vector.png');
const activeTabImage = require('@/assets/images/room/rulebook_modal/active_vector.png');
const prevArrow = require('@/assets/images/room/rulebook_modal/Arrow_Down.png');
const nextArrow = require('@/assets/images/room/rulebook_modal/Arrow_Up.png');

// 탭 설정
const tabs = [
  { label: '서문', image: tabImage },
  { label: '게임의 기본', image: tabImage },
  { label: '직업과 역할', image: tabImage },
  { label: '게임 시스템', image: tabImage },
  { label: '게임 환경', image: tabImage },
  { label: '게임 진행', image: tabImage },
  { label: '부록 및 색인', image: tabImage }
];

const selectedTab = ref(0); // 기본 선택된 탭 설정
const currentPage = ref(0); // 현재 페이지 번호

// 페이지 내용 설정
const leftPages = [
  [
    "던전월드는 대화로 이루어집니다. 누군가가 무슨 말인가를 하고, 다른 사람이 여기에 응답을 하고, 때로는 또 다른 사람이 끼어듭니다. 던전월드의 대화는 캐릭터들이 사는 세상과 캐릭터들 주변에서 일어나는 일들, 즉 “이야기”를 중심으로 이루어집니다. 이 책의 룰이 대화에 끼어들어서 세상에 변화를 일으키기도 합니다. 던전월드에는 “턴”이나 “순서”가 없고, 누가 언제 말을 할 수 있는지에 관한 규칙도 없습니다. 대화는 그냥 자연스럽게, 일상생활의 대화처럼 일어납니다. 던전월드에서는 아무도 긴 독백을 하지 않습니다. 플레이는 항상 오가는 대화로 이루어진다는 점을 기억하십시오.",
    "데이터넣기.."
  ],
  [
    "능력치와 능력수정치",
    "많은 룰에서, 플레이어 캐릭터의 능력치와 능력수정치를 사용합니다. 능력치는 근력, 체력, 민첩성, 지능, 지혜, 매력의 여섯 가지이고, 3에서 18까지로 정의됩니다. 18은 사람으로서 최고 수준임을 가리킵니다."
  ],
  [
    "직업과 역할 내용 왼쪽1",
   "직업과 역할 내용 왼쪽2",
    "직업과 역할 내용 왼쪽3"
  ],
  [
    "게임 시스템 내용 왼쪽1",
    "게임 시스템 내용 왼쪽2"],
  [
    "게임 진행 내용 왼쪽1", 
    "게임 진행 내용 왼쪽2"],
  [
    "부록 및 색인 내용 왼쪽1", 
    "부록 및 색인 내용 왼쪽2"],
];

const rightPages = [
  [
    "이 책에 실린 룰은 그 대화에 영향을 줍니다. 이야기 속에서 뭔가 특정한 일이 일어날 때 룰이 발동되고, 그러고 나면 대화에 그 내용이 반영되는 것입니다.",
   "대화가 다 그렇듯, 말을 하는 것만큼이나 듣는 것도 중요합니다. 대화를 통해 플레이에서 확립된 사실들, 예를 들어 세계 설정이나 상황 묘사는 자기가 그 대화에 직접 끼지 않았다 해도 의미가 있습니다."
  ],
  [
    "판정을 할 때는 능력치에서 유래한 능력수정치가 사용됩니다. +근, +체, +민, +지, +혜, +매의 약자를 씁니다. 능력수정치는 -3에서 +3까지이고, 능력치를 따라서 정해집니다.", 
    "액션은 던전월드의 룰에서 가장 기본적인 단위입니다. 다음과 같은 형식으로 정의됩니다: 근거리 전투에서 적을 공격하면 +근 판정을 합니다. 10+이면 적에게 피해를 주고 자기는 공격을 피합니다."
  ],
  [
    "직업과 역할 내용 오른쪽1", 
    "직업과 역할 내용 오른쪽2",
    "직업과 역할 내용 오른쪽3"
  ],
  [
    "게임 시스템 내용 오른쪽1", 
    "게임 시스템 내용 오른쪽2"
  ],
  [
    "게임 환경 내용 오른쪽1", 
    "게임 환경 내용 오른쪽2"
  ],
  [
    "게임 진행 내용 오른쪽1", 
    "게임 진행 내용 오른쪽2"
  ],
  [
    "부록 및 색인 내용 오른쪽1", 
    "부록 및 색인 내용 오른쪽2"
  ]
];

const maxPage = computed(() => leftPages[selectedTab.value].length); // 선택된 탭의 최대 페이지 수 계산

const selectTab = (index) => {
  selectedTab.value = index;
  currentPage.value = 0;
};

const nextPage = () => {
  if (currentPage.value < maxPage.value - 1) {
    currentPage.value++;
  }
};

const prevPage = () => {
  if (currentPage.value > 0) {
    currentPage.value--;
  }
};

const modalStyle = computed(() => ({
  backgroundImage: `url(${rulebookImage})`,
  backgroundSize: 'contain',
  backgroundPosition: 'center',
  backgroundRepeat: 'no-repeat'
}));

const contentStyle = computed(() => ({
  display: 'flex',
  flexDirection: 'column',
  justifyContent: 'center',
  alignItems: 'center',
  height: '100%',
  overflow: 'hidden'
}));
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000; /* Ensure the overlay is on top */
}

.modal-content {
  position: relative;
  padding: 40px 20px;
  border-radius: 10px;
  max-width: 1050px;
  width: 90%;
  height: 90%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  background-size: contain;
  z-index: 1001; /* Ensure the modal content is on top of the overlay */
}

.modal-header h2 {
  margin: 0;
  font-size: 24px;
  color: #000;
}

.modal-body {
  flex-grow: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  position: relative;
}

.rulebook {
  display: flex;
  width: 100%;
  height: 100%;
  position: relative;
}

.tabs {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: flex-start;
  position: absolute;
  left: -140px; /* Adjust based on tab width */
  top: 50px; /* Adjust based on header height */
}

.tab {
  margin: 5px 0;
  cursor: pointer;
  position: relative;
  width: 100%;
  height: 20%;
}

.tab img {
  width: 100%;
  height: 100%;
}

.tab span {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  color: white;
  font-weight: bold;
  font-size: 14px;
  white-space: nowrap;
}

.page-content {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
  width: 100%;
  position: relative;
}

.page {
  width: 50%;
  text-align: center;
  padding: 0 10px;
}

.page.left {
  border-right: 1px solid #ccc;
}

.page p {
  margin: 0;
  font-size: 18px;
  color: #000;
}

.nav-button {
  position: absolute;
  top: 95%;
  transform: translateY(-50%)translateX(-20%);
  width: 30px;
  height: 30px;
  background-color: transparent;
  border: none;
  cursor: pointer;
}

.nav-button.prev {
  left: 10px;
}

.nav-button.next {
  right: 10px;
}

.modal-footer {
  margin-top: 20px;
}

button {
  padding: 10px 20px;
  font-size: 16px;
  background-color: #444;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

/* 페이지 전환 애니메이션 */
/* .page-flip-enter-active, .page-flip-leave-active {
  transition: transform 0.6s;
  transform-style: preserve-3d;
}

.page-flip-enter, .page-flip-leave-to {
  transform: rotateY(-180deg);
} */
</style>
