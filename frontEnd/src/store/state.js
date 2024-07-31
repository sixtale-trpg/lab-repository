// store/state.js
import { ref } from 'vue';

export const selectedPlayMemberID = ref(null); // 전역 상태로 선택된 플레이어 ID를 저장
export const selectedUserJob = ref(''); // 전역 상태로 선택된 유저의 직업 이름을 저장
