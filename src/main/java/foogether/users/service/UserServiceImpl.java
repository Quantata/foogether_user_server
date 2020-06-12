package foogether.users.service;

import foogether.users.domain.Entity.User;
import foogether.users.domain.UserStatus;
import foogether.users.repository.UserRepository;
import foogether.users.utils.ResponseMessage;
import foogether.users.web.dto.DefaultResponse;
import foogether.users.web.dto.UserDto;
import foogether.users.web.dto.UserListRequestDto;
import foogether.users.web.dto.UserResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserRepository userRepository;

    /* 회원 정보 조회 */
    /* 로그인 */
    /* 회원 권한 조정 */

    /* 회원 리스트 조회 */
    @Override
    public DefaultResponse<List<UserResponseDto>> findAllUserByIdx(List<Integer> userIdxList) {
        List<UserResponseDto> userList = new ArrayList<>();
        for(int userIdx : userIdxList){
            User user = userRepository.findByIdx(userIdx);
            if(user != null) {
                userList.add(new UserResponseDto(user));
            }
        }
        return DefaultResponse.res("success", ResponseMessage.FIND_USERLIST_INFO, userList);
    }

    /* 내 정보 조회 */
    @Override
    public DefaultResponse<UserResponseDto> findUserByIdx(int userIdx) {
        User user = userRepository.findByIdx(userIdx);
        if(user == null){
            return DefaultResponse.res("fail", ResponseMessage.NOT_FOUND_USER);
        }

        UserResponseDto userResponseDto = new UserResponseDto(user);
        return DefaultResponse.res("success", ResponseMessage.FIND_USER_INFO, userResponseDto);
    }


    /* 회원 수정 */
    @Override
    public DefaultResponse<UserResponseDto> updateUser(UserDto userDto) {
        if (userDto.getIdx() != 0) {    // 수정
            userRepository.save(userDto.toEntity());
            User user = userRepository.findByIdx(userDto.getIdx());
            UserResponseDto userResponseDto = new UserResponseDto(user);

            return DefaultResponse.res(
                    "success", ResponseMessage.UPDATE_USER, userResponseDto
            );
        } else {
            return DefaultResponse.res(
                    "fail", ResponseMessage.UPDATE_USER_FAIL
            );
        }

    }

    /* 회원 가입 및 탈퇴 */
    @Override
    public DefaultResponse save(UserDto userDto) {
        if (userDto.getIdx() != 0 && userDto.getUserStatus() == UserStatus.BLOCKED) {   // 탈퇴
            userRepository.save(userDto.toEntity());
            return DefaultResponse.res(
                    "success", ResponseMessage.DELETE_USER
            );
        }
        else {  // 가입
            // 이메일 중복 검사
            if(userRepository.findByPhoneNum(userDto.getPhoneNum()) != null){
                return DefaultResponse.res("fail", ResponseMessage.ALREADY_USER);
            }
            userRepository.save(userDto.toEntity());
            return DefaultResponse.res(
                    "success", ResponseMessage.SAVE_USER
            );
        }

    }
}
