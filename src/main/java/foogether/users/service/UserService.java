package foogether.users.service;

import foogether.users.domain.Entity.User;
import foogether.users.domain.UserStatus;
import foogether.users.web.dto.DefaultResponse;
import foogether.users.web.dto.UserDto;
import foogether.users.web.dto.UserListRequestDto;
import foogether.users.web.dto.UserResponseDto;
import lombok.Builder;

import java.util.List;

public interface UserService {
    /* 로그인 */
    /* 회원 권한 조정 */
    DefaultResponse updateUserState(String header, int userIdx, UserStatus userStatus);

    /* 회원 정보 리스트 조회 */
    DefaultResponse<List<UserResponseDto>> findAllUserByIdx(List<Integer> userIdxList);

    /* 내 정보 조회 */
    DefaultResponse<UserResponseDto> findUserByIdx(int userIdx);
    /* 회원 가입 및 탈퇴*/
    DefaultResponse save(UserDto userDto);

    /* 회원 수정*/
    DefaultResponse<UserResponseDto> updateUser(UserDto userDto);
}
