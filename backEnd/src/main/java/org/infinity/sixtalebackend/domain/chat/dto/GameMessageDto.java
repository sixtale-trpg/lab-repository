package org.infinity.sixtalebackend.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) //
public class GameMessageDto {
    @JsonProperty("type")
    private MessageType type; // 메시지 유형 (예: GAME_START, MAP_CHANGE 등)

    @JsonProperty("timestamp")
    private LocalDateTime timestamp; // 메시지가 생성된 시간

    @JsonProperty("gameId")
    private String gameId; // 게임 ID

    @JsonProperty("playerId")
    private String playerId; // 플레이어 ID (해당되는 경우)

    @JsonProperty("map")
    private String map; // 새로운 맵 (해당되는 경우)

    @JsonProperty("stats")
    private Map<String, Integer> stats; // 능력치 변경 (예: {"strength": 10, "agility": 5})

    @JsonProperty("diceRoll")
    private DiceRoll diceRoll; // 주사위 롤 세부 사항

    @JsonProperty("action")
    private String action; // 사용된 액션 (해당되는 경우)

    @JsonProperty("equipment")
    private EquipmentChange equipment; // 장비 변경 세부 사항

    @JsonProperty("event")
    private GameEvent event; // 이벤트 세부 사항

    @JsonProperty("level")
    private Integer level; // 레벨 정보 (해당되는 경우)

    @JsonProperty("npc")
    private NpcAction npcAction; // NPC 동작 (이동, 생성, 삭제)

    // 복잡한 유형을 위한 내부 클래스들
    @Data
    @Builder
    public static class DiceRoll {
        @JsonProperty("type")
        private String type; // 주사위 유형 (예: 1D6+1D10)
        @JsonProperty("result")
        private Integer result; // 롤 결과
        @JsonProperty("details")
        private List<Integer> details; // 롤의 세부 결과
    }

    @Data
    @Builder
    public static class EquipmentChange {
        @JsonProperty("action")
        private String action; // 장비에 대한 액션 (변경, 추가, 제거)
        @JsonProperty("equipmentId")
        private String equipmentId; // 장비 ID
        @JsonProperty("weight")
        private Integer weight; // 장비의 무게 (해당되는 경우)
    }

    @Data
    @Builder
    public static class GameEvent {
        @JsonProperty("eventType")
        private String eventType; // 이벤트 유형
        @JsonProperty("details")
        private Map<String, Object> details; // 이벤트 세부 사항
    }

    @Data
    @Builder
    public static class NpcAction {
        @JsonProperty("actionType")
        private String actionType; // 동작 유형 (이동, 생성, 삭제)
        @JsonProperty("npcId")
        private String npcId; // NPC ID
        @JsonProperty("position")
        private Map<String, Integer> position; // 이동할 경우 새 위치 (x, y)
    }

    public enum MessageType {
        GAME_START,
        MAP_CHANGE,
        STAT_CHANGE,
        DICE_ROLL,
        ACTION_USE,
        EQUIPMENT_CHANGE,
        EVENT,
        LEVEL_UP,
        GAME_END,
        MAP_MOVEMENT,
        DICE_SETTING,
        HP_EVENT,
        EQUIPMENT_MODIFY,
        NPC_MOVE,
        NPC_CREATE,
        NPC_DELETE
    }
}
