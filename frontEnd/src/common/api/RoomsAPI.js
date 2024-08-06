import axios from 'axios';

const accessToken = 'YOUR_ACCESS_TOKEN'; // 실제 토큰으로 대체하세요

const apiClient = axios.create({
  baseURL: 'https://i11d108.p.ssafy.io/api/v1', 
  headers: {
    'Content-Type': 'application/json',
    Authorization: `Bearer ${accessToken}`
  }
});

export const fetchRooms = async (page = 0, size = 15) => {
  try {
    const statuses = ['WAITING', 'PLAYING', 'UPCOMING'];
    const response = await apiClient.get('/rooms', {
      params: { statuses: statuses.join(','), page, size }
    });
    console.log('Rooms fetched:', response.data.data.content); // 콘솔 로그 추가
    return response.data.data.content;
  } catch (error) {
    console.error('Error fetching rooms:', error);
    throw error;
  }
};

// 참가한 방 목록을 가져오는 함수
export const fetchJoinedRooms = async () => {
  try {
    const response = await apiClient.get('/user/rooms'); // 실제 API 경로로 대체하세요
    console.log('Joined rooms fetched:', response.data.data); // 콘솔 로그 추가
    return response.data.data; // 실제 응답 데이터 구조에 맞게 수정하세요
  } catch (error) {
    console.error('Error fetching joined rooms:', error);
    throw error;
  }
};
