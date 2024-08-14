<template>
  <div :style="waitingContainerStyle" class="waiting-container">
    <RoomHeader :roomTitle="roomDetails.title" :nextSchedule="nextSchedule" />
    <div class="content">
      <div class="left-section">
        <div class="user-cards">
          <UserCard
            v-for="user in allUsers"
            :key="user.id || user.placeholder"
            :user="user"
            :isGM="isGM"
            :roomId="roomId"
            :memberId="user.memberId"
          />
        </div>
        <Chat class="chat-section" />
      </div>
      <div class="right-section">
        <div :style="topSectionStyle" class="top-section">
          <Map :roomId="roomId" :mapList="mapList" :gmName="gm.name" />
          <div :style="gmCardStyle" class="gm-section">
            <div class="gm-info">
              <div
                :style="profileImageContainerStyle"
                class="profile-image-container"
              >
                <img
                  :src="gm.profileImage || defaultImage"
                  alt="GM 프로필"
                  class="profile-image"
                  @click="showGMModal = true"
                />
                <img
                  :src="avatarFrameImagePath"
                  alt="테두리"
                  class="avatar-frame"
                />
              </div>
              <div :style="gmNameStyle" class="gm-name" :title="gm.name">
                {{ truncatedGMName }}
              </div>
            </div>
            <button
              :disabled="nickName !== gm.name"
              :style="[startGameButtonStyle, nickName !== gm.name ? disabledButtonStyle : {}]"
              class="start-game-button"
              @click="startGame"
            >
              게임 시작
            </button>
          </div>
        </div>
        <div class="details">
          <div class="rule-scenario">
            <div :style="gameInfoStyle" class="game-info">
              <div class="title">
                <div :style="vectorImage">게임 룰</div>
              </div>
              <div class="content cursor" @click="openRulebookModal">
                <div
                  :style="gameRuleContainerStyle"
                  class="game-rule-container"
                >
                  <div class="game-rule-text">{{ gameRule }}</div>
                </div>
              </div>
            </div>
            <div :style="scenarioInfoStyle" class="game-info">
              <div class="title">
                <div :style="vectorImage">시나리오</div>
              </div>
              <div
                class="content scenario-content cursor"
                @click="openScenarioModal"
              >
                <img
                  :src="scenarioImagePath"
                  class="scenario-image"
                />
                <div class="scenario-text">{{ scenario }}</div>
              </div>
            </div>
          </div>
          <Calendar @select="selectDate" :style="calendarContainerStyle" />
        </div>
      </div>
    </div>
    <Userinfo
      v-if="showUserModal"
      :user="selectedUser"
      @close="showUserModal = false"
    />
    <Userinfo v-if="showGMModal" :user="gm" @close="showGMModal = false" />
    <ScenarioModal
      v-if="isScenarioModalOpen"
      :scenario="scenarioDetails"
      @close="closeScenarioModal"
    />
    <RulebookModal :isOpen="isRulebookModalOpen" @close="closeRulebookModal" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import { useStore } from "vuex";
import RoomHeader from "./components/waiting/RoomHeader.vue";
import UserCard from "./components/waiting/UserCard.vue";
import Chat from "./components/waiting/Chat.vue";
import Map from "./components/waiting/Map.vue";
import Calendar from "./components/Calendar.vue";
import RulebookModal from "./components/Modal/RulebookModal.vue";
import ScenarioModal from "./components/Modal/ScenarioModal.vue";
import Userinfo from "./components/Modal/UserInfo.vue";
import { getRoomInfo, getMapList } from "@/common/api/RoomsAPI";
import defaultImage from "@/assets/images/users/default.png";
import { getMemberInfo } from "@/common/api/mypageAPI"; // 사용자 정보 가져오기 API

const store = useStore();

const roomDetails = ref({
  title: "",
  currentPlayers: 0,
  totalPlayers: 0,
  status: "",
});

const users = ref([]);
const gm = ref({
  name: "",
  profileImage: "",
});

