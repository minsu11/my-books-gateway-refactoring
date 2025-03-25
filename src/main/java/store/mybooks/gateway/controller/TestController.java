package store.mybooks.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.EntityResponse;

import javax.xml.stream.events.EntityReference;

/**
 * packageName    : store.mybooks.gateway.controller
 * fileName       : TestController
 * author         : parkminsu
 * date           : 25. 3. 25.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 3. 25.        parkminsu       최초 생성
 */
@RestController
@RequestMapping("api/test")
public class TestController {
    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("test");
    }
}
