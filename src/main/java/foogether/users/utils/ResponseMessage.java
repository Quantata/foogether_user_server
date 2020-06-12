package foogether.users.utils;

public class ResponseMessage {
    /**
     * default
     */
    public static final String AUTHORIZED = "인증 성공";
    public static final String UNAUTHORIZED = "인증 실패";
    public static final String FORBIDDEN = "인가 실패";

    public static final String INTERNAL_SERVER_ERROR = "서버 내부 에러";
    public static final String SERVICE_UNAVAILABLE = "현재 서비스를 사용하실 수 없습니다. 잠시후 다시 시도해 주세요.";

    public static final String DB_ERROR = "데이터베이스 에러";

    /* Users */
    public static final String SAVE_USER = "회원 가입 성공";
    public static final String UPDATE_USER = "회원정보 수정 성공";
    public static final String UPDATE_USER_FAIL = "회원정보 수정 실패";


    public static final String DELETE_USER = "회원 탈퇴 성공";
    public static final String FIND_USER_INFO = "회원 정보 조회 성공";
    public static final String FIND_USERLIST_INFO = "회원들의 정보 조회 성공";

    public static final String ALREADY_USER = "이미 등록된 전화번호 입니다.";
    public static final String BAD_PARAMETER = "파라미터 값이 잘못되었습니다.";
    public static final String NOT_FOUND_USER = "유저 정보를 찾을 수 없습니다.";

    public static final String LOGIN_SUCCESS = "로그인 성공";
    public static final String LOGIN_FAIL = "아이디와 비밀번호를 다시 확인해주십시오.";


    /**
     * Meetings
     */
    public static final String NOT_FOUND_LIST = "조회 정보를 찾을 수 없습니다.";
    public static final String READ_ALL_BUT_ZERO = "작성된 게시글이 없습니다.";
    public static final String WRONG_STATUS = "카테고리 설정이 잘못되었습니다.";
    public static final String READ_ALL_CONTENTS = "전체 게시판 조회 성공";
    public static final String READ_CONTENT = "상세 게시판 조회 성공";

    public static final String CREATE_CONTENT = "게시글 작성 성공";
    public static final String FAIL_CREATE_CONTENT = "게시글 작성 실패";
    public static final String UPDATE_CONTENT = "게시글 수정 성공";
    public static final String FAIL_UPDATE_CONTENT = "게시글 수정 실패";
    public static final String DELETE_CONTENT = "게시글 삭제 성공";
    public static final String LIKE_CONTENT = "게시글 좋아요 등록 성공";
    public static final String UNLIKE_CONTENT = "게시글 좋아요 취소 성공";

    public static final String JOIN_MEETING = "참여하기 성공";
    public static final String OUT_MEETING = "참여취소 성공";
    public static final String MEETING_COMPLETE = "모집 완료 성공";



}