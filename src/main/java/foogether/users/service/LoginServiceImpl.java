package foogether.users.service;

import foogether.users.domain.Entity.User;
import
        foogether.users.repository.UserRepository;
import foogether.users.utils.ResponseMessage;
import foogether.users.web.dto.DefaultResponse;
import foogether.users.web.dto.LoginDto;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;


    /**
     * 로그인 서비스
     * @param loginDto 로그인 객체
     * @return DefaultResponse
     */
    public DefaultResponse<JwtService.TokenRes> login(final LoginDto loginDto) {
        final User user = userRepository.findByPhoneNumAndPassword(
                loginDto.getPhoneNum(), loginDto.getPassword());
        if (user != null) {
            //토큰 생성
            final JwtService.TokenRes tokenDto = new JwtService.TokenRes(jwtService.create(user.getIdx()), user.getIdx());
            return DefaultResponse.res("success", ResponseMessage.LOGIN_SUCCESS, tokenDto);
        }
        return DefaultResponse.res("fail", ResponseMessage.LOGIN_FAIL);
    }
}
