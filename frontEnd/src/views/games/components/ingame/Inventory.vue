<template>
  <div class="inventory-area" :style="backgroundStyle">
    <div class="inventory-grid">
      <div
        v-for="(item, index) in items"
        :key="index"
        class="inventory-slot"
        @mouseover="showTooltip(item, $event)"
        @mouseleave="hideTooltip"
      >
        <template v-if="item">
          <img :src="item.image" :alt="item.name" class="inventory-item" />
          <button @click="removeItem(index)" class="remove-item-button">
            <img :src="require('@/assets/images/ingame/Trash.png')" alt="Delete" class="delete" />
          </button>
        </template>
      </div>
      <div v-if="items.length < 18">
        <button @click="showItemSelection" class="add-item-button">
          <img :src="require('@/assets/images/ingame/Plus.png')" alt="Add" class="add" />
        </button>
      </div>
    </div>
    <div class="inventory-info">
      <div class="info-box weight-info">WEIGHT: {{ currentWeight }}/{{ limitWeight }}</div>
      <div class="info-box gold-info">GOLD: {{ currentGold }}</div>
    </div>
    <div v-if="itemSelectionVisible" class="item-selection-modal">
      <div class="item-selection-content">
        <h3>아이템 선택</h3>
        <div v-for="availableItem in availableItems" :key="availableItem.id" class="available-item" @click="selectItem(availableItem)">
          <img :src="availableItem.image" :alt="availableItem.name" />
          <span>{{ availableItem.name }}</span>
        </div>
        <button @click="closeItemSelection">닫기</button>
      </div>
    </div>
    <div v-if="tooltip.visible" class="tooltip" :style="tooltipStyle">{{ tooltip.text }}</div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
// import axios from 'axios';  // 실제 API 호출 시 사용

const items = reactive([]);
const maxSlots = 18;  // 3x6 그리드에 맞춘 슬롯 수
const currentWeight = ref(5);
const limitWeight = ref(11);
const currentGold = ref(7);
const backgroundStyle = {
  backgroundImage: `url(${require('@/assets/images/ingame/Border4.png')})`,
  backgroundSize: '100% 100%',
  backgroundRepeat: 'no-repeat',
  backgroundPosition: 'center',
  display: 'flex',
  justifyContent: 'space-between',
  gap: '5px',
  padding: '10px',
  margin: '5px',
  boxSizing: 'border-box',
  width: '100%',
  height: '100%',
};

const tooltip = ref({ visible: false, text: '' });
const tooltipStyle = ref({ top: '0px', left: '0px' });

const showTooltip = (item, event) => {
  tooltip.value = { visible: true, text: item.description };
  tooltipStyle.value = {
    top: `${event.clientY}px`,
    left: `${event.clientX}px`
  };
};

const hideTooltip = () => {
  tooltip.value.visible = false;
};

const itemSelectionVisible = ref(false);
const availableItems = ref([
  { id: 1, name: 'Sword', image: require('@/assets/images/ingame/Sword.png') },
  { id: 2, name: 'Helmet', image: require('@/assets/images/ingame/Helmet.png') },
]);

const showItemSelection = () => {
  itemSelectionVisible.value = true;
};

const selectItem = (availableItem) => {
  if (items.length < maxSlots) {
    items.push({ ...availableItem, count: 1 });
  }
  itemSelectionVisible.value = false;
};

const closeItemSelection = () => {
  itemSelectionVisible.value = false;
};

const removeItem = (index) => {
  items.splice(index, 1);
};


// 인벤토리 설명 호버 미구현 => 방법 잘 모르겠음
// 캐릭터 시트의 호버는 미리 설명을 입력해두고 display:none 으로 해서 보여주고 안보여주고 했는데
// 이상한거 같아서 함수를 정의하고 했는데 미구현됨

onMounted(async () => {
  try {
    // 주석 처리된 백엔드 API 호출 부분
    // const response = await axios.get(`/rooms/${roomID}/sheets/${playMemberID}`);
    // const characterData = response.data;
    // items.push(...characterData.equipment);
    // currentWeight.value = characterData.current_weight;
    // limitWeight.value = characterData.limit_weight;
    // currentGold.value = characterData.current_money;
    
    // 더미 데이터
    items.push(
      { id: 1, name: 'Shield', image: require('@/assets/images/ingame/Shield.png'), count: 1, description: 'A sturdy shield for protection.' },
      { id: 2, name: 'Armor', image: require('@/assets/images/ingame/Armor.png'), count: 15, description: 'Protective armor made of metal.' },
      { id: 3, name: 'Sword', image: require('@/assets/images/ingame/Sword.png'), count: 1, description: 'A sharp sword for combat.' },
      { id: 4, name: 'Helmet', image: require('@/assets/images/ingame/Helmet.png'), count: 1, description: 'A helmet to protect your head.' }
    );
  } catch (error) {
    console.error('Error loading character data:', error);
  }
});
</script>

<style scoped>
.inventory-area {
  display: flex;
  padding: 10px;
  box-sizing: border-box;
  width: 100%;
  height: 100%;
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
}

.inventory-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  grid-template-rows: repeat(3, 1fr); /* 3x6 그리드 */
  gap: 5px;
  width: 80%;
  max-height: 100%; /* 부모 영역을 넘지 않도록 설정 */
  overflow-y: auto; /* 스크롤바가 필요 시 나타나도록 설정 */
  scrollbar-width: thin; /* Firefox용 */
  scrollbar-color: #110519 #a56722; /* Firefox용 */
}

.inventory-info {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 10px;
  width: 20%;
}

.info-box {
  text-align: center;
  background-color: #564307;
  color: white;
  padding: 5px;
  border-radius: 3px;
  width: 100%;
}

.inventory-slot {
  position: relative;
  width: 100%;
  padding-top: 100%; /* 1:1 비율을 유지 */
  border: 1px solid #564307;
  box-sizing: border-box;
  overflow: hidden;
}

.inventory-item {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.remove-item-button {
  position: absolute;
  top: 2px;
  right: 2px;
  background: red;
  color: white;
  border: none;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  cursor: pointer;
}

.add-item-button {
  width: 60px;
  height: 60px;
  background: none;
  border: none;
  cursor: pointer;
}

.add-item-button img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.tooltip {
  position: absolute;
  background-color: black;
  color: white;
  padding: 5px;
  border-radius: 3px;
  white-space: nowrap;
  z-index: 1000;
  transform: translate(-50%, -100%);
}

.item-selection-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 3;
}

.item-selection-content {
  background-color: white;
  padding: 20px;
  border-radius: 5px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.available-item {
  display: flex;
  align-items: center;
  cursor: pointer;
  margin-bottom: 10px;
}

.available-item img {
  width: 40px;
  height: 40px;
  margin-right: 10px;
}

/* Chrome, Edge, Safari 스크롤바 커스터마이징 */
.inventory-grid::-webkit-scrollbar {
  width: 8px;
}

.inventory-grid::-webkit-scrollbar-track {
  background: #a56722;
  border-radius: 5px;
}

.inventory-grid::-webkit-scrollbar-thumb {
  background-color: #274e13;
  border-radius: 5px;
  border: 1px solid #a56722;
}
</style>
