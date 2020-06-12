package foogether.users.service;

import foogether.users.web.dto.DefaultResponse;
import foogether.users.web.dto.LoginDto;

public interface LoginService {
    /* 로그인 */
    DefaultResponse<JwtService.TokenRes> login(final LoginDto loginDto);
}
