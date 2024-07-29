package org.infinity.sixtalebackend.domain.scenario.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.infinity.sixtalebackend.domain.member.domain.Member;
import org.infinity.sixtalebackend.domain.model.BaseTimeEntity;
import org.infinity.sixtalebackend.domain.rule.domain.Rule;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@Table(name = "scenario")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Scenario extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @ManyToOne
    @JoinColumn(name = "writer_id", nullable = false)
    private Member writer;

    @Column(nullable = false, length = 512)
    private String summary;

    @Lob
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer likes;

    @Column(nullable = false)
    @ColumnDefault("4")
    private Byte optimalCount;

    @Column(nullable = false)
    @ColumnDefault("true")
    private Boolean isPublic;

    @Column(nullable = false)
    @ColumnDefault("true")
    private Boolean isOpen;

    @ManyToOne
    @JoinColumn(name = "rule_id", nullable = false)
    private Rule rule;

    @Column(length = 255)
    private String imageURL;

    @Column(length = 255)
    private String gifURL;

}