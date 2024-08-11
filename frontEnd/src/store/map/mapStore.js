import { defineStore } from "pinia";
import { getMapList } from '@/common/api/RoomsAPI';

export const useMapStore = defineStore("map", {
  state: () => ({
    mapData: [], // API에서 가져온 맵 데이터 저장
    isLoading: false,
    error: null,
    selectedMap: null, // 선택한 맵을 저장할 상태
    selectedMapImage: null, // 선택한 맵의 이미지를 저장할 상태
  }),

  actions: {
    // 선택한 맵을 설정하는 함수
    setSelectedMap(map) {
      this.selectedMap = map;
      this.selectedMapImage = map.imageURL; // 선택한 맵의 이미지를 저장
    },

    // 특정 위치에 해당하는 맵을 가져오는 함수
    getMapByPosition(row, column) {
      const map = this.mapData.find((m) =>
        m.cellList.some((cell) => cell.row === row && cell.column === column)
      );
      return map || null;
    },

    // 실제 API를 통해 맵 데이터를 가져오는 함수
    async fetchMaps(roomID) {
      this.isLoading = true;
      try {
        const mapList = await getMapList(roomID);
        this.mapData = mapList.map((map) => ({
          id: map.id,
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
