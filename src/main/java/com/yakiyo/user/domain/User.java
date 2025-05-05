package com.yakiyo.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User {
    @Id
    private String Id;

    private String fcmToken;

    private String name;

    private String nickname; //기본으로 구글 이름을 nickname으로 사용

    @Column(unique = true)
    private String email;



    //Setter
    public void updateFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
    public void updateName(String name) {
        this.name = name;
    }
    public void updateEmail(String email) {
        this.email = email;
    }


}
