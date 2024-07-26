package org.infinity.sixtalebackend.domain.map.dto;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.infinity.sixtalebackend.domain.map.domain.Map;
import org.infinity.sixtalebackend.domain.scenario.domain.ScenarioNPC;

@Getter
@Setter
@Builder
public class NPCEventResponse {

    private Long id;
    private Map map;
    private String description;
    private ScenarioNPC scenarioNPC;
    private Integer currentHp;

}
