package org.infinity.sixtalebackend.domain.character_sheet.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.infinity.sixtalebackend.domain.member.domain.Member;
import org.infinity.sixtalebackend.domain.room.domain.PlayMember;
import org.infinity.sixtalebackend.domain.rule.domain.Belief;
import org.infinity.sixtalebackend.domain.rule.domain.Job;
import org.infinity.sixtalebackend.domain.rule.domain.Race;

@Entity
@Getter
@Builder
@Table(name = "character_sheet")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CharacterSheet {

    @Id
    @Column(name = "play_member_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "play_member_id")
    private PlayMember playMember;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne
    @JoinColumn(name = "belief_id", nullable = false)
    private Belief belief;

    @ManyToOne
    @JoinColumn(name = "race_id", nullable = false)
    private Race race;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private String appearance;

    @Column(nullable = false)
    private String background;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer currentWeight;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer currentHp;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer currentMoney;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer limitWeight;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer limitHp;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer glove;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer inspirationScore;

    @Column(nullable = false)
    @ColumnDefault("1")
    private Integer level;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer exp;

    @Column(nullable = false, name="image_url")
    private String imageURL;
}
