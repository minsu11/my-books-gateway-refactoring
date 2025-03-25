package store.mybooks.gateway.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * packageName    : store.mybooks.gateway.config
 * fileName       : PropertiesConfig
 * author         : damho-lee
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24          damho-lee          최초 생성
 */

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "url.name")
public class UrlProperties {
    private String auth;


    public void setAuth(String auth) {
        this.auth = auth;
    }
}
