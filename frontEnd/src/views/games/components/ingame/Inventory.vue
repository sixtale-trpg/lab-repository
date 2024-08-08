<template>
  <div class="inventory-area" :style="backgroundStyle">
    <div class="inventory-grid">
      <div
        v-for="(item, index) in items"
        :key="index"
        class="inventory-slot"
        @click="openItemInfoModal(item)"
        @mouseover="showTooltip"
        @mouseleave="hideTooltip"
      >
        <template v-if="item">
          <img 
            :src="item.image" 
            :alt="item.name" 
            class="inventory-item"
          />
          <span class="item-count">{{ item.count }}</span>
          <button @click.stop="confirmDelete(item, index)" class="remove-item-button">
            <img :src="require('@/assets/images/ingame/Trash.png')" alt="Delete" class="delete" />
          </button>
          <div class="tooltip">클릭시 아이템 상세 정보를 볼 수 있습니다</div>
        </template>
      </div>
      <div v-if="items.length < maxSlots">
        <button @click="openAddItemModal" class="add-item-button">
          <img :src="require('@/assets/images/ingame/Plus2.png')" alt="Add" class="add" />
        </button>
      </div>
    </div>
    <div class="inventory-info">
      <div class="info-box weight-info" :style="infoBoxStyle">
        <img :src="require('@/assets/images/ingame/weight.png')" alt="weight-icon" class="gold-icon"> WEIGHT: {{ currentWeight }}/{{ limitWeight }}
      </div>
      <div class="info-box gold-info" :style="infoBoxStyle" @click="openGoldModal" @mouseover="showGoldTooltip" @mouseleave="hideGoldTooltip">
        <img :src="require('@/assets/images/ingame/Gold.png')" alt="Gold" class="gold-icon"> GOLD: {{ currentGold }}
        <div v-if="isGM" class="tooltip">클릭시 골드 변경이 가능합니다</div>
      </div>
    </div>
    <AddItemModal 
      v-if="isAddItemModalVisible" 
      @close="closeAddItemModal" 
      @select-item="addItem" 
      :current-weight="currentWeight" 
      :total-weight="limitWeight"
    />
    <ItemInfoModal 
      v-if="isItemInfoModalVisible" 
      :item="selectedItem" 
      :isGM="isGM" 
      :currentWeight="currentWeight" 
      :totalWeight="limitWeight"
      @close="closeItemInfoModal" 
      @update-item="updateItem"
    />
    <GoldModal 
      v-if="isGoldModalVisible" 
      :isVisible="isGoldModalVisible" 
      :isGM="isGM" 
      @close="closeGoldModal" 
      @update-gold="updateGold" 
      :current-gold="currentGold"
    />
    <ConfirmDeleteModal
      v-if="isConfirmDeleteModalVisible"
      :isVisible="isConfirmDeleteModalVisible"
      :item="selectedItem"
      @close="closeConfirmDeleteModal"
      @confirm="deleteItem"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue';
import { useRoute } from 'vue-router';
import { selectedPlayMemberID } from '@/store/state.js';
import { getInventory } from '@/common/api/InventoryAPI.js';
import AddItemModal from '@/views/games/components/Modal/AddItemModal.vue';
import ItemInfoModal from '@/views/games/components/Modal/ItemInfoModal.vue';
import GoldModal from '@/views/games/components/Modal/GoldModal.vue';
import ConfirmDeleteModal from '@/views/games/components/Modal/ConfirmDeleteModal.vue';

const route = useRoute();
const items = reactive([]);
const maxSlots = 18;
const currentWeight = ref(5);
const limitWeight = ref(11);
const currentGold = ref(7);
const selectedItem = ref(null);
const selectedIndex = ref(null);
const isGM = ref(true); // GM 여부 확인

const isAddItemModalVisible = ref(false);
const isItemInfoModalVisible = ref(false);
const isGoldModalVisible = ref(false);
const isConfirmDeleteModalVisible = ref(false);

const backgroundStyle = {
  backgroundImage: `url(${require('@/assets/images/ingame/Border4.png')})`,
  backgroundSize: '100% 100%',
  backgroundRepeat: 'no-repeat',
  backgroundPosition: 'center',
  display: 'flex',
  flexDirection: 'column',
  justifyContent: 'space-between',
  boxSizing: 'border-box',
  width: '100%',
  height: '100%',
};

const infoBoxStyle = {
  backgroundImage: `url(${require('@/assets/images/ingame/Border.png')})`,
  backgroundSize: '100% 100%',
  backgroundRepeat: 'no-repeat',
  backgroundPosition: 'center',
  padding: '5px',
  color: 'white',
  textAlign: 'center',
  fontSize: '0.7rem',
  margin: '0 5px',
  flex: '0 0 40%',
  boxSizing: 'border-box',
};

const fetchUserItems = async (playMemberID) => {
  try {
    const roomId = route.params.roomId;
    console.log(`Fetching inventory for roomID: ${roomId} and playMemberID: ${playMemberID}`);
    const inventory = await getInventory(roomId, playMemberID);
    items.splice(0, items.length, ...inventory);
    updateCurrentWeight();
  } catch (error) {
    console.error('Error fetching user items:', error);
  }
};

const updateCurrentWeight = () => {
  currentWeight.value = items.reduce((acc, item) => acc + (item.weight * item.count), 0);
};

watch(selectedPlayMemberID, (newID) => {
  if (newID) {
    fetchUserItems(newID);
  }
});

const openAddItemModal = () => {
  isAddItemModalVisible.value = true;
};

const closeAddItemModal = () => {
  isAddItemModalVisible.value = false;
};

