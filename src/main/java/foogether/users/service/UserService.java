package foogether.users.service;

import foogether.users.domain.UserStatus;
import foogether.users.web.dto.DefaultResponse;
import foogether.users.web.dto.UserRequestDto;
import foogether.users.web.dto.UserResponseDto;

import java.util.List;

public interface UserService {
    /* 전체 회원 정보 조회 - 관리자 */
    DefaultResponse<List<UserResponseDto>> findAll(String header);

    /* 회원 권한 조정 */
    DefaultResponse updateUserState(String header, int userIdx, UserStatus userStatus);

    /* 회원 정보 리스트 조회 */
    DefaultResponse<List<UserResponseDto>> findAllUserByIdx(List<Integer> userIdxList);

    /* 내 정보 조회 */
    DefaultResponse<UserResponseDto> findUserByIdx(int userIdx);

    /* 회원 가입 및 탈퇴*/
    DefaultResponse save(UserRequestDto userRequestDto);

    /* 회원 수정*/
    DefaultResponse<UserResponseDto> updateUser(UserRequestDto userRequestDto);
}
