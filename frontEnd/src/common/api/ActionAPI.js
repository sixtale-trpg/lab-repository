import axios from 'axios';

const BASE_URL = '/api/v1/rules';
const BASE_SHEETS_URL = '/api/v1/rooms';

// 토큰을 로컬 스토리지에서 가져오기
const getAccessToken = () => {
  return localStorage.getItem('accessToken');
};

// 공통 헤더 설정
const getHeaders = () => {
  const accessToken = getAccessToken();
  const headers = {
    'Content-Type': 'application/json'
  };
  if (accessToken) {
    headers['Authorization'] = `Bearer ${accessToken}`;
  }
  return headers;
};

// 공통 액션 목록 조회
export const getCommonActions = async (ruleID) => {
  try {
    const response = await axios.get(`${BASE_URL}/${ruleID}/actions`, {
      headers: getHeaders()
    });
    return response.data.data;
  } catch (error) {
    console.error('Error fetching common actions:', error);
    throw error;
  }
};

// 캐릭터 액션 목록 조회
export const getCharacterActions = async (roomID, playMemberID) => {
  try {
    const response = await axios.get(`${BASE_SHEETS_URL}/${roomID}/sheets/${playMemberID}/actions`, {
      headers: getHeaders()
    });
    return response.data.data;
  } catch (error) {
    console.error('Error fetching character actions:', error);
    throw error;
  }
};
