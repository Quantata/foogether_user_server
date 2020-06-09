package foogether.users.web.dto;

import foogether.users.utils.ResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DefaultResponse<T> {
    // "success", "fail"
    private String result;
    // 메세지
    private String message;
    // data 전체 개수(보여주는거 말고)
    private int len;
    // T 타입 data
    private T data;

    public DefaultResponse(final String result, final String message) {
        this.result = result;
        this.message = message;
        this.data = null;
    }

    public DefaultResponse(final String result, final String message, final int len) {
        this.result = result;
        this.message = message;
        this.len = len;
        this.data = null;
    }

    public static <T> foogether.users.web.dto.DefaultResponse<T> res(final String result, final String message) {
        return res(result, message, null);
    }

    public static <T> foogether.users.web.dto.DefaultResponse<T> res(final String result, final String message, final T t) {
        return foogether.users.web.dto.DefaultResponse.<T>builder()
                .data(t)
                .result(result)
                .message(message)
                .build();
    }


    public static <T> foogether.users.web.dto.DefaultResponse<T> res(final String result, final int len, final String message, final T t) {
        return foogether.users.web.dto.DefaultResponse.<T>builder()
                .data(t)
                .result(result)
                .len(len)
                .message(message)
                .build();
    }
    public static final foogether.users.web.dto.DefaultResponse FAIL_DEFAULT_RES = new foogether.users.web.dto.DefaultResponse("fail", ResponseMessage.INTERNAL_SERVER_ERROR);

    public static final foogether.users.web.dto.DefaultResponse FAIL_AUTHORIZATION_RES = new foogether.users.web.dto.DefaultResponse("fail", ResponseMessage.UNAUTHORIZED);

}