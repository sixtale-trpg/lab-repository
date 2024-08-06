<template>
  <div :style="userCardStyle" class="user-card">
    <div class="user-profile">
      <div class="profile-image-container">
        <img :src="user.profileImage" alt="사용자 프로필" class="profile-image" @click="showUserModal = true" />
        <img :src="avatarFrameImage" alt="테두리" class="avatar-frame" />
      </div>
    </div>
    <div class="user-actions">
      <img v-if="isGM" src="@/assets/images/room/kick.png" alt="강퇴" @click="showKickModal = true" />
    </div>
    <div :style="userNameStyle" class="user-name" :title="user.name">{{ truncatedName }}</div>

    <Userinfo v-if="showUserModal" :user="fetchedUserData" @close="showUserModal = false" />
    <AddFriendModal v-if="showAddFriendModal" :user="user" @close="showAddFriendModal = false" />
    <KickModal v-if="showKickModal" :user="user" @close="showKickModal = false" @kick="kickUser" />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { defineProps } from 'vue';
import axios from 'axios';
import Userinfo from '@/views/games/components/Modal/UserInfo.vue';
import AddFriendModal from '@/views/games/components/Modal/AddFriendModal.vue';
import KickModal from '@/views/games/components/Modal/KickModal.vue';

const props = defineProps({
  user: Object,
  isGM: Boolean,
});

const showUserModal = ref(false);
const showAddFriendModal = ref(false);
const showKickModal = ref(false);
const fetchedUserData = ref(null);

const addFriend = (user) => {
  console.log(`Adding friend ${user.name}`);
};

const kickUser = (user) => {
  console.log(`Kicking user ${user.name}`);
};

const profileBoxImage = require('@/assets/images/room/profile_box.png');
const nameBoxImage = require('@/assets/images/room/name_box.png');
const avatarFrameImage = require('@/assets/images/room/avatar_frame.png');

const userCardStyle = computed(() => ({
  backgroundImage: `url(${profileBoxImage})`,
  backgroundSize: 'cover',
  backgroundPosition: 'center',
}));

const userNameStyle = computed(() => ({
  backgroundImage: `url(${nameBoxImage})`,
  backgroundSize: 'cover',
  backgroundPosition: 'center',
  padding: '4% 10%',
  borderRadius: '5px',
  color: '#ffffff',
  border: '1px solid #5a4d41',
  marginTop: '0%',
  width: '90%',
  textAlign: 'center',
  display: 'inline-block',
  height: '15%',
  lineHeight: '1.0',
  overflow: 'hidden',
  textOverflow: 'ellipsis',
  whiteSpace: 'nowrap',
}));

const truncatedName = computed(() => {
  return props.user.name.length > 10 ? props.user.name.slice(0, 10) + '...' : props.user.name;
});

const fetchUserProfile = async (userId) => {
  try {
    const response = await axios.get(`/api/users/${userId}`);
    fetchedUserData.value = response.data;
    showUserModal.value = true;
  } catch (error) {
    console.error('Failed to fetch user profile:', error);
  }
};
</script>

<style scoped>
.user-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  background-color: #291707;
  border-radius: 10px;
  position: relative;
  margin: 2%;
  padding: 5%;
  width: 90%;
  height: 100%;
  overflow: hidden;
}

.user-profile {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: relative;
  flex: 1;
}

.profile-image-container {
  position: relative;
  width: 100%;
  height: 95%;
  overflow: hidden;
  border-radius: 50%;
  background-color: #291707;
  display: flex;
  align-items: center;
  justify-content: center;
}

.profile-image {
  width: 90%;
  height: 90%;
  object-fit: cover;
  border-radius: 50%;
  cursor: pointer;
}

.avatar-frame {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  border-radius: 50%;
}

.user-actions {
  display: flex;
  gap: 5%;
  position: absolute;
  top: 2%;
  right: -2%;
  z-index: 1;
}

.user-actions img {
  width: 65%;
  height: auto;
  cursor: pointer;
}

.user-name {
  text-align: center;
  font-size: 90%;
}

.user-name:hover::after {
  content: attr(title);
  position: absolute;
  background: rgba(0, 0, 0, 0.75);
  color: #fff;
  padding: 5px 10px;
  border-radius: 5px;
  white-space: nowrap;
  z-index: 10;
  top: -25%;
  left: 50%;
  transform: translateX(-50%);
}

@media (max-width: 768px) {
  .user-card {
    padding: 5%;
  }

  .user-actions img {
    width: 20%;
    height: auto;
  }
}
</style>
