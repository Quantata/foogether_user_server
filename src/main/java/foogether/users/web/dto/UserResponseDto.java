package foogether.users.web.dto;

import foogether.users.domain.Entity.User;
import foogether.users.domain.Gender;
import foogether.users.domain.UserStatus;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class UserResponseDto {
    // user 고유번호
    int idx;
    // 이메일
    String emailAddr;
    // 이름
    String name;
    // 전화 번호
    String phoneNum;
    // 닉네임
    String nickname;
    // 프로필 이미지
    String profileImg;
    // 성별
    Gender gender;

    // entity -> Dto
    public UserResponseDto(User entity){
        this.idx = entity.getIdx();
        this.emailAddr = entity.getEmailAddr();
        this.gender = entity.getGender();
        this.name = entity.getName();
        this.nickname = entity.getNickname();
        this.phoneNum = entity.getPhoneNum();
        this.profileImg = entity.getProfileImg();
    }
}
