package org.infinity.sixtalebackend.domain.rule.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JobBelief {
    @EmbeddedId
    private JobBeliefID id;

    @ManyToOne
    @MapsId("jobID")
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne
    @MapsId("beliefID")
    @JoinColumn(name = "belief_id", nullable = false)
    private Belief belief;

    private String description;
}
