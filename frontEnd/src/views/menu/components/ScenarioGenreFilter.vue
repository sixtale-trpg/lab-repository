<template>
  <div>
    <div class="side-bar">
      <p class="mb-1" style="color: rgba(255, 255, 255, 0.64)">필터</p>
      <div
        class="btn-group d-block"
        role="group"
        area-label="Basic checkbox toggle button group"
      >
        <div class="d-flex mx-3 mb-1" v-for="genre in genres" :key="genre.id">
          <input
            type="checkbox"
            class="check-genre"
            :id="'btn-check-' + genre.id"
            @change="toggleGenreSelection(genre.id)"
            autocomplete="off"
          />
          <label :for="'btn-check-' + genre.id"> {{ genre.name }}</label>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from "vue";

export default {
  name: "ScenarioGenreFilter",
  props: {
    genres: {
      type: Array,
      required: true,
    },
    selectedGenres: {
      type: Array,
      reuiqred: true,
    },
  },
  setup(props, { emit }) {
    const checkboxes = ref({});

    const toggleGenreSelection = (genreId) => {
      const index = props.selectedGenres.indexOf(genreId);

      if (index > -1) {
        // 이미 선택된 경우 제거
        emit(
          "update:selectedGenres",
          props.selectedGenres.filter((id) => id !== genreId)
        );
      } else {
        // 선택되지 않은 경우 추가
        emit("update:selectedGenres", [...props.selectedGenres, genreId]);
      }
    };

    return {
      checkboxes,
      toggleGenreSelection,
    };
  },
};
</script>

<style scoped>
.side-bar {
  width: 130px;
  background-color: #3c3d41;
  border-radius: 1em;
  color: white;
  padding: 10px 15px;
}
input[type="checkbox"] {
  display: none;
}
input[type="checkbox"]:checked + label {
  color: gray;
}
</style>
