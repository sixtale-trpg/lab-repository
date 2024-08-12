package org.infinity.sixtalebackend.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.infinity.sixtalebackend.domain.character_sheet.domain.CharacterAction;
import org.infinity.sixtalebackend.domain.character_sheet.domain.CharacterSheet;
import org.infinity.sixtalebackend.domain.character_sheet.repository.CharacterActionRepository;
import org.infinity.sixtalebackend.domain.character_sheet.repository.CharacterSheetRepository;
import org.infinity.sixtalebackend.domain.chat.dto.GameMessageDto;
import org.infinity.sixtalebackend.domain.chat.repository.PlayGameLogRepository;
import org.infinity.sixtalebackend.domain.map.domain.Map;
import org.infinity.sixtalebackend.domain.map.repository.MapRepository;
import org.infinity.sixtalebackend.domain.member.domain.Member;
import org.infinity.sixtalebackend.domain.room.repository.RoomRepository;
import org.infinity.sixtalebackend.infra.redis.service.RedisPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayGameLogServiceImpl implements PlayGameLogService {
    private final PlayGameLogRepository playGameLogRepository;
    private final RedisPublisher redisPublisher;
    private final ChatRoomService chatRoomService;
    private final RoomRepository roomRepository;
    private final MapRepository mapRepository;
    private final CharacterSheetRepository characterSheetRepository;
    private final CharacterActionRepository characterActionRepository;

    @Override
    public void sendPlayGameLogMessage(GameMessageDto messageRequest) {
        String content = createContent(messageRequest);
        messageRequest.setContent(content);

        // Save to DB
        // playGameLogRepository.save(convertToEntity(messageRequest));

        // Send WebSocket message
        // redisPublisher.publish(chatRoomService.getTopic(String.valueOf(messageRequest.getRoomID())),messageRequest);
    }

    private String createContent(GameMessageDto messageRequest) {
        switch (messageRequest.getGameType()) {
            case MAP_CHANGE:
                return String.format("MAP : %d(%s) -> %d(%s)",
                        messageRequest.getCurrentMapID(),
                        findMap(messageRequest.getCurrentMapID()).getName(), // 실제 맵 이름 조회
                        messageRequest.getNextMapID(),
                        findMap(messageRequest.getNextMapID()).getName()); // 실제 맵 이름 조회
            case STAT_CHANGE:
                return String.format("[%s] %s : %d -> %d",
                        findSheet(messageRequest.getSheetID()).getName(), // 실제 플레이어 이름 조회
                        messageRequest.getStatName(),
                        messageRequest.getCurrentStat(),
                        messageRequest.getUpdateStat());
            case DICE_SETTING:
                return String.format("[%s] DICE_ROLL_SETTING : %s",
                        findSheet(messageRequest.getSheetID()).getName(), // 실제 플레이어 이름 조회
                        diceRollsToString(messageRequest.getDiceRolls()));
            case DICE_ROLL:
                // 결과 계산
                Integer calculatedTotalResult = messageRequest.getDiceRolls().stream()
                        .flatMap(roll -> roll.getResults().stream())
                        .mapToInt(Integer::intValue)
                        .sum();

                return String.format("[%s] DICE_ROLL : %s ＞ %s",
                        findSheet(messageRequest.getSheetID()).getName(), // 실제 플레이어 이름 조회
                        diceRollsToString(messageRequest.getDiceRolls()),
                        diceRollResultsToString(messageRequest.getDiceRolls(), calculatedTotalResult));
            case ACTION:
                return String.format("[%s] ACTION : %d(%s)",
                        findSheet(messageRequest.getSheetID()).getName(), // 실제 플레이어 이름 조회
                        messageRequest.getCharacterActionID());
            case WEIGHT:
                return String.format("[%s] WEIGHT : %d -> %d",
                        findSheet(messageRequest.getSheetID()).getName(), // 실제 플레이어 이름 조회
                        messageRequest.getCurrentWeight(),
                        messageRequest.getUpdateWeight());
            case GOLD:
                return String.format("[%s] GOLD : %d -> %d",
                        findSheet(messageRequest.getSheetID()).getName(), // 실제 플레이어 이름 조회
                        messageRequest.getCurrentGold(),
                        messageRequest.getUpdateGold());
            case EVENT_HP_CHANGE:
                return createHpEventContent(messageRequest.getEvents());
//            case MAP_MOVEMENT:
//                return createTokenMoveContent(messageRequest.getEvents());
            case GAME_END:
                return "Game has ended";
            default:
                throw new IllegalArgumentException("Unsupported game type: " + messageRequest.getGameType());
        }
    }

    private String diceRollsToString(List<GameMessageDto.DiceRollSetting> diceRolls) {
        // Check if the diceRolls list is null or empty
        if (diceRolls == null || diceRolls.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < diceRolls.size(); i++) {
            GameMessageDto.DiceRollSetting rollSetting = diceRolls.get(i);
            String type = rollSetting.getType(); // Get the dice type (e.g., D6, D10)
            Integer count = rollSetting.getCount(); // Get the count of dice

            sb.append(count).append(type);

            if (i < diceRolls.size() - 1) {
                sb.append(" + ");
            }
        }

        return sb.toString();
    }


    private String diceRollResultsToString(List<GameMessageDto.DiceRollSetting> diceRolls, Integer totalResult) {
        if (diceRolls == null || diceRolls.isEmpty()) {
            return "";
        }

        // Convert dice rolls to a readable string
        StringBuilder rollDescription = new StringBuilder();
        StringBuilder resultDetails = new StringBuilder();
        int rollSum = 0;

        // Process each dice roll setting
        for (int i = 0; i < diceRolls.size(); i++) {
            GameMessageDto.DiceRollSetting rollSetting = diceRolls.get(i);
            String type = rollSetting.getType(); // e.g., D6, D10
            Integer count = rollSetting.getCount(); // Number of dice
            List<Integer> results = rollSetting.getResults(); // Roll results

            // Add the roll description (e.g., 4D6)
            rollDescription.append(count).append(type);

            // Add detailed results (e.g., 1[1,1,3,3])
            if (results != null && !results.isEmpty()) {
                resultDetails.append(" ").append(results.size()).append("[").append(String.join(",", results.stream().map(String::valueOf).toArray(String[]::new))).append("]");
            }

            // Add a plus sign if it's not the last item
            if (i < diceRolls.size() - 1) {
                rollDescription.append(" + ");
            }

            // Calculate the sum of the results
            if (results != null) {
                rollSum += results.stream().mapToInt(Integer::intValue).sum();
            }
        }

        // Append roll description with total result
        return String.format("%s (%s) ＞ %s ＞ %d",
                rollDescription.toString(),
                rollDescription.toString(),
                resultDetails.toString().trim(),
                totalResult != null ? totalResult : rollSum
        );
    }



    private String createHpEventContent(List<GameMessageDto.GameEvent> events) {
        if (events == null || events.isEmpty()) {
            return "";
        }

        StringBuilder contentBuilder = new StringBuilder();

        // Iterate over the list of events
        for (GameMessageDto.GameEvent event : events) {
            // Extract NPC ID, current HP, and updated HP
            Long npcEventID = event.getNpcEventID();
            Integer currentHP = event.getCurrentHP();
            Integer updateHP = event.getUpdateHP();

            // Format the event details
            if (npcEventID != null && currentHP != null && updateHP != null) {
                contentBuilder.append(String.format("[%d] HP : %d → %d\n", npcEventID, currentHP, updateHP));
            }
        }

        // Convert the StringBuilder to String and return it
        return contentBuilder.toString().trim();
    }


//    private String createTokenMoveContent(List<GameMessageDto.GameEvent> events) {
//        // Create token move content
//    }

//    private GameMessageEntity convertToEntity(GameMessageDto dto) {
//        // Convert DTO to Entity
//    }

    private Boolean findRoom(Long roomID){
        boolean existRoom = roomRepository.existsById(roomID);
        if(!existRoom) throw new IllegalArgumentException("해당 방이 존재하지 않습니다. id=" + roomID);
        return true;
    }

    private Map findMap(Long mapID){
        return mapRepository.findById(mapID)
                .orElseThrow(() -> new IllegalArgumentException("해당 맵이 존재하지 않습니다. id=" + mapID));
    }

    private CharacterSheet findSheet(Long sheetID){
        return characterSheetRepository.findById(sheetID)
                .orElseThrow(() -> new IllegalArgumentException("해당 캐릭터 시트가 존재하지 않습니다. id=" + sheetID));
    }

    private CharacterAction findAction(Long characterActionID){
        return characterActionRepository.findById(characterActionID)
                .orElseThrow(() -> new IllegalArgumentException("해당 캐릭터 액션이 존재하지 않습니다. id=" + characterActionID));
    }
}
