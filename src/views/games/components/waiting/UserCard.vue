<template>
  <div class="user-card">
    <div class="user-profile">
      <img :src="user.profileImage" alt="User Profile" class="profile-image" />
      <div class="user-actions">
        <img src="@/assets/images/room/info.png" alt="View" @click="showUserModal = true" />
        <img src="@/assets/images/room/add-friend.png" alt="Add Friend" @click="showAddFriendModal = true" />
        <img v-if="isGM" src="@/assets/images/room/kick.png" alt="Kick" @click="showKickModal = true" />
      </div>
    </div>
    <div class="user-name">{{ user.name }}</div>

    <Userinfo v-if="showUserModal" :user="user" @close="showUserModal = false" />
    <AddFriendModal v-if="showAddFriendModal" :user="user" @close="showAddFriendModal = false" />
    <KickModal v-if="showKickModal" :user="user" @close="showKickModal = false" @kick="kickUser" />
  </div>
</template>

<script setup>
import { ref } from 'vue';
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
  width: 60%; 
  height: auto;
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

.user-name {
  background-color: #564307; 
  padding: 5px 10px;
  border-radius: 5px;
  color: #ffffff; 
  border: 1px solid #5a4d41; 
  margin-top: 10px; 
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
    width: 50%;
  }
}
</style>
