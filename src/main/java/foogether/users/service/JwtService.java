package foogether.users.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

import static com.auth0.jwt.JWT.require;

@Slf4j
@Service
public class JwtService {

//    @Value("${JWT.ISSUER}")
    @Value("${security.oauth2.client.client-id}")
    private String ISSUER;

    //    @Value("${JWT.SECRET}")
    @Value("${security.oauth2.client.client-secret}")
    private String SECRET;

    /**
     * 토큰 생성
     *
     * @param userIdx 토큰에 담길 로그인한 사용자의 회원 고유 IDX
     * @return 토큰
     */
    public String create(final int userIdx) {
        try {
            JWTCreator.Builder b = JWT.create();
            b.withIssuer(ISSUER);
            b.withClaim("userIdx", userIdx);

            // 한달
            b.withExpiresAt(expiresAt());
            //토큰 해싱해서 반환
            return b.sign(Algorithm.HMAC256(SECRET));
        } catch (JWTCreationException | UnsupportedEncodingException JwtCreationException) {
            log.info(JwtCreationException.getMessage());
        }
        return null;
    }

    private Date expiresAt() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        //한달 24*31
        cal.add(Calendar.HOUR, 744);
        return cal.getTime();
    }
    /**
     * 토큰 해독
     *
     * @param token 토큰
     * @return 로그인한 사용자의 회원 고유 IDX
     */
    public Token decode(final String token) {
        try {
            //토큰 해독 객체 생성
            final JWTVerifier jwtVerifier = require(Algorithm.HMAC256(SECRET)).withIssuer(ISSUER).build();
            //토큰 검증
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            // 정상적인 토큰이라면 토큰 주인(사용자) 고유 ID, 아니라면 -1
//            return new Token(decodedJWT.getClaim("user_idx").asLong().intValue());
            return new Token(decodedJWT.getClaim("userIdx").asLong().intValue());
        } catch (Exception jve) {
            log.error(jve.getMessage());
        }
        return new Token();
    }

    public static class Token {
        //토큰에 담길 정보 필드
        //초기값을 -1로 설정함으로써 로그인 실패시 -1반환
        private int userIdx = -1;

        public Token() {
        }

        public Token(final int userIdx) {
            this.userIdx = userIdx;
        }

        public int getUserIdx() {
            return userIdx;
        }
    }

    //반환될 토큰Res
    @Data
    public static class TokenRes {
        //실제 토큰
        private String token;
        //userIdx 반환
        private int userIdx;

        public TokenRes() {
        }

        public TokenRes(final String token, final int userIdx) {
            this.token = token;
            this.userIdx = userIdx;
        }
    }
    /**
     * 권환 확인
     *
     * @param header  Authorization
     * @param userIdx 사용자 고유 번호
     * @return boolean
     */
    public boolean checkAuth(final String header, final int userIdx) {
        return decode(header).getUserIdx() == userIdx;
    }
}