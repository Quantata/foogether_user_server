package foogether.users.service;

import foogether.users.domain.Entity.User;
import foogether.users.domain.UserStatus;
import foogether.users.repository.UserRepository;
import foogether.users.utils.ResponseMessage;
import foogether.users.web.dto.DefaultResponse;
import foogether.users.web.dto.UserRequestDto;
import foogether.users.web.dto.UserResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtService jwtService;

    /* 회원 정보 조회  - 관리자 */
    @Override
    public DefaultResponse<List<UserResponseDto>> findAll(String header) {
        int adminIdx = jwtService.decode(header).getUserIdx();
        User isAdmin = userRepository.findByIdx(adminIdx);

        if(isAdmin.getUserStatus().equals(UserStatus.ADMIN)) {
            List<User> userList = userRepository.findAll();
            List<UserResponseDto> userResponseDtoList = new ArrayList<>();

            for(User user : userList) {
//                if(user.getUserStatus() != UserStatus.ADMIN) {}
                userResponseDtoList.add(new UserResponseDto(user));

            }

            if(userResponseDtoList.size() == 0 ){
                // 조회된 유저 정보가 없습니다.
                return DefaultResponse.res("fail", ResponseMessage.NOT_FOUND_USER);
            }
            else {
                // UserList 출력
                return DefaultResponse.res("success", ResponseMessage.FIND_USERLIST_INFO,
                        userResponseDtoList);
            }
        }

        // 권한이 없습니다.
        return DefaultResponse.res("fail", ResponseMessage.UNAUTHORIZED);
    }

    /* 회원 권한 조정 */
    @Override
    public DefaultResponse updateUserState(String header, int userIdx, UserStatus userStatus) {
        int adminIdx = jwtService.decode(header).getUserIdx();
        User isAdmin = userRepository.findByIdx(adminIdx);

        if(isAdmin.getUserStatus().equals(UserStatus.ADMIN)){
            User user = userRepository.findByIdx(userIdx);

            if(user == null){
                return DefaultResponse.res("fail", ResponseMessage.NOT_FOUND_USER);
            }
            UserRequestDto userRequestDto = new UserRequestDto(user);
            userRequestDto.setUserStatus(userStatus);

            userRepository.save(userRequestDto.toEntity());

            return DefaultResponse.res("success", ResponseMessage.UPDATE_USER_STATUS);
        } else {
            return DefaultResponse.res("fail", ResponseMessage.UNAUTHORIZED);
        }
    }

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
    public DefaultResponse<UserResponseDto> updateUser(UserRequestDto userRequestDto) {
        if (userRequestDto.getIdx() != 0) {    // 수정
            userRepository.save(userRequestDto.toEntity());
            User user = userRepository.findByIdx(userRequestDto.getIdx());
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
    public DefaultResponse save(UserRequestDto userRequestDto) {
        if (userRequestDto.getIdx() != 0 && userRequestDto.getUserStatus() == UserStatus.BLOCKED) {   // 탈퇴
            userRepository.save(userRequestDto.toEntity());
            return DefaultResponse.res(
                    "success", ResponseMessage.DELETE_USER
            );
        }
        else {  // 가입
            // 이메일 중복 검사
            if(userRepository.findByPhoneNum(userRequestDto.getPhoneNum()) != null){
                return DefaultResponse.res("fail", ResponseMessage.ALREADY_USER);
            }
            userRepository.save(userRequestDto.toEntity());
            return DefaultResponse.res(
                    "success", ResponseMessage.SAVE_USER
            );
        }

    }
}
