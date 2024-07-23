package org.infinity.sixtalebackend.domain.memberdetail.domain;

import jakarta.persistence.*;
import lombok.*;
import org.infinity.sixtalebackend.domain.member.domain.Member;

@Entity
@Getter
@Builder
@Table(name = "member_detail")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDetail {

    @Id
    @Column(name = "member_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "member_id")
    private Member member;

    private String favorRule;

    @Column(name = "rp_type",nullable = false)
    private Integer rpType ;

    @Column(name = "chat_type", nullable = false)
    private Integer chatType ;

    @Column(name = "talk_type", nullable = false)
    private Integer talkType ;

    @Column(name = "taste_type", nullable = false)
    private Integer tasteType ;

    @Column(name = "system_type", nullable = false)
    private Integer systemType ;

    @Column(name = "time_type", nullable = false)
    private Integer timeType ;

}
