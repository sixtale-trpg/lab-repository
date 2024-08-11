// src/common/api/accountApi.js
import axios from "axios";
import Cookies from "js-cookie";

const BASE_URL = "/api/v1/members";

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

// 좋아요한 시나리오 조회
export const getLikedScenarioList = async () => {
  try {
    const response = await axios.get(`${BASE_URL}/liked-scenarios`, {
      headers: getHeaders(),
    });
    return response.data.data;
  } catch (error) {
    console.error("Error fetching liked scenario info:", error);
    throw error;
  }
};
