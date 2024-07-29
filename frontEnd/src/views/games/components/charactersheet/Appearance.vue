<template>
  <div class="image-generator-container">
    <div class="header">
      <img :src="nicknameLightImage" alt="이미지 생성" class="header-image">
      <span class="header-text">이미지 생성</span>
    </div>
    <div class="content">
      <div class="input-container">
        <img :src="promptImage" alt="설명 입력 배경" class="input-image">
        <textarea v-model="formData.appearanceDescription" class="input-textarea" placeholder="예시: 검은색 두건, 푸른 눈, 오뚝한 코, 구릿빛 피부, 야비한 생김새"></textarea>
        <div class="hover-description">
          <img :src="imgDescriptionImage" alt="이미지 설명" class="description-image">
          <div class="description-text-container">
            <p class="description-text">입력한 텍스트를 기반으로 초상화를 그려드립니다. 창의력을 발휘하여 당신의 의도를 드러나게 해 보세요. 생성 기능은 임시입니다. 다시 생성 시 지정되지 않은 신청서가 리셋됩니다.</p>
          </div>
        </div>
      </div>
      <div class="circle-container">
        <img :src="circleImage" alt="이미지 프레임" class="circle-image">
      </div>
    </div>
    <div class="button-container">
      <button class="create-button" :style="{ backgroundImage: 'url(' + createButtonImage + ')' }">이미지 생성</button>
    </div>
  </div>
</template>

<script setup>
import { ref, toRefs } from 'vue';

const props = defineProps(['formData']);
const { formData } = toRefs(props);

const nicknameLightImage = ref(require('@/assets/images/character_sheet/nickname_light.png'));
const promptImage = ref(require('@/assets/images/character_sheet/prompt.png'));
const imgDescriptionImage = ref(require('@/assets/images/character_sheet/img_description.png'));
const createButtonImage = ref(require('@/assets/images/character_sheet/create_button.png'));
const circleImage = ref(require('@/assets/images/character_sheet/create_avatar.png'));
</script>

<style scoped>
.image-generator-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  color: #fff;
  border-radius: 10px;
  width: 80%;
  margin: auto;
}

.header {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 20px;
  position: relative;
}

.header-image {
  width: 300px;
  height: 50px;
}

.header-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 1.5rem;
  font-weight: bold;
  color: #fff;
}

.content {
  display: flex;
  justify-content: space-between;
  width: 100%;
  margin-bottom: 20px;
  position: relative;
}

.input-container {
  position: relative;
  width: 60%;
}

.input-image {
  width: 100%;
  height: 400px;
  border-radius: 10px;
}

.input-textarea {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  padding: 20px;
  border: none;
  border-radius: 10px;
  background: transparent;
  color: #fff;
  font-size: 1rem;
  resize: none;
}

.hover-description {
  position: absolute;
  bottom: 0;
  right: 0;
  transform: translate(100%, 100%);
  width: 300px;
  height: 200px;
  display: none;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.input-container:hover .hover-description {
  display: flex;
}

.description-image {
  width: 100%;
  height: 100%;
  border-radius: 10px;
  position: relative;
}

.description-text-container {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 90%;
}

.description-text {
  font-size: 0.8rem;
  color: #fff;
  text-align: center;
  padding: 0 10px;
}

.circle-container {
  position: relative;
  width: 35%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.circle-image {
  width: 100%;
  height: auto;
}

.button-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.create-button {
  width: 150px;
  height: 50px;
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
  border: none;
  cursor: pointer;
  font-size: 1rem;
  color: #fff;
  text-align: center;
  line-height: 50px;
  border-radius: 5px;
}

.create-button:hover {
  transform: scale(1.05);
}
</style>
