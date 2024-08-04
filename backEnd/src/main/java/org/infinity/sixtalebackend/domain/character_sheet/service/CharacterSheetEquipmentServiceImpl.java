package org.infinity.sixtalebackend.domain.character_sheet.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.infinity.sixtalebackend.domain.character_sheet.domain.CharacterEquipment;
import org.infinity.sixtalebackend.domain.character_sheet.domain.CharacterSheet;
import org.infinity.sixtalebackend.domain.character_sheet.dto.CharacterEquipmentRequest;
import org.infinity.sixtalebackend.domain.character_sheet.dto.CharacterEquipmentResponse;
import org.infinity.sixtalebackend.domain.character_sheet.dto.CharacterSheetEquipmentResponse;
import org.infinity.sixtalebackend.domain.character_sheet.repository.CharacterEquipmentRepository;
import org.infinity.sixtalebackend.domain.character_sheet.repository.CharacterSheetRepository;
import org.infinity.sixtalebackend.domain.equipment.domain.Equipment;
import org.infinity.sixtalebackend.domain.room.domain.PlayMember;
import org.infinity.sixtalebackend.domain.room.repository.PlayMemberRepository;
import org.infinity.sixtalebackend.domain.scenario.domain.ScenarioEquipment;
import org.infinity.sixtalebackend.domain.scenario.repository.ScenarioEquipmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class CharacterSheetEquipmentServiceImpl implements CharacterSheetEquipmentService{
    private final CharacterSheetRepository characterSheetRepository;
    private final ScenarioEquipmentRepository scenarioEquipmentRepository;
    private final CharacterEquipmentRepository characterEquipmentRepository;
    private final PlayMemberRepository playMemberRepository;

    @Override
    @Transactional(readOnly = true)
    public CharacterSheetEquipmentResponse getCharacterSheetEquipment(Long roomID, Long playMemberID) {
        CharacterSheet characterSheet = characterSheetRepository.findById(playMemberID)
                .orElseThrow(() -> new IllegalArgumentException(("Character Sheet not found")));
        List<ScenarioEquipment> equipmentList = scenarioEquipmentRepository.findByJobId(characterSheet.getJob().getId());

        List<CharacterSheetEquipmentResponse.EquipmentInfo> equipmentInfoList = equipmentList.stream()
                .map(equipment -> CharacterSheetEquipmentResponse.EquipmentInfo.builder()
                        .id(equipment.getId())
                        .name(equipment.getName())
                        .description(equipment.getDescription())
                        .typeID(equipment.getEquipmentType().getId())
                        .typeName(equipment.getEquipmentType().getName())
                        .weight(equipment.getWeight())
                        .currentCount(equipment.getCount())
                        .imageURL(equipment.getImageURL())
                        .build())
                .collect(Collectors.toList());

        return CharacterSheetEquipmentResponse.builder()
                .characterEquipment(equipmentInfoList)
                .build();
    }

    @Override
    @Transactional
    public void addCharacterEquipment(Long roomID, Long playMemberID, List<CharacterEquipmentRequest> equipmentRequests) {
        PlayMember playMember = playMemberRepository.findByIdAndRoomId(playMemberID, roomID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid PlayMember or Room ID"));
        CharacterSheet characterSheet = characterSheetRepository.findById(playMemberID)
                .orElseThrow(() -> new IllegalArgumentException("Character Sheet not found"));

        int totalWeightToAdd = 0;

        for (CharacterEquipmentRequest equipmentRequest : equipmentRequests) {
            ScenarioEquipment equipment = scenarioEquipmentRepository.findById(equipmentRequest.getEquipmentId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Equipment ID: " + equipmentRequest.getEquipmentId()));

            CharacterEquipment newEquipment = CharacterEquipment.builder()
                    .playMember(playMember)
                    .equipment(equipment)
                    .currentCount(equipmentRequest.getCurrentCount())
                    .weight(equipmentRequest.getWeight())
                    .build();
            characterEquipmentRepository.save(newEquipment);

            //새로 추가되는 무게들의 합
            totalWeightToAdd += equipmentRequest.getWeight();
        }

        characterSheet.setCurrentWeight(characterSheet.getCurrentWeight() + totalWeightToAdd);
        characterSheetRepository.save(characterSheet);
    }

    /**
     * 캐릭터 장비 삭제
     */
    @Override
    @Transactional
    public void deleteCharacterEquipment(Long roomID, Long playMemberID, Long equipmentID) {
        PlayMember playMember = playMemberRepository.findByIdAndRoomId(playMemberID, roomID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid PlayMember or Room ID"));
        CharacterEquipment equipment = characterEquipmentRepository.findByPlayMemberAndEquipment_Id(playMember, equipmentID)
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found"));
        
        characterEquipmentRepository.delete(equipment);

        //삭제된 장비의 무게를 현재 무게에서 뺌
        CharacterSheet characterSheet = characterSheetRepository.findById(playMemberID)
                .orElseThrow(() -> new IllegalArgumentException("Character Sheet not found"));
        characterSheet.setCurrentWeight(characterSheet.getCurrentWeight() - equipment.getWeight());
        characterSheetRepository.save(characterSheet);

    }
}
