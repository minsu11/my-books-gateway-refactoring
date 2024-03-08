package store.mybooks.gateway.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import store.mybooks.gateway.error.ErrorMessage;
import store.mybooks.gateway.exception.ForbiddenAccessException;
import store.mybooks.gateway.exception.InvalidStatusException;
import store.mybooks.gateway.handler.ErrorResponseHandler;
import store.mybooks.gateway.utils.HttpUtils;
import store.mybooks.gateway.validator.TokenValidator;

/**
 * packageName    : store.mybooks.gateway.filter<br>
 * fileName       : AdminAuthFilter<br>
 * author         : masiljangajji<br>
 * date           : 3/2/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/2/24        masiljangajji       최초 생성
 */
public class AdminAuthFilter extends AbstractGatewayFilterFactory<AdminAuthFilter.Config> {

    public AdminAuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // 헤더에서 값을 읽어옴

            String token = HttpUtils.getAuthorizationHeaderValue(exchange);
            String originalPath = HttpUtils.getPath(exchange);

            DecodedJWT jwt;

            try {
                jwt = TokenValidator.isValidToken(token);
                TokenValidator.isValidAuthority(jwt, Config.STATUS_ACTIVE, Config.ROLE_ADMIN);

            } catch (InvalidStatusException e) {
                return ErrorResponseHandler.handleInvalidToken(exchange, HttpStatus.FORBIDDEN,
                        ErrorMessage.INACTIVE_USER.getMessage()); //  토큰은 유효한데 활성 상태 아님
            } catch (ForbiddenAccessException e) {
                return ErrorResponseHandler.handleInvalidToken(exchange, HttpStatus.FORBIDDEN,
                        ErrorMessage.INVALID_ACCESS.getMessage()); //  토큰은 유효한데 권한 없음 403
            } catch (TokenExpiredException e) {
                return ErrorResponseHandler.handleInvalidToken(exchange, HttpStatus.UNAUTHORIZED,
                        ErrorMessage.TOKEN_EXPIRED.getMessage()); // 토큰 만료됐음 인증 필요 401
            } catch (JWTVerificationException e) {
                return ErrorResponseHandler.handleInvalidToken(exchange, HttpStatus.UNAUTHORIZED,
                        ErrorMessage.INVALID_TOKEN.getMessage()); // 토큰이 조작됐음 올바르지 않은 요청 401
            }

            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .path(originalPath.replace("/api/admin/", "/api/")) // 새로운 URL 경로 설정
                    .build();

            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(modifiedRequest)
                    .build();

            return chain.filter(modifiedExchange);
        };


    }


    public static class Config { // // 필요한 전달할 설정
        private static final String ROLE_ADMIN = "ROLE_ADMIN";
        private static final String STATUS_ACTIVE = "활성";
    }
}
