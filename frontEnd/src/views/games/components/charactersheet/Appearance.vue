<template>
  <div class="appearance-container">
    <div class="content-wrapper">
      <div class="left-section">
        <div class="title-container">
          <img src="@/assets/images/character_sheet/avatar/Title.png" alt="Title" class="title-image">
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
            <button @click="generateImage" class="create-button">
              <img src="@/assets/images/character_sheet/avatar/Create_Button.png" alt="이미지 생성" />
              <span class="button-text">이미지 생성</span>
            </button>
            <div class="help-icon-container" 
              @mouseenter="showTooltip = true"
              @mouseleave="hideTooltip">
              <img
                src="@/assets/images/character_sheet/avatar/Help_Icon.png"
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
            <img src="@/assets/images/character_sheet/avatar/Create_Avatar_Frame.png" alt="Avatar Frame" class="frame-image">
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
            <img v-for="n in 3" :key="n" src="@/assets/images/character_sheet/avatar/Candidate_Frame.png" alt="Candidate Frame" class="candidate-frame">
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue';
import { handleGenerateImage } from '@/api';

const props = defineProps(['formData']);
const generatedImageUrl = ref('');
const previousImages = ref([]);

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

  // 화면 경계 확인
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
  try {
    const imageUrl = await handleGenerateImage(props.formData.appearanceDescription);
    generatedImageUrl.value = imageUrl;
    previousImages.value.push(imageUrl);
  } catch (error) {
    console.error('이미지 생성 중 오류 발생:', error);
    // 오류 처리 로직 추가
  }
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
}

.create-button img {
  width: 10vw;
  height: auto;
}

.button-text {
  position: absolute;
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

/* .help-icon-container:hover .tooltip {
  display: block;
} */

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
  /* border-radius: 50%; */
}

.generated-image {
  object-fit: cover;
  border-radius: 50%;
  width: 100%; /* 이미지를 부모 컨테이너에 맞게 조정 */
  height: 100%; /* 이미지를 부모 컨테이너에 맞게 조정 */
}

.candidate-frames {
  display: flex;
  justify-content: center;
  gap: 0.8vw;
}

.candidate-frame {
  width: 5vw;
  height: 5vw;
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
