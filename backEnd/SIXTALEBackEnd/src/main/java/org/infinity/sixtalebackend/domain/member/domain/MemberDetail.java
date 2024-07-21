package org.infinity.sixtalebackend.domain.member.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "member_detail")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDetail {

    @Id
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String favorRule;

    @Column(name = "rp_type",nullable = false)
    private Integer rpType = 0;

    @Column(name = "chat_type", nullable = false)
    private Integer chatType = 0;

    @Column(name = "talk_type", nullable = false)
    private Integer talkType = 0;

    @Column(name = "taste_type", nullable = false)
    private Integer tasteType = 0;

    @Column(name = "system_type", nullable = false)
    private Integer systemType = 0;

    @Column(name = "time_type", nullable = false)
    private Integer timeType = 0;

    // 비트 연산을 위한 메서드들
    public void setBitForField(String fieldName, int position, boolean value) {
        Integer bitField = getBitField(fieldName);
        if (position < 0 || position >= getBitLength(fieldName)) {
            throw new IllegalArgumentException("Position out of range for " + fieldName);
        }
        if (value) {
            bitField |= (1 << position); // 비트를 1로 설정
        } else {
            bitField &= ~(1 << position); // 비트를 0으로 설정
        }
        setBitField(fieldName, bitField);
    }

    public boolean getBitForField(String fieldName, int position) {
        Integer bitField = getBitField(fieldName);
        if (position < 0 || position >= getBitLength(fieldName)) {
            throw new IllegalArgumentException("Position out of range for " + fieldName);
        }
        return (bitField & (1 << position)) != 0;
    }

    private Integer getBitField(String fieldName) {
        switch (fieldName) {
            case "rpType": return rpType;
            case "chatType": return chatType;
            case "talkType": return talkType;
            case "tasteType": return tasteType;
            case "systemType": return systemType;
            case "timeType": return timeType;
            default: throw new IllegalArgumentException("Unknown field: " + fieldName);
        }
    }

    private void setBitField(String fieldName, Integer value) {
        switch (fieldName) {
            case "rpType": rpType = value; break;
            case "chatType": chatType = value; break;
            case "talkType": talkType = value; break;
            case "tasteType": tasteType = value; break;
            case "systemType": systemType = value; break;
            case "timeType": timeType = value; break;
            default: throw new IllegalArgumentException("Unknown field: " + fieldName);
        }
    }

    private int getBitLength(String fieldName) {
        switch (fieldName) {
            case "rpType": return 10;
            case "chatType": return 5;
            case "talkType": return 6;
            case "tasteType": return 7;
            case "systemType": return 4;
            case "timeType": return 6;
            default: throw new IllegalArgumentException("Unknown field: " + fieldName);
        }
    }
}
