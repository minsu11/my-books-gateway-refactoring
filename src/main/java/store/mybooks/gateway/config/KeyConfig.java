//package store.mybooks.gateway.config;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URI;
//import java.security.KeyManagementException;
//import java.security.KeyStore;
//import java.security.KeyStoreException;
//import java.security.NoSuchAlgorithmException;
//import java.security.UnrecoverableKeyException;
//import java.security.cert.CertificateException;
//import java.util.List;
//import java.util.Objects;
//import javax.net.ssl.SSLContext;
//import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
//import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.ssl.SSLContextBuilder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.MediaType;
//import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.util.UriComponentsBuilder;
//
///**
// * packageName    : store.mybooks.resource.config
// * fileName       : KeyConfig
// * author         : Fiat_lux
// * date           : 2/20/24
// * description    :
// * ===========================================================
// * DATE              AUTHOR             NOTE
// * -----------------------------------------------------------
// * 2/20/24        Fiat_lux       최초 생성
// */
//@Configuration
//public class KeyConfig {
//
//    private final KeyManagerProperties keyProperties;
//
//    @Autowired
//    public KeyConfig(KeyManagerProperties keyProperties) {
//        this.keyProperties = keyProperties;
//    }
//
//    public String keyStore(String keyId) {
//        try {
//            KeyStore clientStore = KeyStore.getInstance("PKCS12");
//            InputStream result = new ClassPathResource("my-books.p12").getInputStream();
//            clientStore.load(result, keyProperties.getPassword().toCharArray());
//
//            SSLContext sslContext = SSLContextBuilder.create()
//                    .setProtocol("TLS")
//                    .loadKeyMaterial(clientStore, keyProperties.getPassword().toCharArray())
//                    .loadTrustMaterial(new TrustSelfSignedStrategy())
//                    .build();
//
//            SSLConnectionSocketFactory sslConnectionSocketFactory =
//                    new SSLConnectionSocketFactory(sslContext);
//            CloseableHttpClient httpClient = HttpClients.custom()
//                    .setSSLSocketFactory(sslConnectionSocketFactory)
//                    .build();
//
//            HttpComponentsClientHttpRequestFactory requestFactory =
//                    new HttpComponentsClientHttpRequestFactory(httpClient);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
//
//            RestTemplate restTemplate = new RestTemplate(requestFactory);
//
//            URI uri = UriComponentsBuilder
//                    .fromUriString(keyProperties.getUrl())
//                    .path(keyProperties.getPath())
//                    .encode()
//                    .build()
//                    .expand(keyProperties.getAppKey(), keyId)
//                    .toUri();
//            return Objects.requireNonNull(restTemplate.exchange(uri,
//                                    HttpMethod.GET,
//                                    new HttpEntity<>(headers),
//                                    KeyResponseDto.class)
//                            .getBody())
//                    .getBody()
//                    .getSecret();
//        } catch (KeyStoreException | IOException | CertificateException
//                 | NoSuchAlgorithmException
//                 | UnrecoverableKeyException
//                 | KeyManagementException e) {
//            throw new KeyMangerException(e.getMessage());
//        }
//    }
//}