package com.susamn.beingreactive.java.projectreactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * @author Supratim Samanta
 * @date 5/8/21 at 17:47
 */
public class ProjectReactorApplicationTest {

    @Test
    public void testSimpleMono(){
        Mono<String> stream = Mono.just("data");
        StepVerifier.create(stream)
                .expectNext("data")
                .verifyComplete();
    }
}