const isGM = ref(true); 
const nextSchedule = ref("");
const gameRule = ref("");
const scenario = ref("");
const scenarioDetails = ref({});
const showScenarioDetails = ref(false);
const roomId = ref(null);

const mapList = ref([]); 

const isRulebookModalOpen = ref(false);
const isScenarioModalOpen = ref(false);
const showUserModal = ref(false);
const showGMModal = ref(false);
const selectedUser = ref(null);

const router = useRouter();
const route = useRoute();

// 현재 로그인한 사용자의 닉네임을 가져오기 위한 변수.
const nickName = ref("");

onMounted(() => {
  getMemberInfo()
    .then((response) => {
      nickName.value = response.data.data.nickName;
    })
    .catch((error) => {
      console.error("Failed to fetch member info:", error);
    });

  roomId.value = route.params.roomId;
  fetchRoomDetails();
  fetchMapList(); 
});

const fetchRoomDetails = async () => {
  try {
    const roomId = route.params.roomId;
    const roomInfo = await getRoomInfo(roomId);

    roomDetails.value.title = roomInfo.title;
    roomDetails.value.currentPlayers = roomInfo.currentCount;
    roomDetails.value.totalPlayers = roomInfo.maxCount;
    roomDetails.value.status = roomInfo.status;
    nextSchedule.value = roomInfo.nextPlay;
    gameRule.value = roomInfo.ruleTitle;
    scenario.value = roomInfo.scenarioTitle;
    gm.value.name = roomInfo.gmNickname;
    gm.value.profileImage = roomInfo.gmImageURL || defaultImage;
    scenarioImagePath.value = roomInfo.scenarioImageURL; 

    users.value = roomInfo.playMemberList.map((member) => ({
      id: member.playMemberID,
      memberId: member.memberID,
      name: member.playMemberNickname,
      profileImage: member.playMemberImageURL || defaultImage,
    }));

    console.log("users", users.value);
  } catch (error) {
    console.error("Error fetching room details:", error);
  }
};

const fetchMapList = async () => {
  try {
    const fetchedMaps = await getMapList(roomId.value);
    if (fetchedMaps.mapList && Array.isArray(fetchedMaps.mapList)) {
      mapList.value = fetchedMaps.mapList;
    } else {
      console.log("No maps found or fetchedMaps.mapList is not an array.");
    }
  } catch (error) {
    console.error("Error fetching map list:", error);
  }
};

const selectDate = (date) => {
  nextSchedule.value = date;
};

const openRulebookModal = () => {
  isRulebookModalOpen.value = true;
};

const closeRulebookModal = () => {
  isRulebookModalOpen.value = false;
};

const openScenarioModal = () => {
  fetchScenarioDetails();
};

const closeScenarioModal = () => {
  isScenarioModalOpen.value = false;
};

const startGame = () => {
  router.push({
    name: "CharacterSheet",
    params: { roomId: route.params.roomId },
  });
};

const fetchScenarioDetails = async () => {
  try {
    scenarioDetails.value = {
      title: "왕자와 죽음의 개",
      writer_id: "writer123",
      summary: "이 시나리오는...",
      description: "상세 설명 내용...",
    };
    isScenarioModalOpen.value = true;
  } catch (error) {
    console.error("Error fetching scenario details:", error);
  }
};

const toggleScenarioDetails = () => {
  if (isGM.value) {
    showScenarioDetails.value = !showScenarioDetails.value;
  }
};

// 사용자 카드가 8개 되도록 빈 사용자 카드 추가
const allUsers = computed(() => {
  const placeholders = 8 - users.value.length;
  const placeholderCards = Array.from({ length: placeholders }, (_, index) => ({
    id: `placeholder-${index + 1}`,
    name: "",
    profileImage: "",
  }));
  return [...users.value, ...placeholderCards];
});

