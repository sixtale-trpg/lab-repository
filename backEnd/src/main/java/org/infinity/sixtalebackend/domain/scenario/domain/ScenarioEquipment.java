package org.infinity.sixtalebackend.domain.scenario.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.infinity.sixtalebackend.domain.equipment.domain.EquipmentType;
import org.infinity.sixtalebackend.domain.rule.domain.Job;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScenarioEquipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private EquipmentType equipmentType;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer weight;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer count;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne
    @JoinColumn(name = "scenario_id", nullable = false)
    private Scenario scenario;

    private String imageURL;
}