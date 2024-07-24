package org.infinity.sixtalebackend.domain.map.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GridCellEventID implements Serializable {
    private Long gridCellID;
    @Column(name="scenario_npc_id")
    private Long scenarioNPCID;
}
