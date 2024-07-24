package org.infinity.sixtalebackend.domain.scenario.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScenarioNPC {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "scenario_id", nullable = false)
    private Scenario scenario;

    @Column(nullable = false, length = 50)
    private String name;

    private String description;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer hp;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer glove;

    private String imageURL;
}
