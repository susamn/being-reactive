package com.susamn.beingreactive.java.projectreactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

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

    @Test
    public void testChainedFailure(){
        Flux<Integer> fluxFromJust = Flux.range(1, 5)
                .window(2)
                .flatMap( x-> x.map(this::doSomething))
                .onErrorContinue((e,i)->{
                    System.out.println("Error For Item +" + i );
                })
                .subscribeOn(Schedulers.parallel())
                .flatMap(Flux::fromIterable)
                .log();

        StepVerifier
                .create(fluxFromJust)
                .expectNext(1, 2,3,4,5)
                .verifyComplete();
        sleep(5);

    }

    private void sleep(int amount) {
        try {
            Thread.sleep(amount * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> doSomething(Integer i){
        System.out.println("Input : "+i);
        return new Data(i, i+1).apply((x,y)->x+y).apply((x,y)->x/0).getData();
    }

    static class Data{
        int i,j;

        public List<Integer> getData() {
            return data;
        }

        List<Integer> data = new ArrayList<>();

        public Data(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public Data apply(BiFunction<Integer, Integer, Integer> b){
            data.add(b.apply(i,j));
            return this;
        }
    }

}
