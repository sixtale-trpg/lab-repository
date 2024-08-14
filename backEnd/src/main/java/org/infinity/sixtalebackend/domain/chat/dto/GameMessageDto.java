package org.infinity.sixtalebackend.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Builder
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameMessageDto {
    @JsonProperty("gameType")
    private GameType gameType; // 메시지 유형 (예: GAME_START, MAP_CHANGE 등)

    @JsonProperty("createdAt")
    private LocalDateTime createdAt; // 메시지가 생성된 시간

    @JsonProperty("roomID")
    private Long roomID; // 게임 방 ID

    @JsonProperty("playMemberID")
    private Long playMemberID; // 플레이 멤버 ID (플레이어 또는 NPC 시트)

    @JsonProperty("currentMapID")
    private Long currentMapID; // 현재 맵 ID (맵 변경 시 사용)

    @JsonProperty("nextMapID")
    private Long nextMapID; // 다음 맵 ID (맵 변경 시 사용)

    @JsonProperty("statName")
    private String statName; // 능력치 이름 (능력치 변경 시 사용)

    @JsonProperty("currentStat")
    private Integer currentStat; // 현재 능력치 (능력치 변경 시 사용)

    @JsonProperty("updateStat")
    private Integer updateStat; // 업데이트된 능력치 (능력치 변경 시 사용)

    @JsonProperty("diceRolls")
    private List<DiceRollSetting> diceRolls; // 주사위 롤 설정 및 결과

    @JsonProperty("characterActionID")
    private Long characterActionID; // 액션 ID (액션 사용 시)

    @JsonProperty("currentWeight")
    private Integer currentWeight; // 현재 장비 무게 (장비 변경 시 사용)

    @JsonProperty("updateWeight")
    private Integer updateWeight; // 업데이트된 장비 무게 (장비 변경 시 사용)

    @JsonProperty("currentGold")
    private Integer currentGold; // 현재 골드 (골드 변경 시 사용)

    @JsonProperty("updateGold")
    private Integer updateGold; // 업데이트된 골드 (골드 변경 시 사용)

    @JsonProperty("events")
    private List<GameEvent> events; // 게임 이벤트 (여러 이벤트를 처리할 수 있음)

    @JsonProperty("tokens")
    private List<TokenMove> tokens; // 토큰 이동 (좌표 이동 시 사용)

    @JsonProperty("content")
    private String content; // 추가적인 설명 또는 메시지 내용

    // 복잡한 유형을 위한 내부 클래스들
    @Data
    @Builder
    public static class DiceRollSetting {
        @JsonProperty("type")
        private String type; // 주사위 유형 (예: D6, D10)
        @JsonProperty("count")
        private Integer count; // 주사위 수
        @JsonProperty("results")
        private List<Integer> results; // 주사위 결과 (주사위 롤 결과)
    }

    @Data
    @Builder
    public static class GameEvent {
        @JsonProperty("npcEventID")
        private Long npcEventID; // NPC 이벤트 ID (HP 변경 시 사용)

        @JsonProperty("currentHP")
        private Integer currentHP; // 현재 HP (이벤트 HP 변경 시 사용)

        @JsonProperty("updateHP")
        private Integer updateHP; // 업데이트된 HP (이벤트 HP 변경 시 사용)

        @JsonProperty("currentPosition")
        private Map<String, Integer> currentPosition; // 현재 위치 (좌표 이동 시 사용)

        @JsonProperty("updatePosition")
        private Map<String, Integer> updatePosition; // 업데이트된 위치 (좌표 이동 시 사용)
    }

    @Getter
    @Data
    @Builder
    public static class TokenMove {
        @JsonProperty("playMemberID")
        private Long playMemberID; // 플레이 멤버 ID (토큰 이동 시 사용)

        @JsonProperty("currentPosition")
        private Map<String, Integer> currentPosition;

        @JsonProperty("updatePosition")
        private Map<String, Integer> updatePosition;
    }
}
