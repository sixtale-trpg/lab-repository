import axios from 'axios';

const BASE_URL = '/api/v1/rooms';

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

// 게임방 정보 조회
export const getRoomInfo = async (roomId) => {
  try {
    const response = await axios.get(`${BASE_URL}/${roomId}`, {
      headers: getHeaders()
    });
    return response.data.data;
  } catch (error) {
    console.error('Error fetching room info:', error);
    throw error;
  }
};

// 게임방에서 유저 강퇴
export const kickUserFromRoom = async (roomId, memberId) => {
  try {
    const response = await axios.delete(`${BASE_URL}/${roomId}/players`, {
      headers: getHeaders(),
      data: {
        memberId: memberId
      }
    });
    return response.data;
  } catch (error) {
    console.error('Error kicking user:', error);
    throw error;
  }
};

// 맵 목록 조회
export const getMapList = async (roomId) => {
  try {
    const response = await axios.get(`${BASE_URL}/${roomId}/maps`, {
      headers: getHeaders()
    });
    return response.data.data;
  } catch (error) {
    console.error('Error fetching map list:', error);
    throw error;
  }
};
