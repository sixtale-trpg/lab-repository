<template>
  <div id="carouselExampleIndicators" class="carousel slide no-padding" data-bs-ride="carousel">
    <!-- <div class="carousel-indicators">
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
    </div> -->
    <div class="carousel-inner">
      <div class="carousel-item active">
        <img src="@/assets/images/carousel/slide4.png" class="d-block w-100" alt="" />
        <div class="carousel-caption" @click="navigateToLobby">
      </div>
      </div>
      <div class="carousel-item">
        <img src="@/assets/images/carousel/slide4.png" class="d-block w-100" alt="" />
        <div class="carousel-caption" @click="navigateToScenarios">
        </div>
      </div>
      <div class="carousel-item">
        <img src="@/assets/images/carousel/slide4.png" class="d-block w-100" alt="" />
        <div class="carousel-caption" @click="navigateToFindPlayers">
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

.carousel-indicators{
  margin-bottom: 0px;
  transform: translateY(-90%); 
}

.carousel-indicators button {
  width: 110px; /* Adjust width */
  height: 12px; /* Adjust height */
  /* border-radius: 11px !important; */
  margin-right: 1%;
}

.carousel-item {
  height: 550px;
  /* background-color: #ffffff; */
  background: linear-gradient(270deg, rgba(38, 38, 38, 0.45) 65%, #0e0e15 100%), linear-gradient(89.84deg, rgba(56, 56, 56, 0.9) 65.72%, #0e0e15 100%);
}

.carousel-item img {
  /* object-fit: cover; */
  object-fit: fill;
  height: 100%;
  width: 100%;
  border-radius: 30px;
  filter: drop-shadow(0px 4px 4px rgba(0, 0, 0, 0.25));
}

.carousel-caption {
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