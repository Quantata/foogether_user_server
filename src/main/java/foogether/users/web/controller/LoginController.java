package foogether.users.web.controller;

import foogether.users.service.AuthService;
import foogether.users.utils.PasswordIncoder;
import foogether.users.utils.auth.Auth;
import foogether.users.web.dto.DefaultResponse;
import foogether.users.web.dto.LoginDto;
import foogether.users.web.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import static foogether.users.web.dto.DefaultResponse.FAIL_AUTHORIZATION_RES;
import static foogether.users.web.dto.DefaultResponse.FAIL_DEFAULT_RES;

@Slf4j
@RestController
public class LoginController {
    @Autowired
    AuthService authService;

    // 로그인 구현
    @PostMapping("/login")
    public ResponseEntity login(
            @Valid LoginDto loginDto,
            BindingResult bindingResult
    ) {

        DefaultResponse<UserDto> defaultResponse;

        try {
            if (bindingResult.hasErrors()) {
                defaultResponse = DefaultResponse.res("fail",
                        bindingResult.getAllErrors()
                                .get(0).getDefaultMessage());
                return new ResponseEntity<>(defaultResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            String hashpw = PasswordIncoder.incodePwd(loginDto.getPassword());
            loginDto.setPassword(hashpw);
            return new ResponseEntity<>(authService.login(loginDto), HttpStatus.OK);

        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
}
