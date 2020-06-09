package foogether.users.service;

import foogether.users.web.dto.DefaultResponse;
import foogether.users.web.dto.UserDto;
import lombok.Builder;

public interface UserService {
    /* 회원 정보 조회 */
    /* 로그인 */
    /* 회원 권한 조정 */
    /* 회원 수정 */
    /* 회원 탈퇴 */
    /* 회원 가입*/
    DefaultResponse saveUser(UserDto userDto);
}
