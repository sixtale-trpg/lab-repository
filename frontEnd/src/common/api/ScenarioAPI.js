// src/api/ScenarioAPI.js
import axios from "axios";
import Cookies from "js-cookie";

const BASE_URL = "/api/v1/scenarios";

// 토큰을 로컬 스토리지에서 가져오기
const getAccessToken = () => {
  return Cookies.get("access-token");
};

// 공통 헤더 설정
const getHeaders = () => {
  const accessToken = getAccessToken();
  const headers = {
    "Content-Type": "application/json",
  };
  if (accessToken) {
    headers["Authorization"] = `Bearer ${accessToken}`;
  }
  return headers;
};

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
      `${BASE_URL}?genre=${genre}&title=${title}&page=${page}&size=${size}&sort=${sort}&order=${order}`,
      {
        headers: getHeaders(),
      }
    );
    return response.data.data;
  } catch (error) {
    console.error("Error fetching scenario info:", error);
    throw error;
  }
};

export const likeScenario = async (scenarioID) => {
  try {
    const response = await axios.post(`${BASE_URL}/${scenarioID}/like`, null, {
      headers: getHeaders(),
    });
    return response.data;
  } catch (error) {
    console.error("Error Scenario Like:", error);
    throw error;
  }
};

export const unlikeScenario = async (scenarioID) => {
  try {
    const response = await axios.delete(`${BASE_URL}/${scenarioID}/like`, {
      headers: getHeaders(),
    });
    return response.data;
  } catch (error) {
    console.error("Error Scenario Unlike:", error);
    throw error;
  }
};
