package org.infinity.sixtalebackend.domain.map.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@Table(name = "grid_cell")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GridCell {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "map_id", nullable = false)
    private Map map;

    @Column(nullable = false)
    private Integer row;

    @Column(nullable = false)
    private Integer col;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 100)
    private String description;

}