// 배경 이미지 경로 설정
const backgroundImagePath = require("@/assets/images/room/main_background.png");
const profileBoxImagePath = require("@/assets/images/room/profile_box.png");
const nameBoxImagePath = require("@/assets/images/room/name_box.png");
const avatarFrameImagePath = require("@/assets/images/room/avatar_frame.png");
const startButtonImagePath = require("@/assets/images/room/start_button.png");
const infoIconPath = require("@/assets/images/room/info.png");
const ruleBoxImagePath = require("@/assets/images/room/rule_box.png");
const ruleBox1ImagePath = require("@/assets/images/room/rule_box1.png");
const scenario_boxPath = require("@/assets/images/room/scenario_box.png");
const vectorImagePath = require("@/assets/images/room/Vector.png");
const scenarioImagePath = ref("");
const calendarBoxImagePath = require("@/assets/images/room/calendar_box.png");

// 배경 이미지 스타일 설정
const waitingContainerStyle = computed(() => ({
  backgroundImage: `url(${backgroundImagePath})`,
  backgroundSize: "cover",
  backgroundPosition: "center",
  backgroundRepeat: "no-repeat",
  height: "100vh",
  width: "100vw",
}));

// GM Card 스타일 설정
const gmCardStyle = computed(() => ({
  backgroundImage: `url(${profileBoxImagePath})`,
  backgroundSize: "cover",
  backgroundPosition: "center",
  display: "flex",
  flexDirection: "column",
  alignItems: "center",
  justifyContent: "space-between",
  borderRadius: "10px",
  height: "80%",
  padding: "5%",
  width: "90%",
  overflow: "hidden",
}));

const gmNameStyle = computed(() => ({
  backgroundImage: `url(${nameBoxImagePath})`,
  backgroundSize: "cover",
  backgroundPosition: "center",
  padding: "0% 0%",
  borderRadius: "5px",
  color: "#ffffff",
  border: "1px solid #5a4d41",
  marginTop: "8%",
  width: "110%",
  textAlign: "center",
  display: "inline-block",
  height: "15%",
  lineHeight: "1.5",
  overflow: "hidden",
  textOverflow: "ellipsis",
  whiteSpace: "nowrap",
}));

const truncatedGMName = computed(() => {
  return gm.value.name.length > 10 ? gm.value.name.slice(0, 10) + '...' : gm.value.name;
});

const profileImageContainerStyle = {
  position: "relative",
  width: "110%",
  height: "80%",
  overflow: "hidden",
  borderRadius: "50%",
  backgroundColor: "#291707",
  display: "flex",
  alignItems: "center",
  justifyContent: "center",
};

const topSectionStyle = {
  display: "flex",
  height: "55%",
};

const startGameButtonStyle = {
  backgroundImage: `url(${startButtonImagePath})`,
  backgroundSize: "cover",
  backgroundPosition: "center",
  width: "100%",
  padding: "4%",
  color: "white",
  borderRadius: "5px",
  cursor: "pointer",
  marginTop: "8%",
  border: "none",
  textAlign: "center",
  marginLeft: "6%",
};

const disabledButtonStyle = {
  cursor: "not-allowed",
  opacity: "0.5",
};

const vectorImage = computed(() => ({
  backgroundImage: `url(${vectorImagePath})`,
  width: "40%",
  backgroundSize: "cover",
  backgroundPosition: "center",
}));

const gameInfoStyle = computed(() => ({
  backgroundImage: `url(${ruleBox1ImagePath})`,
  backgroundSize: "cover",
  backgroundPosition: "center",
  height: "100%",
}));

const scenarioInfoStyle = computed(() => ({
  backgroundImage: `url(${scenario_boxPath})`,
  backgroundSize: "cover",
  backgroundPosition: "center",
  height: "100%",
}));

const gameRuleContainerStyle = {
  backgroundImage: `url(${ruleBoxImagePath})`,
  backgroundSize: "contain",
  backgroundRepeat: "no-repeat",
  backgroundPosition: "center",
  borderRadius: "10px",
  display: "flex",
  justifyContent: "center",
  alignItems: "center",
  width: "60%",
  height: "60%",
  fontFamily: "'Abhaya Libre ExtraBold', sans-serif",
  fontStyle: "normal",
  fontWeight: 800,
  fontSize: "18px",
  lineHeight: "100%",
  color: "rgb(214, 205, 170)",
  background: "rgba(101, 78, 53, 0.49)",
  boxShadow: "4px 4px 4px rgba(0, 0, 0, 0.25), inset 4px 4px 4px rgba(255, 255, 255, 0.15)",
};

