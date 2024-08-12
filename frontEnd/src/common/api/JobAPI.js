import axios from 'axios';

const BASE_SHEETS_URL = '/api/v1/rules';

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

// 직업 목록 조회 API 요청
export const fetchJobList = async (ruleId) => {
    try {
      const response = await axios.get(`${BASE_SHEETS_URL}/${ruleId}/jobs`, {
        headers: getHeaders(),
      });
      if (response.data.statusCode === 200) {
        return response.data.data.jobList.filter((job) => job.id >= 1 && job.id <= 8);
      } else {
        throw new Error('Failed to fetch job list');
      }
    } catch (error) {
      console.error('직업 목록 조회 중 오류 발생:', error);
      throw error;
    }
  };