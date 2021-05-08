package com.susamn.beingreactive.java.projectreactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    @Test
    public void testSimpleFlux(){
        Flux<String> stream = Flux.just("data","from","some","datasource");
        StepVerifier.create(stream)
                .expectSubscription() // Makes sure that there is a valid subscription, otherwise the flux will not send any data
                .expectNext("data")
                .expectNext("from")
                .expectNext("some")
                .expectNext("datasource")
                .verifyComplete();
    }

    @Test
    public void testSimpleFluxFromIterable(){
        List<String> d = new ArrayList<>(Collections.emptyList());
        d.add("data1");
        d.add("data2");
        Flux<String> stream = Flux.fromIterable(d);
        StepVerifier.create(stream)
                .expectSubscription() // Makes sure that there is a valid subscription, otherwise the flux will not send any data
                .expectNext("data1")
                .expectNext("data2")
                .verifyComplete();
    }
}
