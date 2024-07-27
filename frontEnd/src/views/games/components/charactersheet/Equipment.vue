<template>
  <div class="equipment-selection-container">
    <p>선호하는 무기를 선택하십시오</p>
    <div class="equipment-buttons">
      <div class="equipment-category">
        <div class="title-container">
          <img src="@/assets/images/character_sheet/nickname_light.png" alt="근거리 무기 선택" class="title-image">
          <span class="title-text">근거리 무기 선택</span>
        </div>
        <div 
          v-for="(button, index) in closeRangeWeapons" 
          :key="index" 
          class="equipment-button" 
          @click="selectCloseRangeWeapon(index)"
          :class="{ active: selectedCloseRangeWeapon === index }"
          :style="{ backgroundImage: `url(${button.image})` }"
        >
          <span>{{ button.title }}</span>
        </div>
      </div>
      <div class="equipment-category">
        <div class="title-container">
          <img src="@/assets/images/character_sheet/nickname_light.png" alt="원거리 무기 선택" class="title-image">
          <span class="title-text">원거리 무기 선택</span>
        </div>
        <div 
          v-for="(button, index) in longRangeWeapons" 
          :key="index" 
          class="equipment-button" 
          @click="selectLongRangeWeapon(index)"
          :class="{ active: selectedLongRangeWeapon === index }"
          :style="{ backgroundImage: `url(${button.image})` }"
        >
          <span>{{ button.title }}</span>
        </div>
      </div>
      <div class="equipment-category">
        <div class="title-container">
          <img src="@/assets/images/character_sheet/nickname_light.png" alt="다음 중 하나 선택" class="title-image">
          <span class="title-text">다음 중 하나 선택</span>
        </div>
        <div 
          v-for="(button, index) in miscellaneous" 
          :key="index" 
          class="equipment-button" 
          @click="selectMiscellaneous(index)"
          :class="{ active: selectedMiscellaneous === index }"
          :style="{ backgroundImage: `url(${button.image})` }"
        >
          <span>{{ button.title }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';

// 버튼 정보 및 스타일 등록
const closeRangeWeapons = reactive([
  { title: '단도 (반검용, 무게 1)', image: require('@/assets/images/character_sheet/equip1.png') },
  { title: '레이피어 (한검용, 장검, 무게 1)', image: require('@/assets/images/character_sheet/equip2.png') },
]);

const longRangeWeapons = reactive([
  { title: '투척용 단검 세 자루 (투척, 중거리, 무게 0)', image: require('@/assets/images/character_sheet/equip1.png') },
  { title: '낡은 활 (중거리, 무게 1), 화살 한 다발 (발사, 무게 1)', image: require('@/assets/images/character_sheet/equip2.png') },
]);

const miscellaneous = reactive([
  { title: '모험 장비 (무게 1)', image: require('@/assets/images/character_sheet/equip1.png') },
]);

const selectedCloseRangeWeapon = ref(null);
const selectedLongRangeWeapon = ref(null);
const selectedMiscellaneous = ref(null);

const selectCloseRangeWeapon = (index) => {
  selectedCloseRangeWeapon.value = index;
};

const selectLongRangeWeapon = (index) => {
  selectedLongRangeWeapon.value = index;
};

const selectMiscellaneous = (index) => {
  selectedMiscellaneous.value = index;
};
</script>

<style scoped>
.equipment-buttons {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px; /* 간격 조정 */
}

.equipment-category {
  margin-bottom: 15px; /* 간격 조정 */
}

.title-container {
  position: relative;
  display: inline-block;
  margin-bottom: 10px; /* 간격 조정 */
}

.title-image {
  width: 300px; /* 너비 조정 */
  height: 50px; /* 높이 조정 */
  display: block;
}

.title-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: #fff;
  font-size: 1rem; /* 폰트 크기 조정 */
  white-space: nowrap;
}

.equipment-button {
  background-size: cover;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  cursor: pointer;
  margin-bottom: 10px; /* 간격 조정 */
  padding: 5px; /* 패딩 조정 */
  font-size: 0.8rem; /* 폰트 크기 조정 */
  width: 350px; /* 너비 조정 */
  height: 50px; /* 높이 조정 */
  border-radius: 10px; /* 테두리 반경 조정 */
  position: relative;
  transition: transform 0.3s, box-shadow 0.3s; /* 애니메이션 추가 */
}

.equipment-button.active {
  transform: scale(1.05); /* 크기 조정 */
  box-shadow: 0 0 5px rgba(255, 255, 255, 0.8); /* 그림자 추가 */
}
</style>
