package org.infinity.sixtalebackend.domain.rule.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.infinity.sixtalebackend.domain.equipment.domain.Equipment;
import org.infinity.sixtalebackend.domain.rule.domain.*;

import java.util.List;

@Getter
@Setter
@Builder
public class JobOptionListResponse {
    private List<JobRace> jobRaceList;
    private List<JobBelief> jobBeliefList;
    private List<Equipment> jobEquipmentList;
    private List<JobAction> jobActionList;
    private List<List<ActionOption>> actionOptionList;
}
