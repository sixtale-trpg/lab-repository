<template>
  <div class="action-container">
    <div class="action-buttons">
      <div 
        v-for="(action, index) in actions" 
        :key="index" 
        class="action-button"
        :style="getActionStyle(action)"
        :class="{ active: selectedIndex === index }"
        @click="handleClick(index)"
      >
        <span class="action-title">{{ action.title }}</span>
        <div class="action-content">
          <p v-for="(line, lineIndex) in action.content" :key="lineIndex">{{ line }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import actionBox from '@/assets/images/character_sheet/action_box.png';

// 액션 정보 등록
const actions = reactive([
  {
    title: '덫 전문가',
    content: [
      '위험한 지역을 잠시 살피면, 민 판정을 합니다. 10+이면 예비 3점을, 7~9이면 예비 1점을 받습니다. 그 지역을 지나는 동안 말할 때 언제나 예비를 소비하여 다음 질문을 할 수 있습니다 (예비 1점마다 질문 하나씩):',
      ' • 여기에 덫이 있는가? 있다면 어떻게 발동되는가?',
      ' • 발동되면 무슨 일이 일어나는가?',
      ' • 그 외에 숨겨진 것이 있는가?'
    ]
  },
  {
    title: '프로의 솜씨',
    content: [
      '자물쇠를 따거나 덫을 해체할 때, 민 판정을 합니다. 10+이면 아무 문제 없이 해냅니다. 7~9에도 성공은 하지만, 의심, 위험, 대가 중 두 가지를 마스터가 제시할 것입니다.'
    ]
  },
  {
    title: '암습',
    content: [
      '방어를 할 수 없거나 기술을 안한 적 근거리 무기로 공격할 때, 피해를 주지 않고 대신, 민 판정을 할 수 있습니다. 10+이면 다음 중 둘을 고릅니다. 7~9이면 하나만 고릅니다.',
      ' • 상대의 근거리 전투에 말려들지 않습니다.',
      ' • 통상적인 피해 +1d4을 줍니다.',
      ' • 유리한 상황을 만듭니다. 자기 자신, 또는 그 상황을 이용하는 우리 편이 다음 판정에 +1을 받습니다.',
      ' • 상대가 갖춘 수비를 깁까지 방어가 -1을 받습니다.'
    ]
  },
  {
    title: '희박한 도덕관념',
    content: [
      '누군가가 가진 것을 탐지하며 함 경우, 원하면 거짓으로 가르쳐 주어도 됩니다.'
    ]
  }
]);

const selectedIndex = ref(null);

const getActionStyle = (action) => {
  const baseHeight = 150; // 기본 높이
  const additionalHeight = action.content.length * 30; // 내용 길이에 따른 추가 높이
  const height = baseHeight + additionalHeight;

  return {
    backgroundImage: `url(${actionBox})`,
    width: '100%', // 너비를 100%로 설정
    height: `${height}px`,
    borderRadius: '15px',
    backgroundSize: 'cover',
    backgroundRepeat: 'no-repeat',
    backgroundPosition: 'center',
    padding: '20px',
    color: '#fff',
    textAlign: 'left',
    overflow: 'hidden', // 텍스트가 박스를 벗어나지 않도록 숨기기
    boxSizing: 'border-box'
  };
};

const handleClick = (index) => {
  selectedIndex.value = index;
};
</script>

<style scoped>
.action-container {
  padding: 20px;
  border-radius: 10px;
  color: #fff;
  width: 80%;
  margin: auto;
  text-align: center;
}

.action-buttons {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-top: 20px;
}

.action-button {
  position: relative;
  padding: 20px;
  overflow: hidden; /* 박스를 넘치는 텍스트를 숨기기 */
  transition: transform 0.3s, box-shadow 0.3s; /* 애니메이션 추가 */
}

.action-button.active {
  transform: scale(1.05); /* 크기 조정 */
  box-shadow: 0 0 10px rgba(255, 255, 255, 0.8); /* 그림자 추가 */
}

.action-title {
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 10px;
}

.action-content {
  font-size: 1.2rem;
  line-height: 1.5;
  max-height: calc(100% - 40px); /* 제목의 높이를 제외한 높이 */
  overflow-y: auto; /* 텍스트가 넘치면 스크롤바 표시 */
}
</style>
