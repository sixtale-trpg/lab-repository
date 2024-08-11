// src/api/ScenarioAPI.js
import axios from "axios";

const BASE_URL = "/api/v1/scenarios";

// 시나리오 목록 조회
export const getScenarioList = async (
  genre,
  title,
  page,
  size,
  sort,
  order
) => {
  try {
    const response = await axios.get(
      `${BASE_URL}?genre=${genre}&title=${title}&page=${page}&size=${size}&sort=${sort}&order=${order}`
    );
    return response.data.data;
  } catch (error) {
    console.error("Error fetching scenario info:", error);
    throw error;
  }
};
