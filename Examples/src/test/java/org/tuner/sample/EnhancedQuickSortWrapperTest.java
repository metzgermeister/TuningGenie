package org.tuner.sample;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * User: Pavlo_Ivanenko
 * Date: 8/28/12
 * Time: 4:10 PM
 */
public class EnhancedQuickSortWrapperTest {
    @Test
    public void testCall() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(1);
        EnhancedQuickSortWrapper wrapper = new EnhancedQuickSortWrapper();
        Future<Long> future = pool.submit(wrapper);
        System.out.println(future.get());
    }
}
