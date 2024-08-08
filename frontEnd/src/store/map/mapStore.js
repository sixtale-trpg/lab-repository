import { defineStore } from "pinia";
import axios from "axios";

export const useMapStore = defineStore("map", {
  state: () => ({
    mapData: [], // API에서 가져온 맵 데이터 저장
    isLoading: false,
    error: null,
    selectedMap: null, // 선택한 맵을 저장할 상태
  }),

  actions: {
    // 더미 데이터를 로드하는 함수
    // 더미 데이터 설정에 cellList가 포함되어 있는지 확인
    loadDummyData() {
      this.isLoading = true;
      try {
        // 더미 데이터 설정
        this.mapData = [
          {
            id: 1,
            name: "영캡슨 빌리지",
            description: "평화로운 마을",
            imageURL: require("@/assets/images/maps/map1.png"),
            cellList: [{ row: 2, column: 3 }], // 좌표 추가
          },
          {
            id: 2,
            name: "브랑뉴 밸리",
            description: "넓고 푸른 계곡",
            imageURL: require("@/assets/images/maps/map14.png"),
            cellList: [{ row: 4, column: 5 }], // 좌표 추가
          },
          {
            id: 3,
            name: "콜든 필드 야영지",
            description: "여름의 캠프장",
            imageURL: require("@/assets/images/maps/map15.png"),
            cellList: [{ row: 5, column: 6 }, { row: 6, column: 7 }], // 좌표 추가
          },
          {
            id: 4,
            name: "웨드 우드",
            description: "울창한 숲",
            imageURL: require("@/assets/images/maps/map4.png"),
            cellList: [{ row: 3, column: 2 }], // 좌표 추가
          },
          {
            id: 5,
            name: "터틀 아일랜드",
            description: "거북이 섬",
            imageURL: require("@/assets/images/maps/map5.png"),
            cellList: [{ row: 1, column: 1 }, { row: 7, column: 7 }], // 좌표 추가
          },
          {
            id: 6,
            name: "언더필드 S번가",
            description: "번화가",
            imageURL: require("@/assets/images/maps/map6.png"),
            cellList: [{ row: 2, column: 4 }, { row: 3, column: 3 }], // 좌표 추가
          },
          {
            id: 7,
            name: "주점",
            description: "사람들이 모이는 곳",
            imageURL: require("@/assets/images/maps/map7.png"),
            cellList: [{ row: 4, column: 4 }, { row: 8, column: 8 }], // 좌표 추가
          },
          {
            id: 8,
            name: "일로이와 크세핀",
            description: "신비한 장소",
            imageURL: require("@/assets/images/maps/map8.png"),
            cellList: [{ row: 5, column: 5 }], // 좌표 추가
          },
          {
            id: 9,
            name: "챈슬러",
            description: "고급 주거지",
            imageURL: require("@/assets/images/maps/map9.png"),
            cellList: [{ row: 6, column: 6 }], // 좌표 추가
          },
          {
            id: 10,
            name: "행크 슈레이더",
            description: "모험의 시작",
            imageURL: require("@/assets/images/maps/map10.png"),
            cellList: [{ row: 7, column: 1 }], // 좌표 추가
          },
          {
            id: 11,
            name: "칼크 로드 갈림길",
            description: "여러 길이 갈라지는 곳",
            imageURL: require("@/assets/images/maps/map11.png"),
            cellList: [{ row: 8, column: 2 }, { row: 9, column: 3 }], // 좌표 추가
          },
          {
            id: 12,
            name: "폭마룡 오시리스",
            description: "강력한 마룡이 사는 곳",
            imageURL: require("@/assets/images/maps/map13.png"),
            cellList: [{ row: 1, column: 2 }], // 좌표 추가
          },
          {
            id: 13,
            name: "노스힐",
            description: "추운 북쪽 언덕",
            imageURL: require("@/assets/images/maps/map12.png"),
            cellList: [{ row: 9, column: 9 }], // 좌표 추가
          },
          {
            id: 14,
            name: "리버 사이드",
            description: "강 옆의 마을",
            imageURL: require("@/assets/images/maps/map1.png"),
            cellList: [{ row: 3, column: 6 }], // 좌표 추가
          },
          {
            id: 15,
            name: "미드필드 언덕",
            description: "초록빛 언덕",
            imageURL: require("@/assets/images/maps/map3.png"),
            cellList: [{ row: 5, column: 8 }], // 좌표 추가
          },
          {
            id: 16,
            name: "사막의 오아시스",
            description: "사막 한가운데 오아시스",
            imageURL: require("@/assets/images/maps/map2.png"),
            cellList: [{ row: 2, column: 2 }], // 좌표 추가
          },
        ];
      } catch (error) {
        this.error = error;
        console.error("더미 데이터를 로드하는 중 오류 발생:", error);
      } finally {
        this.isLoading = false;
      }
    },
    

    // 선택한 맵을 설정하는 함수
    setSelectedMap(map) {
      this.selectedMap = map;
    },

    // 특정 위치에 해당하는 맵을 가져오는 함수
    getMapByPosition(row, column) {
      // row와 column에 맞는 맵 찾기
      const map = this.mapData.find((m) =>
        m.cellList.some((cell) => cell.row === row && cell.column === column)
      );
      return map || null;
    },

    // 실제 API를 통해 맵 데이터를 가져오는 함수
    async fetchMaps(roomID, mapID) {
      this.isLoading = true;
      try {
        const response = await axios.get(
          `http://i11d108.p.ssafy.io:8888/api/v1/rooms/${roomID}/maps/${mapID}`
        );
        // API 응답 구조에 맞춰 데이터를 저장
        this.mapData = response.data.map((map) => ({
          id: map.id, // 고유 식별자가 필요할 경우 추가
          name: map.name,
          description: map.description,
          imageURL: map.imageURL,
          bgmURL: map.bgmURL,
          cellList: map.cellList.map((cell) => ({
            row: cell.row,
            column: cell.column,
            description: cell.description,
            scenarioEvent: cell.scenarioEvent.map((event) => ({
              id: event.id,
              title: event.title,
              name: event.name,
              description: event.description,
              npcDescription: event.npcDescription,
              hp: event.hp,
              glove: event.glove,
              imageURL: event.imageURL,
            })),
          })),
        }));
      } catch (error) {
        this.error = error;
        console.error("맵 데이터를 가져오는 중 오류 발생:", error);
      } finally {
        this.isLoading = false;
      }
    },
  },
});
