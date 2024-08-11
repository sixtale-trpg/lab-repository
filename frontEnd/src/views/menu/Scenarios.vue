<template>
  <div class="background">
    <div class="container">
      <h1 class="menu-title">시나리오</h1>
      <div class="card-container" style="background-color: gray">
        {{ scenarios.scenarioList }}
        <div class="card-inner-container">
        <ScenarioCard
          class="card-item"
          v-for="scenario in scenarios.scenarioList" :key="index"
          :scenario="scenario"
        />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from "vue";
import ScenarioCard from "./components/ScenarioCard.vue";
import { getScenarioList } from "@/common/api/ScenarioAPI.js";

onMounted();

export default {
  name: "Scenarios",
  components: {
    ScenarioCard,
  },
  setup() {
    // const scenarios = ref([
    //   {
    //     title: "아름다운 마을의 더러운 날들",
    //     genres: ["판타지", "스릴러", "모험"]
    //   },
    //   {
    //     title: "테스트2",
    //     genres: ["호러", "스릴러"]
    //   },
    //   {
    //     title: "테스트3",
    //     genres: ["추리", "일상"]
    //   },
    //   {
    //     title: "테스트4",
    //     genres: ["판타지", "호러"]
    //   },
    //   {
    //     title: "테스트5",
    //     genres: ["현대", "추리"]
    //   }
    // ]);
    const scenarios = ref([]);
    onMounted(async () => {
      const scenarios = getScenarioList().then(res => console.log(res));
    });
    return { scenarios };
  }
};
</script>

<style scoped>
.background {
  width: 100%;
  background: radial-gradient(circle, #262626 0%, #232428 100%);
}
.container {
  max-width: 1200px;
  padding-top: 80px;
}
.card-container {
  padding: 20px;
  display: flex;
  justify-content: center; /* 중앙 정렬을 위해 추가 */
}

.card-inner-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: start;
  gap: 20px; /* div들 사이의 일정한 간격 */
  max-width: 1200px; /* 원하는 최대 너비 설정 */
  width: 100%; /* 전체 너비 사용 */
}

.card-item {
  /* 기타 스타일 */
}
.menu-title {
  color: white;
  margin: 50px 0;
}
</style>
