package foogether.users.web.dto;

import com.sun.istack.Nullable;
import foogether.users.domain.Entity.User;
import foogether.users.domain.Gender;
import foogether.users.domain.UserStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlEnumValue;

@Data
@NoArgsConstructor
@Validated
public class UserRequestDto {
    // user 고유번호
    int idx;

    // 이메일
    @Email(message = "이메일 형식에 맞지 않습니다.")
    String emailAddr;

    // 유저 상태
    UserStatus userStatus;

    // 이름
    @NotBlank(message = "이름은 필수 입력값 입니다.")
    @Length(min = 2, max = 10, message = "이름은 길이가 2에서 10 사이어야 합니다.")
    String name;

    // 전화 번호
    @NotBlank(message = "전화번호는 필수 입력값 입니다.")
    @Pattern(regexp = "[0-9]+")
    @Length(min = 8, max = 14, message = "전화번호 길이가 8에서 14 사이어야 합니다.")
    String phoneNum;

    // 닉네임
    @NotBlank(message = "닉네임은 필수 입력값 입니다.")
    @Length(min = 2, max = 10, message = "닉네임은 길이가 2에서 10 사이어야 합니다.")
    String nickname;

    // 프로필 이미지
    // default = 클라이언트에서
    String profileImg;

    // 성별
//    @NotBlank(message = "성별입력은 필수 입니다.")
    Gender gender;

    // 비밀번호
    @NotBlank(message = "비밀번호는 필수 입력값 입니다.")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
            message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    String password;

    // entity -> Dto
    public UserRequestDto(User entity){
        this.idx = entity.getIdx();
        this.emailAddr = entity.getEmailAddr();
        this.gender = entity.getGender();
        this.name = entity.getName();
        this.nickname = entity.getNickname();
        this.phoneNum = entity.getPhoneNum();
        this.profileImg = entity.getProfileImg();
        this.password = entity.getPassword();
        this.userStatus = entity.getUserStatus();
    }

    // dto ->
    public User toEntity() {
        return User.builder()
                .idx(this.idx)
                .name(this.name)
                .emailAddr(this.emailAddr)
                .gender(this.gender)
                .nickname(this.nickname)
                .phoneNum(this.phoneNum)
                .profileImg(this.profileImg)
                .password(this.password)
                .userStatus(this.userStatus)
                .build();
    }

}