const openItemInfoModal = (item) => {
  selectedItem.value = item;
  isItemInfoModalVisible.value = true;
};

const closeItemInfoModal = () => {
  isItemInfoModalVisible.value = false;
  selectedItem.value = null;
};

const openGoldModal = () => {
  if (isGM.value) {
    isGoldModalVisible.value = true;
  }
};

const closeGoldModal = () => {
  isGoldModalVisible.value = false;
};

const confirmDelete = (item, index) => {
  selectedItem.value = item;
  selectedIndex.value = index;
  isConfirmDeleteModalVisible.value = true;
};

const closeConfirmDeleteModal = () => {
  isConfirmDeleteModalVisible.value = false;
  selectedItem.value = null;
  selectedIndex.value = null;
};

const deleteItem = () => {
  if (selectedIndex.value !== null) {
    items.splice(selectedIndex.value, 1);
    updateCurrentWeight();
    closeConfirmDeleteModal();
  }
};

const addItem = (item) => {
  if (items.length < maxSlots) {
    items.push(item);
    updateCurrentWeight();
  }
};

const updateItem = (updatedItem) => {
  const itemIndex = items.findIndex(item => item.id === updatedItem.id);
  if (itemIndex !== -1) {
    items[itemIndex] = updatedItem;
    updateCurrentWeight();
  }
};

const updateGold = (newGoldAmount) => {
  currentGold.value = newGoldAmount;
};

const showTooltip = (event) => {
  const tooltip = event.target.closest('.inventory-slot').querySelector('.tooltip');
  if (tooltip) {
    tooltip.style.visibility = 'visible';
    tooltip.style.opacity = '1';
    const slotRect = event.target.closest('.inventory-slot').getBoundingClientRect();
    const tooltipRect = tooltip.getBoundingClientRect();
    const top = slotRect.top + slotRect.height + 5; // 아이템 슬롯 아래에 5px 간격
    const left = slotRect.left + (slotRect.width / 2) - (tooltipRect.width / 2); // 중앙 정렬
    tooltip.style.top = `${top}px`;
    tooltip.style.left = `${left}px`;
  }
};

const hideTooltip = (event) => {
  const tooltip = event.target.closest('.inventory-slot').querySelector('.tooltip');
  if (tooltip) {
    tooltip.style.visibility = 'hidden';
    tooltip.style.opacity = '0';
  }
};

const showGoldTooltip = (event) => {
  if (isGM.value) {
    const tooltip = event.target.closest('.info-box').querySelector('.tooltip');
    if (tooltip) {
      tooltip.style.visibility = 'visible';
      tooltip.style.opacity = '1';
      const slotRect = event.target.closest('.info-box').getBoundingClientRect();
      const tooltipRect = tooltip.getBoundingClientRect();
      const top = slotRect.top + slotRect.height + 5; // 골드 박스 아래에 5px 간격
      const left = slotRect.left + (slotRect.width / 2) - (tooltipRect.width / 2); // 중앙 정렬
      tooltip.style.top = `${top}px`;
      tooltip.style.left = `${left}px`;
    }
  }
};

const hideGoldTooltip = (event) => {
  if (isGM.value) {
    const tooltip = event.target.closest('.info-box').querySelector('.tooltip');
    if (tooltip) {
      tooltip.style.visibility = 'hidden';
      tooltip.style.opacity = '0';
    }
  }
};

onMounted(() => {
  if (selectedPlayMemberID.value) {
    fetchUserItems(selectedPlayMemberID.value);
  }
});
</script>

<style scoped>
html, body {
  overflow: hidden;
}

.inventory-area {
  display: flex;
  flex-direction: column;
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
  grid-template-rows: repeat(3, 1fr);
  gap: 5px;
  flex-grow: 1;
  width: 100%;
  max-height: 85%;
  overflow-y: auto;
  scrollbar-width: thin;
  scrollbar-color: #855e2fee #201805;
}

.inventory-grid::-webkit-scrollbar {
  width: 8px;
}

.inventory-grid::-webkit-scrollbar-track {
  background: #201805;
  border-radius: 5px;
}

.inventory-grid::-webkit-scrollbar-thumb {
  background-color: #855e2fee;
  border-radius: 5px;
  border: 2px solid #201805;
}

.inventory-info {
  display: flex;
  justify-content: space-evenly;
  align-items: center;
  padding: 5px 0;
  width: 100%;
  font-weight: bold;
  height: 15%;
}

.info-box {
  background-size: contain;
  background-repeat: no-repeat;
  text-align: center;
  padding: 5px;
  border-radius: 5px;
  color: white;
  box-sizing: border-box;
  flex: 1;
  position: relative;
}

.gold-icon, .weight-icon {
  width: 16px;
  height: 16px;
  margin-left: 5px;
  vertical-align: middle;
}

.inventory-slot {
  position: relative;
  width: 100%;
  padding-top: 100%;
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

.item-count {
  position: absolute;
  bottom: 5px;
  right: 5px;
  background: rgba(0, 0, 0, 0.7);
  color: white;
  border-radius: 3px;
  padding: 2px 5px;
  font-size: 0.8rem;
}

.remove-item-button {
  position: absolute;
  top: 2px;
  right: 2px;
  background: none;
  border: none;
  width: 20px;
  height: 20px;
  cursor: pointer;
}

.add-item-button {
  width: 100%;
  height: 100%;
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
  visibility: hidden;
  width: 150px;
  background-color: rgba(0, 0, 0, 0.8);
  color: #fff;
  text-align: center;
  border-radius: 3px;
  padding: 5px;
  position: fixed; /* 고정 위치 */
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
</style>
