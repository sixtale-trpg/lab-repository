package org.infinity.sixtalebackend.domain.equipment.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.infinity.sixtalebackend.domain.equipment.domain.EquipmentType;
import org.infinity.sixtalebackend.domain.rule.domain.Job;
import org.infinity.sixtalebackend.domain.rule.domain.Rule;

@Entity
@Getter
@Builder
@Table(name="equipment")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
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
    @JoinColumn(name = "rule_id")
    private Rule rule;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    private String imageUrl;
}
