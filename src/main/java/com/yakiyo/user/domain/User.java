package com.yakiyo.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String Id;

    private String fcmToken;

    private String name;

    private String nickname; //기본으로 구글 이름을 nickname으로 사용

    @Column(unique = true)
    private String email;



}
