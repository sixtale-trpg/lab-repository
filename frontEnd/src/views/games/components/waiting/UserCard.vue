<!-- UserCard.vue -->
<template>
  <div :style="userCardStyle" class="user-card">
    <div class="user-profile">
      <div class="profile-image-container">
        <!-- 사용자 프로필 이미지 -->
        <img
          :src="user.profileImage"
          alt="사용자 프로필"
          class="profile-image"
          @click="openUserProfile(user.id)"
        />
        <img :src="avatarFrameImage" alt="테두리" class="avatar-frame" />
      </div>
    </div>
    <div class="user-actions">
      <img
        v-if="isGM"
        src="@/assets/images/room/kick.png"
        alt="강퇴"
        @click="showKickModal = true"
      />
    </div>
    <div :style="userNameStyle" class="user-name" :title="user.name">
      {{ truncatedName }}
    </div>

    <!-- 사용자 정보 모달 -->
    <Userinfo
      v-if="showUserModal"
      :user="fetchedUserData"
      @close="showUserModal = false"
    />
    <AddFriendModal
      v-if="showAddFriendModal"
      :user="user"
      @close="showAddFriendModal = false"
    />
    <KickModal
      v-if="showKickModal"
      :user="user"
      @close="showKickModal = false"
      @kick="kickUser"
    />
  </div>
</template>

<script setup>
import { ref, computed, defineProps } from 'vue';
import Userinfo from '@/views/games/components/Modal/UserInfo.vue';
import AddFriendModal from '@/views/games/components/Modal/AddFriendModal.vue';
import KickModal from '@/views/games/components/Modal/KickModal.vue';

// props로 전달된 사용자 데이터
const props = defineProps({
  user: Object,
  isGM: Boolean,
});

// 더미 데이터 정의
const dummyUsers = [
  {
    id: 1,
    race: '인간',
    job: '전사',
    background: '검투사 출신이며...',
  },
  {
    id: 2,
    race: '엘프',
    job: '마법사',
    background: '숲에서 자라난...',
  },
  {
    id: 3,
    race: '드워프',
    job: '기사',
    background: '산속에서 전투를...',
  },
  {
    id: 4,
    race: '인간',
    job: '궁수',
    background: '활을 잘 다루며...',
  },
  {
    id: 5,
    race: '오크',
    job: '도적',
    background: '몰래 다가가 적을...',
  },
  {
    id: 6,
    race: '고블린',
    job: '도적',
    background: '작고 민첩한...',
  },
  {
    id: 7,
    race: '하프엘프',
    job: '음유시인',
    background: '노래와 시를 사랑하며...',
  },
  {
    id: 8,
    race: '하프오크',
    job: '전사',
    background: '힘과 용맹을 겸비한...',
  },
];

const showUserModal = ref(false);
const showAddFriendModal = ref(false);
const showKickModal = ref(false);
const fetchedUserData = ref(null);

// 백그라운드 이미지 경로 설정
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

// 사용자 이름이 길 경우 잘라서 표시
const truncatedName = computed(() => {
  return props.user.name.length > 10
    ? props.user.name.slice(0, 10) + '...'
    : props.user.name;
});

// 사용자 프로필을 열어 모달에 사용자 정보 표시
const openUserProfile = (userId) => {
  // props.user의 ID를 기반으로 추가 정보를 결합
  const additionalData = dummyUsers.find((user) => user.id === userId);
  if (additionalData) {
    // 기존 사용자 데이터에 추가 정보를 결합하여 fetchedUserData에 저장
    fetchedUserData.value = { ...props.user, ...additionalData };
    showUserModal.value = true; // 모달 표시
  } else {
    console.error('사용자 데이터를 찾을 수 없습니다.');
  }
};

const kickUser = (user) => {
  console.log(`사용자 강퇴: ${user.name}`);
};

const addFriend = (user) => {
  console.log(`친구 추가: ${user.name}`);
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
