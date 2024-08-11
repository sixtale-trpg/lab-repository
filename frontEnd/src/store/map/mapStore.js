import { defineStore } from "pinia";

export const useMapStore = defineStore("map", {
  state: () => ({
    mapData: [], // API에서 가져온 맵 데이터 저장
    isLoading: false,
    error: null,
    selectedMap: null, // 선택한 맵을 저장할 상태
    selectedMapImage: null, // 선택한 맵의 이미지를 저장할 상태
    selectedMapId: null, // 선택한 맵의 ID를 저장할 상태
  }),

  actions: {
    // 선택한 맵과 ID를 설정하는 함수
    setSelectedMap(map) {
      this.selectedMap = map;
      this.selectedMapImage = map.imageURL; // 선택한 맵의 이미지를 저장
      this.selectedMapId = map.id; // 선택한 맵의 ID를 저장
    },

    // 특정 위치에 해당하는 맵을 가져오는 함수임
    getMapByPosition(row, column) {
      const map = this.mapData.find((m) =>
        m.cellList.some((cell) => cell.row === row && cell.column === column)
      );
      return map || null;
    },
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
