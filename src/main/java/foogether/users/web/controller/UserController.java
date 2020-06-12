package foogether.users.web.controller;

import foogether.users.domain.UserStatus;
import foogether.users.service.JwtService;
import foogether.users.service.S3FileUploadService;
import foogether.users.service.UserService;
import foogether.users.utils.PasswordEncoder;
import foogether.users.utils.ResponseMessage;
import foogether.users.utils.auth.Auth;
import foogether.users.web.dto.DefaultResponse;
import foogether.users.web.dto.UserRequestDto;
import foogether.users.web.dto.UserResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import java.util.List;

import static foogether.users.web.dto.DefaultResponse.*;

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
    @Autowired
    private JwtService jwtService;

    /* 관리자 */
    /* 회원 권한 조정 */
    @PostMapping("/{userIdx}/userStatus/{userStatus}")
    public ResponseEntity updateUserState(
            @RequestHeader(value = "Authorization", required = false) final String header,
            @PathVariable("userIdx") int userIdx,
            @PathVariable("userStatus") String postUserStatus
    ) {
        if (jwtService.decode(header).getUserIdx() != -1) {

            DefaultResponse defaultResponse;
            try {
                //userStatus EnumType으로 바뀜
                UserStatus userStatus = UserStatus.valueOf(postUserStatus);

                defaultResponse = userService.updateUserState(
                        header, userIdx, userStatus);
                return new ResponseEntity<>(defaultResponse, HttpStatus.OK);
//            return null;
            } catch (Exception e) {
                log.error(e.getMessage());
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(FAIL_AUTHORIZATION_RES, HttpStatus.UNAUTHORIZED);
        }
    }

    /* 관리자 전체 회원 조회 */
    @GetMapping("/list")
    public ResponseEntity getUserListInfo(
            @RequestHeader(value = "Authorization") final String header ) {
        DefaultResponse<List<UserResponseDto>> defaultResponse;
        try{
            defaultResponse = userService.findAll(header);
            return new ResponseEntity<>(defaultResponse, HttpStatus.OK);
        } catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* 회원 정보 */
    /* 회원 리스트 조회 */
    @PostMapping("/list")
    public ResponseEntity getUserListInfo(
            @RequestBody List<Integer> userIdxList) {
        DefaultResponse<List<UserResponseDto>> defaultResponse;
        try{
            defaultResponse = userService.findAllUserByIdx(userIdxList);
            return new ResponseEntity<>(defaultResponse, HttpStatus.OK);
        } catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* 회원 한명 정보 조회 - 진행중 */
    @GetMapping("/{userIdx}")
    public ResponseEntity getUserInfo(
//            @RequestHeader(value = "Authorization", required = false) final String header,
            @PathVariable("userIdx") int userIdx
        ) {
        DefaultResponse<UserResponseDto> defaultResponse;
//        if(jwtService.decode(header).getUserIdx() == userIdx) {

            try {
//                int userIdx = jwtService.decode(header).getUserIdx();
//                userIdx = jwtService.decode(header).getUserIdx();
                defaultResponse = userService.findUserByIdx(userIdx);

                ResponseEntity responseEntity =
                        new ResponseEntity<>(defaultResponse, HttpStatus.OK);
                log.info("body >>>> " + responseEntity.getBody());
//                return new ResponseEntity<>(defaultResponse, HttpStatus.OK);
                return responseEntity;
            } catch (Exception e) {
                log.error(e.getMessage());
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
//        else {
//            return new ResponseEntity(FAIL_AUTHORIZATION_RES, HttpStatus.UNAUTHORIZED);
//        }
//    }

    /* 회원 탈퇴 */
    @Auth
    @DeleteMapping("")
    public ResponseEntity deleteUser(
            @RequestHeader(value = "Authorization", required = false) final String header,
            @Valid UserRequestDto userRequestDto,
            BindingResult bindingResult,
            @RequestPart(value = "file", required = true)
                    MultipartFile img
    ){

        if(jwtService.checkAuth(header, userRequestDto.getIdx()))
        {
            DefaultResponse<UserRequestDto> defaultResponse;
            try {
                if(bindingResult.hasErrors()){
                    defaultResponse = DefaultResponse.res("fail",
                            bindingResult.getAllErrors()
                                    .get(0).getDefaultMessage());
                    return new ResponseEntity<>(defaultResponse, HttpStatus.INTERNAL_SERVER_ERROR);
                }

                userRequestDto.setUserStatus(UserStatus.DELETE);
                return new ResponseEntity<>(userService.save(userRequestDto), HttpStatus.OK);

            } catch (Exception e) {
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity(FAIL_AUTHORIZATION_RES, HttpStatus.UNAUTHORIZED);
        }


    }

    /* 회원 수정 */
    @Auth
    @PutMapping("")
    public ResponseEntity updateUser(
            @RequestHeader(value = "Authorization", required = false) final String header,
            @Valid UserRequestDto userRequestDto,
            BindingResult bindingResult,
            @RequestPart(value = "file", required = true)
                    MultipartFile img
    ){

        if(jwtService.checkAuth(header, userRequestDto.getIdx()))
        {
            DefaultResponse<UserResponseDto> defaultResponse;
            try {
                if(bindingResult.hasErrors()){
                    defaultResponse = DefaultResponse.res("fail",
                            bindingResult.getAllErrors()
                                    .get(0).getDefaultMessage());
                    return new ResponseEntity<>(defaultResponse, HttpStatus.INTERNAL_SERVER_ERROR);
                }
                String imgUrl = s3FileUploadService.upload(img);
                userRequestDto.setProfileImg(imgUrl);
                // 값은 그대로 넣어줘야함(그래야 수정 안일어남)
                userRequestDto.setPassword(PasswordEncoder.encodePwd(userRequestDto.getPassword()));
                return new ResponseEntity<>(userService.updateUser(userRequestDto), HttpStatus.OK);

            } catch (Exception e) {
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity(FAIL_AUTHORIZATION_RES, HttpStatus.UNAUTHORIZED);
        }


    }

    /* 회원 가입 */
    @PostMapping("")
    public ResponseEntity signUp(
            @Valid UserRequestDto userRequestDto,
            BindingResult bindingResult,
            @RequestPart(value = "file", required = true)
            MultipartFile img
            ){

        DefaultResponse<UserRequestDto> defaultResponse;
        try {
            if(userRequestDto.getIdx() != 0){
                defaultResponse = DefaultResponse.res("fail", ResponseMessage.BAD_PARAMETER);
                return new ResponseEntity<>(defaultResponse, HttpStatus.BAD_REQUEST);
            }
            if(bindingResult.hasErrors()){
                defaultResponse = DefaultResponse.res("fail",
                        bindingResult.getAllErrors()
                        .get(0).getDefaultMessage());
                return new ResponseEntity<>(defaultResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            String imgUrl = s3FileUploadService.upload(img);
            userRequestDto.setProfileImg(imgUrl);
            userRequestDto.setPassword(PasswordEncoder.encodePwd(userRequestDto.getPassword()));
            return new ResponseEntity<>(userService.save(userRequestDto), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