const calendarContainerStyle = {
  backgroundImage: `url(${calendarBoxImagePath})`,
  backgroundSize: "cover",
  backgroundPosition: "center",
  display: "flex",
  justifyContent: "center",
  alignItems: "center",
  padding: "23px",
  borderRadius: "10px",
  width: "48%",
  height: "100%",
};
</script>

<style scoped>
.waiting-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  box-sizing: border-box;
}

.content {
  display: flex;
  height: 90%;
}

.left-section {
  width: 49%;
  display: flex;
  flex-direction: column;
  padding: 10px;
  box-sizing: border-box;
}

.user-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  grid-template-rows: repeat(2, 1fr);
  gap: 5px;
  height: 55%;
  padding-left: 6px;
}

.chat-section {
  flex: 1;
  margin-top: 20px;
  overflow-y: auto;
}

.right-section {
  width: 50%;
  display: flex;
  flex-direction: column;
  padding-top: 15px;
  box-sizing: border-box;
  overflow: hidden;
}

.top-section {
  display: flex;
  height: 60%;
}

.map-section {
  flex: 6;
  border-radius: 10px;
  height: 100%;
  position: relative;
}

.gm-section {
  flex: 4;
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-left: 10px;
  background-color: #291707;
  border-radius: 10px;
  height: 70%;
  width: 100%;
  justify-content: center;
}

.gm-profile {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: relative;
  flex: 1;
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

.info-icon {
  width: 1.04vw;
  height: 1.04vw;
  cursor: pointer;
  position: absolute;
  top: 12%;
  right: 1%;
  transform: translate(-50%, -50%);
}

.start-game-button.disabled {
  cursor: not-allowed;
  opacity: 0.5;
}

.details {
  display: flex;
  justify-content: space-between;
  margin-top: -7%;
  padding-bottom: 20px;
  flex-grow: 1;
}

.rule-scenario {
  display: flex;
  flex-direction: column;
  width: 50%;
  height: 100%;
}

.game-info {
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 10px;
  background-color: #130b02;
  border-radius: 10px;
  margin-bottom: 5px;
  height: 50%;
  background-size: cover;
  background-position: center;
}

.scenario-content {
  display: flex;
  align-items: center;
}

.scenario-image {
  width: 20%;
  height: 100%;
  margin-right: -30px;
}

.scenario-text {
  flex: 1;
  color: #ffffff;
}

.game-info .title {
  border-radius: 10px;
  color: rgb(214, 205, 170);
  border: none;
  border-radius: 10px;
  cursor: pointer;
  display: flex;
  align-items: center;
}

.info-icon-large {
  width: 16px;
  height: 16px;
  cursor: pointer;
  margin-left: 5px;
}

.game-info .content {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
  border-radius: 5px;
  color: #ffffff;
  border: 1px solid #5a4d41;
  text-align: center;
  font-size: 1em;
}

.calendar {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 23px;
  border-radius: 10px;
  width: 48%;
  height: 78%;
}

.map-section .map-controls {
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  display: flex;
  justify-content: space-between;
  transform: translateY(-50%);
}

.map-section .map-controls img {
  width: 20px;
  height: 20px;
  cursor: pointer;
}

.cursor {
  cursor: pointer;
}

.disabled {
  cursor: not-allowed;
}

@media (max-width: 768px) {
  .content {
    flex-direction: column;
  }

  .left-section,
  .right-section {
    width: 100%;
  }

  .user-cards {
    grid-template-columns: repeat(2, 1fr);
    grid-template-rows: repeat(4, 1fr);
    height: auto;
  }

  .chat-section {
    margin-top: 5px;
  }

  .top-section,
  .details {
    height: auto;
  }

  .details {
    flex-direction: column;
  }

  .rule-scenario,
  .calendar {
    width: 100%;
    margin-bottom: 10px;
  }
}
</style>
