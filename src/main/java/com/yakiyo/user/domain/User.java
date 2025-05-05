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
    private String Id; //구글 아이디 받아서 사용할거임

    private String fcmToken;

    private String name;

    private String nickname;

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
