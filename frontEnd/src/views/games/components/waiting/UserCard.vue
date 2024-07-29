<template>
  <div :style="userCardStyle" class="user-card">
    <div class="user-profile">
      <img :src="user.profileImage" alt="사용자 프로필" class="profile-image" />
      <div class="user-actions">
        <img src="@/assets/images/room/info.png" alt="보기" @click="showUserModal = true" />
        <img src="@/assets/images/room/add-friend.png" alt="친구 추가" @click="showAddFriendModal = true" />
        <img v-if="isGM" src="@/assets/images/room/kick.png" alt="강퇴" @click="showKickModal = true" />
      </div>
    </div>
    <div :style="userNameStyle" class="user-name" :title="user.name">{{ truncatedName }}</div>

    <Userinfo v-if="showUserModal" :user="user" @close="showUserModal = false" />
    <AddFriendModal v-if="showAddFriendModal" :user="user" @close="showAddFriendModal = false" />
    <KickModal v-if="showKickModal" :user="user" @close="showKickModal = false" @kick="kickUser" />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { defineProps } from 'vue';
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

const addFriend = (user) => {
  console.log(`Adding friend ${user.name}`);
};

const kickUser = (user) => {
  console.log(`Kicking user ${user.name}`);
};

// 이미지 경로를 script에서 정의
const profileBoxImage = require('@/assets/images/room/profile_box.png');
const nameBoxImage = require('@/assets/images/room/name_box.png');

// computed 속성을 사용하여 인라인 스타일을 정의
const userCardStyle = computed(() => ({
  backgroundImage: `url(${profileBoxImage})`,
  backgroundSize: 'cover',
  backgroundPosition: 'center',
}));

const userNameStyle = computed(() => ({
  backgroundImage: `url(${nameBoxImage})`,
  backgroundSize: 'cover',
  backgroundPosition: 'center',
  padding: '10px 20px', // 패딩 조정
  borderRadius: '5px',
  color: '#ffffff',
  border: '1px solid #5a4d41',
  marginTop: '10px',
  width: '160px', // 고정 너비 설정
  textAlign: 'center',
  display: 'inline-block',
  height: '50px', // 고정 높이 설정
  lineHeight: '30px', // 텍스트 가운데 정렬
  overflow: 'hidden',
  textOverflow: 'ellipsis',
  whiteSpace: 'nowrap',
}));

const truncatedName = computed(() => {
  return props.user.name.length > 10 ? props.user.name.slice(0, 10) + '...' : props.user.name;
});
</script>

<style scoped>
.user-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  background-color: #291707;
  border: 1px solid #ccc;
  border-radius: 10px;
  position: relative;
  margin: 10px;
  padding: 15px 0;
  width: calc(100% - 20px);
}

.user-profile {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex: 1;
  justify-content: center;
}

.profile-image {
  width: 120px; /* 이미지 크기를 고정 */
  height: 120px; /* 이미지 크기를 고정 */
  border-radius: 50%;
}

.user-actions {
  display: flex;
  gap: 5px;
  position: absolute;
  top: 5px;
  right: 5px;
}

.user-actions img {
  width: 20px;
  height: 20px;
  cursor: pointer;
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
  top: -25px;
  left: 50%;
  transform: translateX(-50%);
}

@media (max-width: 768px) {
  .user-card {
    padding: 10px 0;
  }

  .user-actions img {
    width: 15px;
    height: 15px;
  }

  .profile-image {
    width: 50px;
    height: 50px;
  }
}
</style>
