<template>
  <div class="character-info-modal" @click="close">
    <div class="modal-content" @click.stop>
      <div class="modal-header">
        <h2>상세 정보</h2>
        <button class="close-button" @click="close">X</button>
      </div>
      <div class="modal-body">
        <div class="left-section">
          <div class="character-image">
            <img :src="characterImage" alt="Character Image">
            <div class="level-badge">Lv.{{ level }}</div>
          </div>
          <div class="character-stats">
            <div class="stat">
              <span>HP</span>
              <div class="stat-bar">
                <div class="stat-bar-fill" :style="{ width: hp + '%' }"></div>
              </div>
            </div>
            <div class="stat" v-for="(stat, index) in otherStats" :key="index">
              <span>{{ stat.name }}</span>
              <div class="stat-bar">
                <div class="stat-bar-fill" :style="{ width: stat.value + '%' }"></div>
              </div>
            </div>
          </div>
        </div>
        <div class="right-section">
          <h3>도적</h3>
          <div v-for="(stat, index) in otherStats" :key="index" class="stat-detail">
            <span>{{ stat.name }}</span>
            <span>{{ stat.value }}</span>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button @click="close">닫기</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      characterImage: 'path/to/character-image.png',
      level: 2,
      hp: 75,
      otherStats: [
        { name: '매력', value: 8 },
        { name: '힘', value: 12 },
        { name: '민첩성', value: 10 },
        { name: '지능', value: 15 },
        { name: '체력', value: 10 },
        // 추가적인 스탯
      ]
    };
  },
  methods: {
    close() {
      this.$emit('close');
    }
  }
};
</script>

<style scoped>
.character-info-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: #3e2723;
  padding: 20px;
  border-radius: 8px;
  width: 600px;
  color: white;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.modal-header {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.close-button {
  background: none;
  border: none;
  color: white;
  font-size: 20px;
  cursor: pointer;
}

.modal-body {
  display: flex;
  width: 100%;
}

.left-section, .right-section {
  flex: 1;
  padding: 10px;
}

.character-image {
  position: relative;
  text-align: center;
}

.character-image img {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  border: 5px solid #6d4c41;
}

.level-badge {
  position: absolute;
  bottom: -10px;
  left: 50%;
  transform: translateX(-50%);
  background: #ff5722;
  border-radius: 12px;
  padding: 5px 10px;
  color: white;
  font-weight: bold;
}

.character-stats, .right-section {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.stat, .stat-detail {
  margin-bottom: 10px;
}

.stat-bar {
  width: 100%;
  background: #6d4c41;
  border-radius: 5px;
  overflow: hidden;
}

.stat-bar-fill {
  height: 10px;
  background: #ff5722;
}
</style>
