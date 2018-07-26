package org.tuner.benchmark;

import com.google.common.collect.Lists;

import org.apache.commons.io.IOUtils;
import org.tuner.Config;
import org.tuner.ParameterConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BenchmarkMaster {
    
    public Map<Long, List<ParameterConfiguration>> benchmark(List<List<ParameterConfiguration>> configurations, String fullSourcePath) throws Exception {
        List<List<List<ParameterConfiguration>>> partitioned = Lists.partition(configurations, Config.BENCHMARK_CONFIGURATION_BATCH_SIZE);
        
        System.out.println("master received " + configurations.size() +
                " configs to run and divided them into " + partitioned.size() + " batches");
        Map<Long, List<ParameterConfiguration>> results = new HashMap<>(configurations.size());
        long benchmarkStart = new Date().getTime();
        for (List<List<ParameterConfiguration>> partition : partitioned) {
            results.putAll(benchmarkPartition(fullSourcePath, partition).getResults());
        }
        System.out.println("benchmark took " + (new Date().getTime() - benchmarkStart) + " ms.");
        return results;
    }
    
    private BenchmarkResults benchmarkPartition(String fullSourcePath, List<List<ParameterConfiguration>> partition) throws IOException, InterruptedException {
        BenchmarkConfiguration config = new BenchmarkConfiguration(partition, fullSourcePath);
        String pathToSerializedConfig = Config.BENCHMARK_CONFIG_TO_RUN;
        BenchmarkUtils.write(config, pathToSerializedConfig);
        
        String executableJarPath = Config.projectDirectory + "Tuner/target/Tuner-1.0-jar-with-dependencies.jar";
        Runtime runtime = Runtime.getRuntime();
        
        BenchmarkUtils.cleanupBenchmarkCompletionFile();
        String command = "java -cp " + executableJarPath + " org.tuner.benchmark.BenchmarkWorker " + pathToSerializedConfig 
            + " >> " + Config.WORKER_OUT;
        Process exec = runtime.exec(command);

//        Thread.sleep(1000);
//        InputStream errorStream = exec.getErrorStream();
//        System.out.println("~~~~~~~~~ error stream");
//        IOUtils.copy(errorStream, System.out);

        System.out.println("triggered benchmark for batch " + partition);
        long partitionStart = new Date().getTime();
        while (!BenchmarkUtils.benchmarkCompleted()) {
            long benchmarkRunningTime = new Date().getTime() - partitionStart;
            if (benchmarkRunningTime > Config.BENCHMARK_STATUS_MAX_WAIT_TIMEOUT) {
                System.out.println("waiting timeout exceeded, exiting ");
                System.exit(42);
            }
            System.out.println("waiting for results of batch benchmark. It has been already running for "
                    + (benchmarkRunningTime / 1000) + " seconds.");
            Thread.sleep(Config.BENCHMARK_STATUS_CHECK_TIMEOUT);
        }
        System.out.println("benchmarked batch in " + (new Date().getTime() - partitionStart) + " ms.");
        return BenchmarkUtils.read(Config.BENCHMARK_CONFIG_RESULTS, BenchmarkResults.class);
    }
    
    
}
