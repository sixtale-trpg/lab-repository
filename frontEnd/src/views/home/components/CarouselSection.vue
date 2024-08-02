<template>
  <div id="carouselExampleIndicators" class="carousel slide no-padding" data-bs-ride="carousel">
    <div class="carousel-indicators">
      <button
        type="button"
        data-bs-target="#carouselExampleIndicators"
        data-bs-slide-to="0"
        class="active"
        aria-current="true"
        aria-label="Slide 1"
      ></button>
      <button
        type="button"
        data-bs-target="#carouselExampleIndicators"
        data-bs-slide-to="1"
        aria-label="Slide 2"
      ></button>
      <button
        type="button"
        data-bs-target="#carouselExampleIndicators"
        data-bs-slide-to="2"
        aria-label="Slide 3"
      ></button>
    </div>
    <div class="carousel-inner">
      <div class="carousel-item active">
        <img src="@/assets/images/carousel/slide1.jpg" class="d-block w-100" alt="" />
        <div class="carousel-caption" @click="navigateToLobby">
          <h5>친구들과 TRPG를 온라인으로 즐겨보세요</h5>
        </div>
      </div>
      <div class="carousel-item">
        <img src="@/assets/images/carousel/slide2.jpg" class="d-block w-100" alt="" />
        <div class="carousel-caption" @click="navigateToScenarios">
          <h5>내가 만들어가는 나만의 이야기</h5>
        </div>
      </div>
      <div class="carousel-item">
        <img src="@/assets/images/carousel/slide3.jpg" class="d-block w-100" alt="" />
        <div class="carousel-caption" @click="navigateToFindPlayers">
          <h5>새로운 동료를 찾아 보세요</h5>
        </div>
      </div>
    </div>
    <button
      class="carousel-control-prev"
      type="button"
      data-bs-target="#carouselExampleIndicators"
      data-bs-slide="prev"
    >
      <span class="carousel-control-prev-icon" aria-hidden="true"></span>
      <span class="visually-hidden">Previous</span>
    </button>
    <button
      class="carousel-control-next"
      type="button"
      data-bs-target="#carouselExampleIndicators"
      data-bs-slide="next"
    >
      <span class="carousel-control-next-icon" aria-hidden="true"></span>
      <span class="visually-hidden">Next</span>
    </button>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

const navigateToLobby = () => {
  router.push({ name: 'Lobby' });
};

const navigateToFindPlayers = () => {
  router.push({ name: 'FindPlayers' });
};

const navigateToScenarios = () => {
  router.push({ name: 'Scenarios' });
};
</script>

<style scoped>
.no-padding {
  padding: 0 !important;
  margin: 0 !important;
}

.carousel-item {
  height: 480px;
  background-color: #ababab;
}

.carousel-item img {
  object-fit: cover;
  height: 100%;
  width: 100%;
}

.carousel-caption {
  background: rgba(0, 0, 0, 0.7);
  padding: 10px;
  border-radius: 10px;
  max-width: 400px;
  margin: 0 auto;
  transition: transform 0.3s ease-in-out;
  cursor: pointer;
}

.carousel-caption:hover {
  transform: scale(1.05);
}

.carousel-caption h5 {
  color: #ffffff;
  font-size: 1.2rem;
  margin-bottom: 10px;
}

.carousel-caption .btn {
  color: #ffffff;
  text-decoration: none;
}
</style>