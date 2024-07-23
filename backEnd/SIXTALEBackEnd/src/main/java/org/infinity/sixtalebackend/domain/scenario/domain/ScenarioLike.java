package org.infinity.sixtalebackend.domain.scenario.domain;

import jakarta.persistence.*;
import lombok.*;
import org.infinity.sixtalebackend.domain.member.domain.Member;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScenarioLike {

    @EmbeddedId
    private ScenarioLikeID id;

    @ManyToOne
    @MapsId("scenarioID")
    @JoinColumn(name = "scenario_id")
    private Scenario scenario;

    @ManyToOne
    @MapsId("memberID")
    @JoinColumn(name = "member_id")
    private Member member;

}
