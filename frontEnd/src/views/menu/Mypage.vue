<template>
  <div class="background">
    <div class="container">
      <div class="row mt-2 g-5">
        <!-- 메인 왼쪽 콘텐츠 -->
        <div class="col-md-3">
          <div class="content-box left-box">
            <!--프로필 섹션 -->
            <div class="profile-section">
              <img
                :src="profileImage || require('@/assets/images/mypage/user.png')"
                alt="Profile Image"
                class="profile-image"
              />
              <!-- Profile Name -->
              <h3 class="profile-name">
                <router-link to="/mypage/user-info"
                  >{{ nickName.length > 7 ? nickName.substring(0, 7) + "..." : nickName }} 님
                  &gt;</router-link
                >
              </h3>
              <hr class="divider" />
              <!-- Menu -->
              <div class="menu-section">
                <h4 class="menu-header">플레이</h4>
                <ul class="menu-list">
                  <li>
                    <router-link to="/mypage/user-playlist">
                      <img
                        src="@/assets/images/mypage/left_box/play_log.png"
                        alt="Records Icon"
                        class="menu-icon"
                      />
                      플레이 기록
                    </router-link>
                  </li>
                  <li>
                    <router-link to="/mypage/user-calendar">
                      <img
                        src="@/assets/images/mypage/left_box/play_schedule.png"
                        alt="Schedule Icon"
                        class="menu-icon"
                      />
                      플레이 일정
                    </router-link>
                  </li>
                </ul>
                <h4 class="menu-header">시나리오</h4>
                <ul class="menu-list">
                  <li>
                    <router-link to="/mypage/user-likescenario">
                      <img
                        src="@/assets/images/mypage/left_box/heart.png"
                        alt="Scenario Icon"
                        class="menu-icon"
                      />
                      좋아요한 시나리오
                    </router-link>
                  </li>
                </ul>
                <h4 class="menu-header">나의 정보</h4>
                <ul class="menu-list">
                  <li>
                    <router-link to="/mypage/user-info">
                      <img
                        src="@/assets/images/mypage/left_box/my_profile.png"
                        alt="Profile Icon"
                        class="menu-icon"
                      />
                      정보 조회 / 수정
                    </router-link>
                  </li>
                  <li>
                    <router-link to="/mypage/user-style">
                      <img
                        src="@/assets/images/mypage/left_box/my_like.png"
                        alt="Sheets Icon"
                        class="menu-icon"
                      />
                      성향 시트
                    </router-link>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
        <!-- 메인 오른쪽 콘텐츠 -->
        <div class="col-md-9">
          <router-view></router-view>
        </div>
      </div>
      <div style="height: 10px" class="mb-5"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { getMemberInfo } from "@/common/api/mypageAPI";

const id = ref(0);
const nickName = ref("");
const profileImage = ref("");

onMounted(() => {
  getMemberInfo()
    .then((response) => {
      id.value = response.data.data.id;
      nickName.value = response.data.data.nickName;
      profileImage.value = response.data.data.imageURL;
      console.log(id.value);
      console.log(nickName.value);
      console.log(profileImage.value);
    })
    .catch((error) => {
      console.error("Failed to fetch member info:", error);
    });
});
</script>

<style scoped>
.background {
  width: 100%;
  background: radial-gradient(circle, #262626 0%, #232428 100%);
}
.container {
  max-width: 95%;
  padding-top: 80px;
}
.menu-title {
  color: white;
  margin: 0 0 20px 30px;
}
.content-box {
  background-color: #3a3a3c;
  border-radius: 18px;
}
.left-box {
  height: 750px;
  padding: 20px;
  border: 1px solid #3a3a3c;
}
.right-box {
  height: 685px; /*왼쪽 박스와 높이 끝 같게 */
}
.profile-section {
  text-align: center;
  color: white;
}
.profile-image {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  margin-top: 30px;
  margin-bottom: 20px;
  border: 2px solid #4c5c68; /* Profile Image border color */
}
.profile-name {
  font-size: 1.5rem;
  margin-bottom: 30px;
  color: white;
  font-style: none;
}
.profile-name a {
  color: white;
  text-decoration: none;
}

.divider {
  border-color: #5c5c5e;
  margin: 30px 0 40px 0;
}
.menu-section {
  text-align: left;
}
.profile-name a:hover {
  text-decoration: underline;
}
.menu-header {
  font-size: 1rem;
  color: #bbb;
  margin: 20px 0 20px 0;
}
.menu-list {
  list-style-type: none;
  padding: 0;
  margin-bottom: 30px;
}
.menu-list li {
  padding: 10px;
}
.menu-list a {
  color: white;
  text-decoration: none;
  font-size: 1rem;
  display: flex;
  align-items: center;
  margin-left: 20px;
}
.menu-list :hover {
  background-color: #343d54; /* Blue background on hover */
  color: white;
}
.menu-icon {
  width: 24px;
  height: 24px;
  margin-right: 10px;
  object-fit: contain;
}
</style>
