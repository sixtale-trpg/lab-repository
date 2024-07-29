package org.infinity.sixtalebackend.domain.npc.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.infinity.sixtalebackend.domain.rule.domain.Rule;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NPC {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Lob
    private String description;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer hp;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer glove;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "rule_id", nullable = false)
    private Rule rule;
}