package foogether.users.web.controller;

import foogether.users.service.LoginServiceImpl;
import foogether.users.service.JwtService;
import foogether.users.utils.PasswordEncoder;
import foogether.users.web.dto.DefaultResponse;
import foogether.users.web.dto.LoginDto;
import foogether.users.web.dto.UserRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
//
import javax.validation.Valid;

import static foogether.users.web.dto.DefaultResponse.FAIL_DEFAULT_RES;

@Slf4j
@RestController
public class LoginController {
    @Autowired
    LoginServiceImpl loginServiceImpl;

    @Autowired
    JwtService jwtService;

    // 로그인 구현
    @PostMapping("/login")
    public ResponseEntity login(
            @Valid LoginDto loginDto,
            BindingResult bindingResult
    ) {

        DefaultResponse<UserRequestDto> defaultResponse;

        try {
            if (bindingResult.hasErrors()) {
                defaultResponse = DefaultResponse.res("fail",
                        bindingResult.getAllErrors()
                                .get(0).getDefaultMessage());
                return new ResponseEntity<>(defaultResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            String hashpw = PasswordEncoder.encodePwd(loginDto.getPassword());
            loginDto.setPassword(hashpw);
            return new ResponseEntity<>(loginServiceImpl.login(loginDto), HttpStatus.OK);
//            return null;

        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
}
