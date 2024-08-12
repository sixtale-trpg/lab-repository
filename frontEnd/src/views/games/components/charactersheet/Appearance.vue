<template>
  <div class="appearance-container">
    <div class="content-wrapper">
      <div class="left-section">
        <div class="title-container">
          <img :src="titleImage" alt="Title" class="title-image">
          <span class="title-text">AI 캐릭터 만들기</span>
        </div>
        <div class="input-box">
          <textarea
            v-model="formData.appearanceDescription"
            placeholder="예시: 착한 눈, 붉은 머리, 강인한 입술, 금발 곱슬머리"
            class="appearance-input"
          ></textarea>
        </div>
        <div class="button-container">
          <div class="button-wrapper">
            <button 
              @mousedown="onMouseDown"
              @mouseup="onMouseUp"
              @mouseleave="onMouseLeave"
              @click="generateImage" 
              class="create-button"
              :disabled="remainingClicks === 0 || isLoading"
              :class="{ 'not-allowed': remainingClicks === 0 || isLoading }"
            >
              <img 
                :src="remainingClicks > 0 ? createButtonImage : exhaustedImage" 
                :class="{ 'active': isButtonActive }"
                alt="이미지 생성" 
              />
              <div class="button-content">
                <div v-if="isLoading" class="loader"></div>
                <span class="button-text">
                  {{ isLoading ? '생성 중...' : `이미지 생성 ${remainingClicks}` }}
                </span>
              </div>
            </button>
            <div class="help-icon-container" 
              @mouseenter="showTooltip = true"
              @mouseleave="hideTooltip">
              <img
                :src="helpIconImage"
                alt="도움말"
                class="help-icon"
              />
            </div>
            <Teleport to="body">
              <div ref="tooltipRef" class="tooltip" :style="tooltipStyle" v-if="showTooltip">
                입력한 텍스트를 기반으로 초상화를 그려드립니다. 창의력을 발휘하여 당신의 의도를 드러나게 해 보세요. 생성 기능은 임시입니다. 다시 생성 시 지정되지 않은 신청서가 리셋됩니다.
              </div>
            </Teleport>
          </div>
        </div>
      </div>
      <div class="right-section">
        <div class="image-and-candidates">
          <div class="image-frame">
            <img :src="avatarFrameImage" alt="Avatar Frame" class="frame-image">
            <div class="image-container">
              <img
                v-if="generatedImageUrl"
                :src="generatedImageUrl"
                alt="생성된 캐릭터 이미지"
                class="generated-image"
              />
            </div>
          </div>
          <div class="candidate-frames">
            <div 
              v-for="(candidateImage, index) in candidateImages" 
              :key="index" 
              class="candidate-frame-wrapper"
              :class="{ 'has-image': candidateImage }"
              @click="selectImage(candidateImage)"
            >
              <img 
                :src="candidateImage ? candidateImage : candidateFrameImage" 
                alt="Candidate Frame" 
                class="candidate-frame"
              >
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue';
import { handleGenerateImage } from '@/common/api/ImageAiAPI';

// 이미지 경로 import
import titleImage from '@/assets/images/character_sheet/avatar/Title.png';
import createButtonImage from '@/assets/images/character_sheet/avatar/Create_Button.png';
import exhaustedImage from '@/assets/images/character_sheet/avatar/Exhausted.png';
import helpIconImage from '@/assets/images/character_sheet/avatar/Help_Icon.png';
import avatarFrameImage from '@/assets/images/character_sheet/avatar/Create_Avatar_Frame.png';
import candidateFrameImage from '@/assets/images/character_sheet/avatar/Candidate_Frame.png';

const props = defineProps(['formData']);
const generatedImageUrl = ref('');
const previousImages = ref([]);
const remainingClicks = ref(3);  // 남은 클릭 수
const isButtonActive = ref(false);  // 버튼 활성화 상태
const isLoading = ref(false); // 로딩 상태

// 후보 액자에 사용할 이미지 목록
const candidateImages = reactive([null, null, null]);

const showTooltip = ref(false);
const tooltipStyle = reactive({
  top: '0px',
  left: '0px'
});

const tooltipRef = ref(null);

const updateTooltipPosition = (event) => {
  if (!tooltipRef.value) return;
  
  const rect = tooltipRef.value.getBoundingClientRect();
  const x = event.clientX + 20;
  const y = event.clientY + 20;

  const maxX = window.innerWidth - rect.width;
  const maxY = window.innerHeight - rect.height;

  tooltipStyle.left = `${Math.min(x, maxX)}px`;
  tooltipStyle.top = `${Math.min(y, maxY)}px`;
  showTooltip.value = true;
};

const hideTooltip = () => {
  showTooltip.value = false;
};

const generateImage = async () => {
  if (remainingClicks.value > 0 && !isLoading.value) {
    isLoading.value = true; // 로딩 상태 시작
    try {
      const imageUrl = await handleGenerateImage(props.formData.appearanceDescription);
      generatedImageUrl.value = imageUrl;

      if (candidateImages[0] === null) {
        candidateImages[0] = imageUrl;
      } else if (candidateImages[1] === null) {
        candidateImages[1] = imageUrl;
      } else if (candidateImages[2] === null) {
        candidateImages[2] = imageUrl;
      } else {
        console.log('모든 후보 액자가 이미 채워졌습니다.');
      }

      previousImages.value.push(imageUrl);
      remainingClicks.value -= 1;  // 클릭 횟수 감소
    } catch (error) {
      console.error('이미지 생성 중 오류 발생:', error);
    } finally {
      isLoading.value = false; // 로딩 상태 종료
    }
  }
};

