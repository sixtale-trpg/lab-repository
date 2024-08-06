package org.infinity.sixtalebackend.domain.character_sheet.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.infinity.sixtalebackend.domain.character_sheet.domain.CharacterAction;
import org.infinity.sixtalebackend.domain.character_sheet.domain.CharacterSheet;
import org.infinity.sixtalebackend.domain.character_sheet.dto.ActionOptionResponse;
import org.infinity.sixtalebackend.domain.character_sheet.dto.CharacterActionListResponse;
import org.infinity.sixtalebackend.domain.character_sheet.dto.CharacterActionResponse;
import org.infinity.sixtalebackend.domain.character_sheet.repository.CharacterActionRepository;
import org.infinity.sixtalebackend.domain.character_sheet.repository.CharacterSheetRepository;
import org.infinity.sixtalebackend.domain.room.domain.PlayMember;
import org.infinity.sixtalebackend.domain.room.repository.PlayMemberRepository;
import org.infinity.sixtalebackend.domain.rule.domain.ActionOption;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class CharacterActionServiceImpl implements CharacterActionService{
    private final PlayMemberRepository playMemberRepository;
    private final CharacterSheetRepository characterSheetRepository;
    private final CharacterActionRepository characterActionRepository;

    /**
     * 캐릭터 액션 목록 조회
     */
    @Override
    @Transactional(readOnly = true)
    public CharacterActionListResponse getCharacterActions(Long roomID, Long playMemberID) {
        // playMember와 CharacterSheet 조회
        PlayMember playMember = playMemberRepository.findByIdAndRoomId(playMemberID, roomID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid PlayMember or Room ID"));

        CharacterSheet characterSheet = characterSheetRepository.findByPlayMember(playMember)
                .orElseThrow(() -> new IllegalArgumentException("Character Sheet not found"));

        // 캐릭터 액션 조회
        List<CharacterAction> characterActions = characterActionRepository.findByCharacterSheet(characterSheet);

        // coreActions와 AdvancedActions로 나누기
        List<CharacterActionResponse> coreActions = characterActions.stream()
                .filter(action -> action.getJobAction().getIsCore()) // isCore가 true
                .map(this::mapToCharacterActionResponse)
                .collect(Collectors.toList());

        List<CharacterActionResponse> advancedActions = characterActions.stream()
                .filter(action -> !action.getJobAction().getIsCore())
                .map(this::mapToCharacterActionResponse)
                .collect(Collectors.toList());

        return CharacterActionListResponse.builder()
                .coreActions(coreActions)
                .advancedActions(advancedActions)
                .build();
    }

    // CharacteAction 엔티티를 CharacterActionResponse로 변환
    private CharacterActionResponse mapToCharacterActionResponse(CharacterAction action) {
        return CharacterActionResponse.builder()
                .id(action.getId())
                .actionID(action.getJobAction().getId())
                .name(action.getJobAction().getName())
                .isCore(action.getJobAction().getIsCore())
                .description(action.getJobAction().getDescription())
                .isDice(action.getJobAction().getIsDice())
                .diceType(action.getJobAction().getDiceType() != null ? action.getJobAction().getDiceType() : null)
                .diceCount(action.getJobAction().getDiceCount())
                .level(action.getJobAction().getLevel())
                .actionOption(action.getActionOption() != null ?
                        List.of(mapToActionOptionResponse(action.getActionOption())) :
                        List.of())
                .build();
    }

    // ActionOption 엔티티를 ActionOptionResponse로 변환
    private ActionOptionResponse mapToActionOptionResponse(ActionOption option) {
        return ActionOptionResponse.builder()
                .id(option.getId())
                .content(option.getContent())
                .build();
    }


}
