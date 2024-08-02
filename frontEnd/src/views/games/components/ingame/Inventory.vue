<template>
  <div class="inventory-area" :style="backgroundStyle">
    <div class="inventory-grid">
      <div
        v-for="(item, index) in items"
        :key="index"
        class="inventory-slot"
      >
        <template v-if="item">
          <img :src="item.image" :alt="item.name" class="inventory-item" />
          <button @click="removeItem(index)" class="remove-item-button">
            <img :src="require('@/assets/images/ingame/Trash.png')" alt="Delete" class="delete" />
          </button>
        </template>
      </div>
      <div v-if="items.length < maxSlots">
        <button @click="showItemSelection" class="add-item-button">
          <img :src="require('@/assets/images/ingame/Plus.png')" alt="Add" class="add" />
        </button>
      </div>
    </div>
    <div class="inventory-info">
      <div class="info-box weight-info" :style="infoBoxStyle">
        <img :src="require('@/assets/images/ingame/weight.png')" alt="weight-icon" class="gold-icon"> WEIGHT: {{ currentWeight }}/{{ limitWeight }}
      </div>
      <div class="info-box gold-info" :style="infoBoxStyle">
        <img :src="require('@/assets/images/ingame/Gold.png')" alt="Gold" class="gold-icon"> GOLD: {{ currentGold }}
      </div>
      <button class="save-button" :style="saveButtonStyle">
        저장
      </button>
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
import { ref, reactive, onMounted, watch } from 'vue';
import { selectedPlayMemberID } from '@/store/state.js'; // 전역 상태에서 선택된 플레이어 ID 가져오기

const items = reactive([]);
const maxSlots = 18;
const currentWeight = ref(5);
const limitWeight = ref(11);
const currentGold = ref(7);
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
  flex: '0 0 30%',
  boxSizing: 'border-box',
};

const saveButtonStyle = {
  backgroundImage: `url(${require('@/assets/images/ingame/Save.png')})`,
  backgroundSize: 'contain',
  backgroundRepeat: 'no-repeat',
  backgroundPosition: 'center',
  flex: '0 0 20%',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  border: 'none',
  cursor: 'pointer',
  color: 'white',
  fontSize: '1rem',
  padding: '5px 10px',
  boxSizing: 'border-box',
  backgroundColor: 'transparent',
};

const itemSelectionVisible = ref(false);
const availableItems = ref([
  { id: 1, name: 'Sword', image: require('@/assets/images/ingame/Sword.png') },
  { id: 2, name: 'Helmet', image: require('@/assets/images/ingame/Helmet.png') },
]);

const fetchUserItems = async (playMemberID) => {
  // 임시 데이터
  const dummyItems = [
    [
      { id: 1, name: 'Sword', image: require('@/assets/images/ingame/Sword.png') },
      { id: 2, name: 'Shield', image: require('@/assets/images/ingame/Shield.png') },
    ],
    [
      { id: 3, name: 'Potion', image: require('@/assets/images/ingame/Potion.png') },
      { id: 4, name: 'Bow', image: require('@/assets/images/ingame/Bow.png') },
    ],
    [
      { id: 5, name: 'Arrow', image: require('@/assets/images/ingame/Arrow.png') },
      { id: 6, name: 'Magic Staff', image: require('@/assets/images/ingame/MagicStaff.png') },
    ],
    [
      { id: 7, name: 'Armor', image: require('@/assets/images/ingame/Armor.png') },
      { id: 8, name: 'Helmet', image: require('@/assets/images/ingame/Helmet.png') },
    ],
    [
      { id: 9, name: 'Ring', image: require('@/assets/images/ingame/Ring.png') },
      { id: 10, name: 'Amulet', image: require('@/assets/images/ingame/Amulet.png') },
    ],
    [
      { id: 11, name: 'Boots', image: require('@/assets/images/ingame/Boots.png') },
      { id: 12, name: 'Gloves', image: require('@/assets/images/ingame/Gloves.png') },
    ],
    [
      { id: 13, name: 'Torch', image: require('@/assets/images/ingame/Torch.png') },
      { id: 14, name: 'Map', image: require('@/assets/images/ingame/Maps.png') },
    ],
    [
      { id: 15, name: 'Rope', image: require('@/assets/images/ingame/Rope.png') },
      { id: 16, name: 'Lantern', image: require('@/assets/images/ingame/Lantern.png') },
    ],
  ];

  const userIndex = (playMemberID - 1) % dummyItems.length;
  items.splice(0, items.length, ...dummyItems[userIndex]);

  // 실제 API 요청 부분 (주석 처리)
  /*
  try {
    const response = await axios.get(`/rooms/${roomID}/sheets/${playMemberID}`);
    if (response.data.statusCode === 200) {
      const characterItems = response.data.data.characterEquipment.map(equipment => ({
        id: equipment.id,
        name: equipment.name,
        image: equipment.imageURL,
        description: equipment.description,
      }));
      items.splice(0, items.length, ...characterItems);
    }
  } catch (error) {
    console.error('Error fetching user items:', error);
  }
  */
};

watch(selectedPlayMemberID, (newID) => {
  if (newID) {
    fetchUserItems(newID);
  }
});

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

const confirmSave = () => {
  if (confirm("현재 아이템 정보를 저장하시겠습니까?")) {
    saveInventory();
  }
};

const saveInventory = async () => {
  // 실제 서버로 전송할 데이터 구조
  const payload = {
    characterEquipment: items.map(item => ({
      equipmentId: item.id,
      currentCount: item.count || 1, // 개수를 나타내는 속성 (가정)
      weight: item.weight || 0, // 무게 속성 (가정)
    })),
    currentWeight: currentWeight.value,
    currentMoney: currentGold.value,
    // 추가로 필요한 다른 데이터들...
  };

  console.log('Saving inventory with payload:', payload);

  // 실제 API 요청 부분 (주석 처리)
  /*
  try {
    const response = await axios.put(`/rooms/${roomID}/sheets/${selectedPlayMemberID.value}`, payload);
    if (response.data.statusCode === 200) {
      alert('저장되었습니다.');
    }
  } catch (error) {
    console.error('Error saving inventory:', error);
  }
  */
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
  justify-content: space-between;
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
}

.gold-icon, .weight-icon {
  width: 16px; 
  height: 16px; 
  margin-left: 5px; 
  vertical-align: middle; 
}

.save-button {
  display: flex;
  align-items: center;
  justify-content: center;
  background-size: contain;
  background-repeat: no-repeat;
  background-position: center;
  border: none;
  cursor: pointer;
  font-size: 1rem;
  padding: 5px 10px;
  box-sizing: border-box;
  background-color: transparent;
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
</style>
