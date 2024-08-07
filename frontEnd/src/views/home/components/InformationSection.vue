<template>
  <section class="information">
    <div class="container">
      <div class="header">
        <img src="@/assets/images/SixtaleLogo.png" alt="Site Logo" class="logo-image" />
        <h2>TRPG in Sixtale</h2>
      </div>
      <p class="sub-description">Sixtale은 여러분에게 다양한 지원을 제공합니다</p>
      <div class="features-grid">
        <div class="feature-item" v-for="feature in features" :key="feature.title">
          <div class="feature-icon-wrapper">
            <img :src="feature.icon" :alt="feature.title" class="feature-icon" />
          </div>
          <h3>{{ feature.title }}</h3>
          <p>{{ feature.description }}</p>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref } from 'vue';

const features = ref([
  {
    title: '화상채팅',
    icon: require('@/assets/images/information/video.png'),
    description: '플레이어들과 실시간으로 소통할 수 있는 화상채팅 기능을 제공합니다.'
  },
  {
    title: '로그저장',
    icon: require('@/assets/images/information/log.png'),
    description: '게임 진행 로그를 자동으로 저장하여 나중에 다시 볼 수 있습니다.'
  },
  {
    title: '구인시스템',
    icon: require('@/assets/images/information/recruitment.png'),
    description: '같이 게임을 즐길 새로운 동료를 쉽게 찾을 수 있는 구인 시스템.'
  },
  {
    title: '게임룰북제공',
    icon: require('@/assets/images/information/rule.png'),
    description: '다양한 게임 룰북을 제공하여 쉽게 접근할 수 있습니다.'
  },
  {
    title: '시나리오 창작',
    icon: require('@/assets/images/information/writing.png'),
    description: '자신만의 시나리오를 창작하고 공유할 수 있습니다.'
  },
  {
    title: '자동화시스템',
    icon: require('@/assets/images/information/automation.png'),
    description: '게임 진행을 도와주는 다양한 자동화 시스템을 제공합니다.'
  },
  {
    title: 'AI이미지 제공',
    icon: require('@/assets/images/information/image.png'),
    description: '게임에 필요한 이미지를 AI로 생성하여 제공합니다.'
  },
  {
    title: '음성텍스트화',
    icon: require('@/assets/images/information/sounds.png'),
    description: '음성을 텍스트로 변환하여 기록할 수 있습니다.'
  },
  {
    title: '챗봇도움말',
    icon: require('@/assets/images/information/chatbot.png'),
    description: '게임 진행 중 도움이 필요할 때 챗봇을 통해 도움말을 제공합니다.'
  }
]);
</script>

<style scoped>
.information {
  background-color: #091d31; 
  color: white;
  padding: 0; 
}

.container {
  background-color:  #031527;
  padding: 40px 20px; 
  border-radius: 0; 
  max-width: 1200px; 
  margin: 0 auto;
  text-align: center;
}

.header {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 20px;
  margin-top: 20px;
  margin-bottom: 20px;
}

.logo-image {
  max-width: 80px;
}

.header h2 {
  font-size: 2.5rem; 
  margin: 0;
}

.sub-description {
  font-size: 1.5rem; 
  margin-bottom: 40px;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 40px;
}

.feature-item {
  background-color: #112942;
  padding: 20px;
  border-radius: 10px;
  text-align: center;
}

.feature-icon-wrapper {
  background-color: white;
  border-radius: 50%;
  padding: 10px;
  display: inline-block;
  margin-bottom: 10px;
}

.feature-icon {
  max-width: 50px;
}

h3 {
  margin-bottom: 10px;
}
</style>
