package org.infinity.sixtalebackend.domain.member.domain;

import jakarta.persistence.*;
import lombok.*;
import org.infinity.sixtalebackend.domain.model.BaseTimeEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@Table(name = "member")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    // string default 255
    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 50, unique = true)
    private String nickname;

    @Column(nullable = false, length = 512)
    private String accessToken;

    @Column(nullable = false, length = 512)
    private String refreshToken;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Column(nullable = false,name = "provider_user_id")
    private String providerUserID;

    @Column(nullable = false)
    private Boolean isWithdrawn;

    @Column(nullable = false)
    private LocalDateTime lastLoginAt;

    @Column(name = "image_url")
    private String imageURL;

    public void setNickName(String nickName) {
        this.nickname = nickName;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


    public Member(String email, String nickname, String accessToken, Provider provider, String providerUserID, boolean isWithdrawn) {
        this.email = email;
        this.nickname = nickname;
        this.accessToken = accessToken;
        this.provider = provider;
        this.providerUserID = providerUserID;
        this.isWithdrawn = isWithdrawn;
    }

}
