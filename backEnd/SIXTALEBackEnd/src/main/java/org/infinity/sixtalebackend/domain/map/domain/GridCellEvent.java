package org.infinity.sixtalebackend.domain.map.domain;

import jakarta.persistence.*;
import lombok.*;
import org.infinity.sixtalebackend.domain.scenario.domain.ScenarioNPC;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GridCellEvent {

    @EmbeddedId
    private GridCellEventID id;

    @ManyToOne
    @MapsId("gridCellID")
    @JoinColumn(name = "grid_cell_id", nullable = false)
    private GridCell gridCell;

    @ManyToOne
    @MapsId("scenarioNPCID")
    @JoinColumn(name = "scenario_npc_id", nullable = false)
    private ScenarioNPC scenarioNPC;

    @Column(nullable = false, length=100)
    private String title;

    private String description;

    @ManyToOne
    @JoinColumn(name = "map_id")
    private Map map;

}
