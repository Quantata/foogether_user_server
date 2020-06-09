package foogether.users.domain.Entity;

import foogether.users.domain.Gender;
import foogether.users.domain.UserStatus;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="user")
@NoArgsConstructor
@Getter
@DynamicInsert // (insert 시 null 인필드 제외)
@DynamicUpdate // (update 시 null 인필드 제외)
public class User {
    // user 고유번호
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_idx")
    int idx;

    // 생성 시간
    @Column(name = "user_createdDate")
    LocalDateTime createdDate = LocalDateTime.now();

    // 이메일
    @Column(name = "email_addr")
    String emailAddr;

    // 유저 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "user_Status")
    UserStatus userStatus;
//    // 생일
//    LocalDate birthday;

    // 이름
    @Column(name = "user_name")
    String name;

    // 전화 번호
    @Column(name = "phone_num")
    String phoneNum;

    // 닉네임
    @Column(name = "nickname")
    String nickname;

    // 프로필 이미지
    @Column(name = "profile_img")
    String profileImg;

    // 성별
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    Gender gender;

    // 비밀번호
    @Column(name = "password")
    String password;


    @PrePersist
    public void prePersist() {
        this.userStatus = this.userStatus == null ?
        UserStatus.USING: this.userStatus;
    }

    @PreUpdate
    public void preUpdate() {
        this.userStatus = this.userStatus == null ?
                UserStatus.USING: this.userStatus;
    }

    @Builder
    public User(int idx, String emailAddr, UserStatus userStatus,
                   String name, String phoneNum, String nickname, String profileImg,
                   Gender gender, String password) {
        this.idx = idx;
        this.emailAddr = emailAddr;
        this.userStatus = userStatus;
        this.name = name;
        this.phoneNum = phoneNum;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.gender = gender;
        this.password = password;
    }


}
