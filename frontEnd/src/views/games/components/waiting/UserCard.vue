<template>
  <div :style="userCardStyle" class="user-card">
    <div class="user-profile">
      <div class="profile-image-container">
        <!-- 사용자 프로필 이미지 -->
        <img
          :src="user.profileImage || defaultImage"
          alt="사용자 프로필"
          class="profile-image"
          @click="openUserProfile"
        />
        <img :src="avatarFrameImage" alt="테두리" class="avatar-frame" />
      </div>
    </div>
    <div class="user-actions">
      <img
        v-if="isGM && user.id"
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
      :user="user"
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
      :roomId="roomId"
      :accessToken="accessToken"
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
import defaultImage from '@/assets/images/users/default.png';
import { kickUserFromRoom } from '@/common/api/RoomsAPI';

// props로 전달된 사용자 데이터
const props = defineProps({
  user: {
    type: Object,
    default: () => ({
      id: null,
      name: '',
      profileImage: ''
    })
  },
  isGM: Boolean,
  roomId: {
    type: [String, Number],
    required: true
  },
  accessToken: {
    type: String,
    required: true
  }
});

const showUserModal = ref(false);
const showAddFriendModal = ref(false);
const showKickModal = ref(false);

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
const openUserProfile = () => {
  if (props.user.id) {
    showUserModal.value = true;
  }
};

const kickUser = async () => {
  try {
    const response = await kickUserFromRoom(props.roomId, props.user.id, props.accessToken);
    if (response.statusCode === 200) {
      console.log(`사용자 강퇴: ${props.user.name}`);
      // 강퇴 성공 시, 추가적인 동작을 여기에 작성 (예: UI 업데이트)
    }
  } catch (error) {
    console.error('Error kicking user:', error);
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
  width: 100%; /* 이미지를 컨테이너에 맞게 조절 */
  height: 100%; /* 이미지를 컨테이너에 맞게 조절 */
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
