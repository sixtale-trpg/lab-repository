// src/api/ScenarioAPI.js
import axios from 'axios';

const BASE_URL = '/api/v1/scenarios';

// 토큰을 로컬 스토리지에서 가져오기
const getAccessToken = () => {
  return localStorage.getItem('accessToken');
};

// 시나리오 목록 조회
export const getScenarioList = async () => {
  try {
    const response = await axios.get(`${BASE_URL}`);
    return response.data.data;
  } catch (error) {
    console.error('Error fetching room info:', error);
    throw error;
  }
};

