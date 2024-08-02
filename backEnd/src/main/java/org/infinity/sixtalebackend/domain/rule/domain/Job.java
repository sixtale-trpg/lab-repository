package org.infinity.sixtalebackend.domain.rule.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.infinity.sixtalebackend.domain.model.DiceType;

@Entity
@Getter
@Builder
@Table(name = "job")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer hp;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer weight;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DiceType diceType;

    private String imageURL;

    @ManyToOne
    @JoinColumn(name = "rule_id")
    private Rule rule;

    @PrePersist
    public void prePersist() {
        if (diceType == null) {
            diceType = DiceType.D6;
        }

    }
}