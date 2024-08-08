import axios from 'axios';

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

// 캐릭터 시트 조회
export const getCharacterSheet = async (roomID, playMemberID) => {
  const headers = getHeaders();
  const url = `${BASE_SHEETS_URL}/${roomID}/sheets/${playMemberID}`;
  console.log('Fetching character sheet:', url, headers);

  try {
    const response = await axios.get(url, {
      headers
    });
    console.log('Response:', response);
    return response.data.data;
  } catch (error) {
    console.error('Error fetching character sheet:', error);
    if (error.response) {
      console.error('Response data:', error.response.data);
    }
    throw error;
  }
};

// 캐릭터 시트 생성
export const createCharacterSheet = async (roomID, characterData) => {
  try {
    const response = await axios.post(`${BASE_SHEETS_URL}/${roomID}/sheets`, characterData, {
      headers: getHeaders()
    });
    return response.data;
  } catch (error) {
    console.error('Error creating character sheet:', error);
    throw error;
  }
};

// 캐릭터 시트 업데이트
export const updateCharacterSheet = async (roomID, playMemberID, updatedData) => {
  try {
    const response = await axios.patch(`${BASE_SHEETS_URL}/${roomID}/sheets/${playMemberID}`, updatedData, {
      headers: getHeaders()
    });
    return response.data;
  } catch (error) {
    console.error('Error updating character sheet:', error);
    throw error;
  }
};
