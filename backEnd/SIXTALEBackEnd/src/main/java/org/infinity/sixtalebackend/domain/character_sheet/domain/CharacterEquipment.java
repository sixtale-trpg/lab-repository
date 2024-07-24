package org.infinity.sixtalebackend.domain.character_sheet.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.infinity.sixtalebackend.domain.equipment.domain.Equipment;
import org.infinity.sixtalebackend.domain.room.domain.PlayMember;

@Entity
@Getter
@Setter
@Builder
@Table(name = "character_equipment")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CharacterEquipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    @ManyToOne
    @JoinColumn(name = "play_member_id", nullable = false)
    private PlayMember playMember;

    private Integer currentCount;

    @Column(nullable = false)
    @ColumnDefault("0")
    private boolean statWeight;

}
