package org.infinity.sixtalebackend.domain.character_sheet.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CharacterSheetEquipmentResponse {
    private List<EquipmentInfo> characterEquipment;

    @Getter
    @Builder
    public static class EquipmentInfo {
        private Long id;
        private String name;
        private String description;
        private Long typeID;
        private String typeName;
        private Integer weight;
        private Integer currentCount;
        private String imageURL;
    }

}
