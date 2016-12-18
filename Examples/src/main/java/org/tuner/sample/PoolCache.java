package org.tuner.sample;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;

public class PoolCache {
    
     private final static ConcurrentHashMap<Integer,ForkJoinPool> pools = new ConcurrentHashMap<>();
     
     public static ForkJoinPool get(int parallelism) {
         return pools.computeIfAbsent(parallelism, ForkJoinPool::new);
     }
    
}
