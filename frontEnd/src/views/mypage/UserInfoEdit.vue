<template>
  <div><h3 class="menu-title">정보 조회/수정</h3></div>
  <!-- 정보 조회 페이지 -->
  <div class="content-box right-box">
    <div class="profile-header">
      <div class="profile-image-container">
        <img
          :src="previewImage || profileImage || require('@/assets/images/mypage/user.png')"
          alt="Profile Image"
          class="profile-avater"
        />
        <input type="file" multiple @change="handleFileChange" />
      </div>
      <div class="profile-info">
        <input
          type="text"
          v-model="nickName"
          :placeholder="user.nickName"
          class="profile-nickname"
        />
        <p class="profile-join-date">{{ formatJoinDate(user.createdAt) }} 가입</p>
      </div>
    </div>
    <hr class="divider" />
    <div class="profile-details">
      <div class="profile-detail-item">
        <strong>선호 룰</strong>
        <p>{{ user.preferredRules }}</p>
      </div>
      <div class="profile-detail-item">
        <strong>선호 장르</strong>
        <p>{{ user.preferredGenres }}</p>
      </div>
    </div>
    <div class="profile-actions">
      <button class="profile-button" @click="saveChanges">저장</button>
      <button class="profile-button danger" @click="cancelEditing">취소</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { getMemberInfo, getMemberDetailInfo, updateMemberInfo } from "@/common/api/mypageAPI";
const router = useRouter();
const user = ref({});
const userDetail = ref({});

const nickName = ref(""); // 수정 닉네임 정보
const previewImage = ref(""); // 새로운 이미지 미리보기
const profileImage = ref("");

onMounted(() => {
  //회원 기본 정보
  getMemberInfo()
    .then((response) => {
      console.log(response.data.data);
      user.value = response.data.data;
      nickName.value = user.value.nickName;
      profileImage.value = user.value.imageURL;
    })
    .catch((error) => {
      console.error("Failed to fetch member info:", error);
    });
  //회원 상세 정보
  getMemberDetailInfo()
    .then((response) => {
      console.log(response.data.data);
      userDetail.value = response.data.data;
    })
    .catch((error) => {
      console.error("Failed to fetch member Detail info:", error);
    });
});

// createdAt 배열 포맷팅
const formatJoinDate = (createdAtArray) => {
  if (!createdAtArray || createdAtArray.length < 3) {
    return ""; // createdAt이 배열이 아니거나, 데이터가 충분하지 않으면 빈 문자열 반환
  }
  const [year, month, day] = createdAtArray;
  return `${year}.${String(month).padStart(2, "0")}.${String(day).padStart(2, "0")}`;
};

// 수정 파일 정보
const selectedFiles = ref([]);
const handleFileChange = (event) => {
  const files = event.target.files;
  if (files.length > 0) {
    selectedFiles.value = files;
    previewImage.value = URL.createObjectURL(files[0]);
  }
};

const saveChanges = () => {
  //회원 기본 정보 저장
  updateMemberInfo(nickName.value, selectedFiles.value)
    .then((response) => {
      console.log("User info updated successfully");
      router.push({ name: "UserInfo" });
    })
    .catch((error) => {
      console.error("Failed to update user info:", error);
    });
};

const cancelEditing = () => {
  router.push({ name: "UserInfo" });
};
</script>

<style scoped>
.menu-title {
  color: white;
  margin: 0 0 20px 20px;
}
.content-box {
  background-color: #3a3a3c;
  border-radius: 18px;
  position: relative;
}
.left-box {
  height: 750x;
  padding: 20px;
  border: 1px solid #3a3a3c;
}
.right-box {
  height: 695px; /*왼쪽 박스와 높이 끝 같게 */
  padding: 80px;
  border: 1px solid #3a3a3c;
}
.profile-header {
  display: flex;
  align-items: center;
  margin-bottom: 40px;
}
.profile-image-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
.profile-avatar {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  border: 4px solid #ffffff;
  margin-right: 50px;
  margin-bottom: 10px;
}
.profile-info {
  flex-grow: 1;
  padding: 50px;
}
.profile-nickname {
  color: #ffffff;
  font-size: 1.8rem;
  margin-bottom: 20px;
  background-color: #4c4f53;
  border: none;
  border-radius: 5px;
  padding: 5px 10px;
  width: 100%;
}
.profile-join-date {
  color: #bfbfc0;
  margin: 5px 0 0;
}
.divider {
  border: 1px solid #ffffff;
  margin: 60px 0;
}
.profile-details {
  margin-bottom: 50px;
}
.profile-detail-item {
  margin-bottom: 40px;
}
.profile-detail-item strong {
  color: #ffffff;
  font-size: 1.2rem;
  display: block;
  margin-bottom: 5px;
}
.profile-detail-item p {
  color: #bfbfc0;
  margin: 0;
}
.profile-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  position: absolute;
  right: 20px;
  bottom: 20px;
}
.profile-button {
  color: white;
  background-color: #3a3a3c;
  border: none;
  padding: 10px 20px;
  border-radius: 10px;
  cursor: pointer;
  font-size: 1rem;
  text-decoration: none;
}
.profile-button.danger {
  background-color: #3a3a3c;
}
.profile-button:hover {
  text-decoration: underline;
  font-weight: bold;
}
</style>
