package org.infinity.sixtalebackend.domain.map.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.infinity.sixtalebackend.domain.map.domain.Map;
import org.infinity.sixtalebackend.domain.scenario.domain.Scenario;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class MapResponse {

    private Long id;
    private Scenario scenario;
    private String name;
    private String description;
    private boolean isNpc;
    private boolean isPlace;
    private String imageURL;
    private String bgmURL;
    private LocalDateTime createdAt;

}
