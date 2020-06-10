package foogether.users.web.controller;

import foogether.users.service.S3FileUploadService;
import foogether.users.service.UserService;
import foogether.users.utils.PasswordEncoder;
import foogether.users.utils.auth.Auth;
import foogether.users.web.dto.DefaultResponse;
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
@RequestMapping("/users")
public class UserController {
    /* template */
//    @PostMapping("/signup")
//    public ResponseEntity signUp(
//            @RequestHeader(value = "Authorization") final String header,
//            @RequestBody UserDto userDto
//    ){
//        DefaultResponse<UserDto> defaultResponse;
//
//        try {
//
//        } catch (Exception e) {
//            defaultResponse = DefaultResponse.res("fail", ResponseMessage.INTERNAL_SERVER_ERROR);
//            return new ResponseEntity<>(defaultResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }
    @Autowired
    UserService userService;
    @Autowired
    private S3FileUploadService s3FileUploadService;
//    @Autowired
//    private JwtService jwtService;

    /* 회원 정보 조회 */
    /* 회원 권한 조정 */

    /* 회원 탈퇴 */

    /* 회원 수정 */
    @Auth
    @PostMapping("/{userIdx}")
    public ResponseEntity updateUser(
            @RequestHeader(value = "Authorization", required = false) final String header,
            @Valid UserDto userDto,
            BindingResult bindingResult,
            @RequestPart(value = "file", required = true)
                    MultipartFile img
    ){

        DefaultResponse<UserDto> defaultResponse;
//        if(jwtService.checkAuth(header, userDto.getIdx()))
//        {
            try {
            if(bindingResult.hasErrors()){
                defaultResponse = DefaultResponse.res("fail",
                        bindingResult.getAllErrors()
                                .get(0).getDefaultMessage());
                return new ResponseEntity<>(defaultResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            String imgUrl = s3FileUploadService.upload(img);
            userDto.setProfileImg(imgUrl);
            userDto.setPassword(PasswordEncoder.incodePwd(userDto.getPassword()));
            return new ResponseEntity<>(userService.saveUser(userDto), HttpStatus.OK);

            } catch (Exception e) {
                log.error(e.getMessage());
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
//        } else {
//            return new ResponseEntity(FAIL_AUTHORIZATION_RES, HttpStatus.UNAUTHORIZED);
//        }

    }

    /* 회원 가입 */
    @PostMapping("/signup")
    public ResponseEntity signUp(
            @Valid UserDto userDto,
            BindingResult bindingResult,
            @RequestPart(value = "file", required = true)
            MultipartFile img
            ){

        DefaultResponse<UserDto> defaultResponse;
        try {
            if(bindingResult.hasErrors()){
                defaultResponse = DefaultResponse.res("fail",
                        bindingResult.getAllErrors()
                        .get(0).getDefaultMessage());
                return new ResponseEntity<>(defaultResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            String imgUrl = s3FileUploadService.upload(img);
            userDto.setProfileImg(imgUrl);
            userDto.setPassword(PasswordEncoder.incodePwd(userDto.getPassword()));
            return new ResponseEntity<>(userService.saveUser(userDto), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
