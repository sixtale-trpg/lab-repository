<template>
  <div class="inventory-area" :style="backgroundStyle">
    <div class="inventory-grid">
      <div v-for="(item, index) in items" :key="index" class="inventory-slot">
        <template v-if="item">
          <img :src="item.image" :alt="item.name" class="inventory-item" />
          <button @click="removeItem(index)" class="remove-item-button">
            <img :src="require('@/assets/images/ingame/Trash.png')" alt="Delete" class="delete" />
          </button>
          <div class="tooltip" v-if="tooltip.visible" :style="tooltipStyle">{{ tooltip.text }}</div>
        </template>
      </div>
      <div v-if="items.length < maxSlots">
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';

const characterId = 1;
const items = reactive([]);
const maxSlots = 12;

const currentWeight = ref(5);
const limitWeight = ref(11);
const currentGold = ref(7);

const backgroundStyle = {
  backgroundImage: `url(${require('@/assets/images/ingame/Border4.png')})`,
  backgroundSize: 'cover',
  backgroundRepeat: 'no-repeat',
  backgroundPosition: 'center',
  display: 'flex',
  justifyContent: 'space-between',
  padding: '10px',
  boxSizing: 'border-box',
  width: '100%',
  height: '100%',
};

const tooltip = ref({ visible: false, text: '' });
const tooltipStyle = ref({ top: '0px', left: '0px' });

const showTooltip = (item) => {
  tooltip.value = { visible: true, text: item.name + (item.count > 1 ? ` (${item.count})` : '') };
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

onMounted(() => {
  items.push(
    { id: 1, name: 'Shield', image: require('@/assets/images/ingame/Shield.png'), count: 1 },
    { id: 2, name: 'Armor', image: require('@/assets/images/ingame/Armor.png'), count: 15 },
    { id: 3, name: 'Sword', image: require('@/assets/images/ingame/Sword.png'), count: 1 },
    { id: 4, name: 'Helmet', image: require('@/assets/images/ingame/Helmet.png'), count: 1 }
  );
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
  gap: 5px;
  width: 80%;
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
  padding-top: 100%;
  border: 1px solid #564307;
  box-sizing: border-box;
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
  bottom: 100%;
  left: 50%;
  transform: translateX(-50%);
  background-color: black;
  color: white;
  padding: 5px;
  border-radius: 3px;
  white-space: nowrap;
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
</style>
