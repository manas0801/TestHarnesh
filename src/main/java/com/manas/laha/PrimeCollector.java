package com.manas.laha;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class PrimeCollector implements Collector<Integer, Map<Boolean,List<Integer>>,Map<Boolean,List<Integer>>> {

    @Override
    public Supplier<Map<Boolean, List<Integer>>> supplier() {
        Map<Boolean,List<Integer>> map = new HashMap<>();
        map.put(true, new ArrayList<Integer>());
        map.put(false, new ArrayList<Integer>());
        return ()-> map;
    }

    @Override
    public BiConsumer<Map<Boolean, List<Integer>>, Integer> accumulator() {
        return (m,a)->  m.get(isPrime(m.get(true),a)).add(a);
    }

    @Override
    public BinaryOperator<Map<Boolean, List<Integer>>> combiner() {
        return (m1, m2) -> {
            m1.get(true).addAll(m2.get(true));
            m1.get(false).addAll(m2.get(false));
            return m1;
        };
    }

    @Override
    public Function<Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));
    }


    public static Boolean isPrime(List<Integer> priorCandidates, Integer Candidate){

        int CandidateSquroot =  (int) Math.sqrt( (double)Candidate);
        return
                priorCandidates.stream().takeWhile(  i-> i <= CandidateSquroot)
                        .noneMatch(i-> Candidate % i == 0);
    }
}
