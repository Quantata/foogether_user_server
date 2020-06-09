package foogether.users.service;

import foogether.users.repository.UserRepository;
import foogether.users.utils.ResponseMessage;
import foogether.users.web.dto.DefaultResponse;
import foogether.users.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserRepository userRepository;

    /* 회원 정보 조회 */
    /* 로그인 */
    /* 회원 권한 조정 */
    /* 회원 수정 */
    /* 회원 탈퇴 */
    /* 회원 가입 */
    @Override
    public DefaultResponse saveUser(UserDto userDto) {
        // 이메일 중복 검사
        // Auth
        userRepository.save(userDto.toEntity());
        return DefaultResponse.res(
                "success", ResponseMessage.SAVE_USER
        );
    }
}
