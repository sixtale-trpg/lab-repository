import axios from "axios";
import Cookies from "js-cookie"; // 쿠키 다루기 위한 라이브러리

const BASE_URL = "/api/v1/members";

// 토큰을 쿠키에서 가져오기
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
//유저 id, nickName, image 가져오기
export const getMemberInfo = () => {
  return axios.get(`${BASE_URL}`, {
    headers: getHeaders(),
  });
};

// script 코드

// getMemberInfo()
// .then((response) => {
//   id.value = response.data.data.id;
//   nickName.value = response.data.data.nickName;
//   profileImage.value = response.data.data.imageURL;
//   console.log(id.value);
//   console.log(nickName.value);
//   console.log(profileImage.value);
// })
// .catch((error) => {
//   console.error("Failed to fetch member info:", error);
// });
