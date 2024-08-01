<template>
  <header class="main-header" :style="{ height: height }">
    <div class="logo" @click="goHome">
      <img src="@/assets/images/sixtale.png" alt="Site Logo" class="logo-image" />
    </div>
    <nav class="nav-menu">
      <ul>
        <li><router-link to="/rulebook">룰북</router-link></li>
        <li><router-link to="/scenarios">시나리오</router-link></li>
        <li><router-link to="/find-players">구인광장</router-link></li>
        <li><router-link to="/lobby">로비</router-link></li>
      </ul>
    </nav>
    <div class="profile" @click="openLoginModal">
      <img src="@/assets/images/user.png" alt="Profile Image" class="profile-image" />
      <div class="account">로그인</div>
    </div>
    <SocialLoginModal v-model="isModalOpen" />
  </header>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import SocialLoginModal from './SocialLoginModal.vue';

// Props
const props = defineProps({
  height: {
    type: String,
    default: '70px'
  }
});

// Router instance
const router = useRouter();

// State variables
const isModalOpen = ref(false);

// Methods
const goHome = () => {
  router.push('/');
};

const openLoginModal = () => {
  isModalOpen.value = true;
};
</script>

<style scoped>
.main-header {
  display: flex;
  align-items: center;
  padding: 0 20px;
  background-color: #091d31; 
  color: white;
}

.logo {
  cursor: pointer;
  display: flex;
  align-items: center;
}

.logo-image {
  height: 50px;
}

.nav-menu ul {
  display: flex;
  list-style: none;
  padding: 0;
  margin: 0;
  margin-left: 40px; 
}

.nav-menu li {
  margin: 0 15px;
}

.nav-menu a {
  color: white;
  text-decoration: none;
}

.profile {
  cursor: pointer;
  margin-left: auto;
  display: flex;
  align-items: center;
}

.profile-image {
  height: 40px;
  width: 40px;
  object-fit: cover;
  border-radius: 50%;
  margin-right: 10px;
}

.account {
  color: white;
  padding: 10px;
}
</style>
