<template>
  <div class="background">
    <div class="container">
      <h1 class="menu-title">시나리오</h1>
      <!-- 정렬 버튼 -->
      <div class="d-flex justify-content-end" role="group">
        <button
          type="button"
          class="rounded text-white dropdown-toggle"
          style="background-color: #3c3d41"
          data-bs-toggle="dropdown"
          aria-expanded="false"
        >
          {{ selectedSort.text }}
        </button>
        <ul class="dropdown-menu">
          <li>
            <a
              class="dropdown-item"
              href="#"
              @click="selectSort('updatedAt', 'desc', '최신순')"
              >최신순</a
            >
          </li>
          <li>
            <a
              class="dropdown-item"
              href="#"
              @click="selectSort('updatedAt', 'asc', '등록순')"
              >등록순</a
            >
          </li>
          <li>
            <a
              class="dropdown-item"
              href="#"
              @click="selectSort('likes', 'desc', '좋아요순')"
              >좋아요순</a
            >
          </li>
        </ul>
      </div>
      <div class="main-container d-flex">
        <!-- 장르 필터 -->
        <ScenarioGenreFilter
          :genres="genres"
          :selectedGenres="selectedGenres"
          @update:selectedGenres="selectedGenres = $event"
        />
        <!-- 시나리오 아이템 목록 -->
        <div class="card-container">
          <div class="card-inner-container">
            <ScenarioCard
              class="card-item"
              v-for="scenario in scenarios.scenarioList"
              :key="scenario.id"
              :scenario="scenario"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, watch } from "vue";
import ScenarioCard from "./components/ScenarioCard.vue";
import ScenarioGenreFilter from "./components/ScenarioGenreFilter.vue";
import { getScenarioList } from "@/common/api/ScenarioAPI.js";
import { getGenreList } from "@/common/api/GenreAPI";

export default {
  name: "Scenarios",
  components: {
    ScenarioCard,
    ScenarioGenreFilter,
  },
  setup() {
    const scenarios = ref([]);
    const genres = ref([]);
    const selectedGenres = ref([]);
    const selectedSort = ref({
      criteria: "updatedAt",
      order: "desc",
      text: "최신순",
    });

    const selectSort = async (criteria, order, text) => {
      if (
        selectedSort.value.criteria == criteria &&
        selectedSort.value.order == order
      ) {
        return;
      }

      selectedSort.value = { criteria, order, text };

      scenarios.value = await getScenarioList(
        selectedGenres.value.join(","),
        "",
        0,
        20,
        selectedSort.value.criteria,
        selectedSort.value.order
      );
    };

    onMounted(async () => {
      scenarios.value = await getScenarioList(
        "",
        "",
        0,
        20,
        selectedSort.value.criteria,
        selectedSort.value.order
      );
      genres.value = await getGenreList();
    });

    watch(selectedGenres, async (newGenres) => {
      scenarios.value = await getScenarioList(
        newGenres.join(","),
        "",
        0,
        20,
        selectedSort.value.criteria,
        selectedSort.value.order
      );
    });

    return { scenarios, genres, selectedSort, selectSort, selectedGenres };
  },
};
</script>

<style scoped>
.background {
  width: 100%;
  background: radial-gradient(circle, #262626 0%, #232428 100%);
}
.container {
  max-width: 1200px;
  padding-top: 100px;
}
.main-container {
  max-width: 1200px;
}
.card-container {
  padding-left: 20px;
  display: flex;
  justify-content: center;
}
.card-inner-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: start;
  gap: 20px; /* div들 사이의 일정한 간격 */
  width: 100%;
}
.menu-title {
  color: white;
  margin: 50px 0;
}
</style>
