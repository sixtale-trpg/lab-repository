package org.infinity.sixtalebackend.domain.character_sheet.domain;

import jakarta.persistence.*;
import lombok.*;
import org.infinity.sixtalebackend.domain.room.domain.PlayMember;
import org.infinity.sixtalebackend.domain.rule.domain.Stat;

@Entity
@Getter
@Setter
@Builder
@Table(name = "character_stat")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CharacterStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "play_member_id", nullable = false)
    private PlayMember playMember;

    @ManyToOne
    @JoinColumn(name = "stat_id", nullable = false)
    private Stat stat;

    @Column(nullable = false)
    private Integer statValue;

    @Column(nullable = false)
    private boolean statWeight;

}
