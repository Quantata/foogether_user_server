package foogether.users.web.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@Validated
public class LoginDto {
    @NotBlank(message = "전화번호를 입력하세요.")
    String phoneNum;
    @NotBlank(message = "비밀번호를 입력하세요.")
    String password;
}