const onMouseDown = () => {
  if (!isLoading.value && remainingClicks.value > 0) {
    isButtonActive.value = true;
  }
};

const onMouseUp = () => {
  isButtonActive.value = false;
};

const onMouseLeave = () => {
  isButtonActive.value = false;
};

const selectImage = (image) => {
  generatedImageUrl.value = image;
};

onMounted(() => {
  document.addEventListener('mousemove', updateTooltipPosition);
});

onUnmounted(() => {
  document.removeEventListener('mousemove', updateTooltipPosition);
});
</script>

<style scoped>
.appearance-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  color: #fff;
  padding: 2vh 2vw;
  overflow: hidden;
}

.content-wrapper {
  display: flex;
  flex: 1;
  gap: 2vw;
  height: 100%;
}

.left-section, .right-section {
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 3vh;
}

.left-section {
  flex: 1;
  align-items: center;
}

.right-section {
  flex: 1;
  align-items: center;
  justify-content: center;
}

.image-and-candidates {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2vh;
}

.title-container {
  position: relative;
  width: 70%;
  display: flex;
  justify-content: center;
  align-items: center;
  padding-bottom: 5%;
}

.title-image {
  width: 70%;
  height: auto;
  display: block;
}

.title-text {
  position: absolute;
  font-size: 1.2vw;
  color: #fff;
}

.input-box {
  width: 80%;
  background-color: #1A130E;
  border: 1px solid #4A3A2E;
  border-radius: 10px;
  padding: 0.8vw;
}

.appearance-input {
  width: 100%;
  height: 12vh;
  background: transparent;
  border: none;
  color: #fff;
  resize: none;
  font-size: 0.9vw;
}

.button-container {
  display: flex;
  justify-content: center;
  width: 100%;
}
.button-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}
.create-button, .help-icon {
  background: none;
  border: none;
  cursor: pointer;
}

.create-button {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  background: none;
  border: none;
  cursor: pointer;
  width: 10vw;
  height: auto;
}

.button-content {
  position: absolute;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
}

.create-button.not-allowed {
  cursor: not-allowed; /* 클릭 불가 상태에서의 커서 모양 */
}

.create-button img {
  width: 100%; /* 버튼의 크기에 맞게 조정 */
  height: auto;
}

.create-button img.active {
  filter: brightness(0.8);
  transition: filter 0.1s ease;
}

.loader {
  width: 20px;
  height: 20px;
  border: 2px solid #f3f3f3;
  border-top: 2px solid #3498db;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-right: 10px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.button-text {
  color: #fff;
  font-size: 1vw;
  text-shadow: 1px 1px 2px rgba(0,0,0,0.8);
}

.help-icon-container {
  position: absolute;
  right: -3vw;
  top: 50%;
  transform: translateY(-50%);
}

.help-icon {
  width: 2.5vw;
  height: 2.5vw;
  cursor: default;
}

.tooltip {
  position: fixed;
  width: 18vw;
  background-color: #1A130E;
  border: 1px solid #4A3A2E;
  border-radius: 5px;
  padding: 0.8vw;
  z-index: 9999;
  font-size: 0.7vw;
  pointer-events: none;
  color: #fff;
  box-shadow: 0 2px 10px rgba(0,0,0,0.5);
}

.image-frame {
  position: relative;
  width: 60%;
  padding-bottom: 60%;
}

.frame-image, .generated-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.frame-image {
  z-index: 2;
}
.image-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.generated-image {
  object-fit: cover;
  border-radius: 50%;
  width: 100%;
  height: 100%;
}

.candidate-frames {
  display: flex;
  justify-content: center;
  gap: 0.8vw;
}

.candidate-frame-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 5vw;
  height: 5vw;
  position: relative;
  cursor: pointer;
  transition: transform 0.2s ease-in-out;
}

.candidate-frame-wrapper.has-image {
  border: 3px solid #915B33;
  border-radius: 50%;
  padding: 2px;
}

.candidate-frame {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 50%;
  overflow: hidden;
  transition: transform 0.2s ease-in-out;
}

.candidate-frame-wrapper:hover {
  transform: scale(1.1); /* 호버 시 이미지와 액자가 살짝 커지도록 설정 */
}

.candidate-frame-wrapper:not(.has-image) .candidate-frame {
  border-radius: 0;
  object-fit: contain;
}

@media (max-width: 1024px) {
  .title-text {
    font-size: 1.8vw;
  }

  .appearance-input {
    font-size: 1.3vw;
  }

  .button-text {
    font-size: 1.5vw;
  }

  .create-button img {
    width: 15vw;
  }

  .help-icon {
    width: 4vw;
    height: 4vw;
  }

  .image-frame {
    width: 70%;
    padding-bottom: 70%;
  }

  .candidate-frame {
    width: 7vw;
    height: 7vw;
  }
}

@media (max-width: 768px) {
  .content-wrapper {
    flex-direction: column;
  }

  .title-container {
    width: 90%;
  }

  .title-text {
    font-size: 2.5vw;
  }

  .appearance-input {
    height: 15vh;
    font-size: 1.8vw;
  }

  .button-text {
    font-size: 2.2vw;
  }

  .create-button img {
    width: 22vw;
  }

  .help-icon-container {
    right: -4vw;
  }

  .help-icon {
    width: 5vw;
    height: 5vw;
  }

  .tooltip {
    width: 40vw;
    font-size: 1.8vw;
  }

  .image-frame {
    width: 80%;
    padding-bottom: 80%;
  }

  .candidate-frame {
    width: 12vw;
    height: 12vw;
  }
}
</style>