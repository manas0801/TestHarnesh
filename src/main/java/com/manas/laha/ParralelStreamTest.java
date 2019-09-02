package com.manas.laha;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 3)
@Measurement(iterations = 8)
public class ParralelStreamTest {

    @Param({"10000000"})
    private int N;


    public static void main (String[] args) throws RunnerException {

        Options opt = new OptionsBuilder()
                .include(ParralelStreamTest.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
    @Benchmark
    public void primeCollectorUse(Blackhole bh) {
    IntStream.range(2,N).boxed().collect(new PrimeCollector()).get(true).stream().forEach(s->bh.consume(s));
    }

    @Benchmark
    public void primeCollector(Blackhole bh) {
        IntStream.range(2,N).boxed().collect(Collectors.partitioningBy(i-> isPrime(i))).get(true).stream().forEach(s->bh.consume(s));

    }

    public  static Boolean isPrime( Integer Candidate){

        int CandidateSquroot =  (int) Math.sqrt( (double)Candidate);
        return
                IntStream.rangeClosed(2,CandidateSquroot)
                        .noneMatch(i-> Candidate % i == 0);
    }

}
