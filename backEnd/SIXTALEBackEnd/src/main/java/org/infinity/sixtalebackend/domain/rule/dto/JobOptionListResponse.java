package org.infinity.sixtalebackend.domain.rule.dto;

import lombok.Getter;
import lombok.Setter;
import org.infinity.sixtalebackend.domain.equipment.domain.Equipment;
import org.infinity.sixtalebackend.domain.rule.domain.*;

import java.util.List;

@Getter
@Setter
public class JobOptionListResponse {
    private List<List<JobRace>> jobRaceList;
    private List<List<JobBelief>> jobBeliefList;
    private List<List<Equipment>> jobEquipmentList;
    private List<JobAction> jobActionList;
    private List<List<ActionOption>> actionOptionList;
}
