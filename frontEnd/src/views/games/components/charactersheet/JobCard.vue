<template>
  <div class="job-card">
    <img :src="image" alt="Job Image" class="job-image" />
    <h3>{{ title }}</h3>
  </div>
</template>

<script>
export default {
  props: {
    title: {
      type: String,
      required: true
    },
    image: {
      type: String,
      required: true
    }
  }
};
</script>

<style scoped>
.job-card {
  background-color: #444;
  color: white;
  padding: 10px;
  border-radius: 5px;
  text-align: center;
  width: 330px; /* 카드의 너비를 적절히 조정 */
  margin: 10px; /* 카드 간격을 조정 */
  display: flex;
  flex-direction: column;
  align-items: center;
}

.job-image {
  width: 100px;
  height: 150px;
  object-fit: cover;
  border-radius: 50%;
  margin-bottom: 10px; /* 이미지와 텍스트 사이의 간격을 추가 */
}
</style>
