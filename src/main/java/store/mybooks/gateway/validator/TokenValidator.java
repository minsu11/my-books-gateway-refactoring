package store.mybooks.gateway.validator;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import store.mybooks.gateway.config.JwtConfig;
import store.mybooks.gateway.exception.ForbiddenAccessException;
import store.mybooks.gateway.exception.StatusIsDormancyException;
import store.mybooks.gateway.exception.StatusIsLockException;

/**
 * packageName    : store.mybooks.gateway.validator<br>
 * fileName       : TokenValidator<br>
 * author         : masiljangajji<br>
 * date           : 3/2/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/2/24        masiljangajji       최초 생성
 */
@Component
public class TokenValidator {

    private static JWTVerifier jwtVerifier = null;

    private static final String STATUS_DORMANCY = "휴면";
    private static final String STATUS_LOCK = "잠금";

    private static final String STATUS_ACTIVE = "활성";

    @Autowired
    private TokenValidator(JwtConfig jwtConfig) {
        Algorithm algorithm = Algorithm.HMAC512(jwtConfig.getSecret());
        jwtVerifier = JWT.require(algorithm).build();
    }

    public static void isValidStatus(String status) {

        if (status.equals(STATUS_DORMANCY)) {
            throw new StatusIsDormancyException();
        } else if (status.equals(STATUS_LOCK)) {
            throw new StatusIsLockException();
        } else if (!status.equals(STATUS_ACTIVE)) {
            throw new JWTVerificationException("토큰 조작");
        }

    }

    public static void isValidAuthority(String userAuth, String... authority) {

        // 권한 확인
        if (Arrays.stream(authority).noneMatch(auth -> auth.equals(userAuth))) {
            throw new ForbiddenAccessException();
        }
    }

    public static DecodedJWT isValidToken(String token) throws JWTVerificationException {
        // 토큰이 유효한지 , 만료되지 않았는지
        return jwtVerifier.verify(token);
    }

}
