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

// 게임 방 목록 조회
export const getRoomList = async (status = '', page = 0, size = 15, title = '') => {
  try {
    const response = await axios.get(`${BASE_URL}?status=${status}&page=${page}&size=${size}&title=${title}`, {
      headers: getHeaders()
    });
    console.log('Response data:', response.data);
    return response.data;
  } catch (error) {
    console.error('Error fetching room list:', error);
    throw error;
  }
};

// 게임 방 정보 조회
export const getRoomInfo = async (roomId) => {
  try {
    console.log('Calling getRoomInfo for roomId:', roomId);
    const response = await axios.get(`${BASE_URL}/${roomId}`, {
      headers: getHeaders()
    });
    console.log('getRoomInfo raw response:', response);
    console.log('getRoomInfo response data:', response.data);
    if (response.data && response.data.data) {
      console.log('Room info:', response.data.data);
    } else {
      console.log('Unexpected response structure in getRoomInfo');
    }
    return response.data.data;
  } catch (error) {
    console.error('Error in getRoomInfo:', error);
    console.error('Error response:', error.response);
    throw error;
  }
};

// 게임방에서 유저 강퇴
export const kickUserFromRoom = async (roomId, playerId) => {
  try {
    const response = await axios.delete(`${BASE_URL}/${roomId}/players`, {
      headers: getHeaders(),
      data: {
        playerId: playerId
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

// 맵 정보 조회
export const getMapInfo = async (roomId, mapId) => {
  try {
    const response = await axios.get(`${BASE_URL}/${roomId}/maps/${mapId}`, {
      headers: getHeaders()
    });
    return response.data.data;
  } catch (error) {
    console.error('Error fetching map info:', error);
    throw error;
  }
};

// 맵 장소 이벤트 목록 조회
export const getMapPlace = async (roomId, mapId) => {
  try {
    const response = await axios.get(`${BASE_URL}/${roomId}/maps/${mapId}/places`, {
      headers: getHeaders()
    });
    return response.data.data;
  } catch (error) {
    console.error('Error fetching map place info:', error);
    throw error;
  }
};

// 맵 NPC 이벤트 목록 조회
export const getMapNpcs = async (roomId, mapId) => {
  try {
    const response = await axios.get(`${BASE_URL}/${roomId}/maps/${mapId}/npcs`, {
      headers: getHeaders()
    });
    return response.data.data;
  } catch (error) {
    console.error('Error fetching NPC event list:', error);
    throw error;
  }
};
