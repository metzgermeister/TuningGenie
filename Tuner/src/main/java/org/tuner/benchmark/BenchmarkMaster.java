package org.tuner.benchmark;

import org.tuner.Config;
import org.tuner.ParameterConfiguration;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class BenchmarkMaster {
    public Map<Long, List<ParameterConfiguration>> benchmark(BenchmarkConfiguration config) throws Exception {
        String pathToSerializedConfig = Config.BENCHMARK_CONFIG_TO_RUN;
        BenchmarkUtils.write(config, pathToSerializedConfig);
        
        String executableJarPath = Config.projectDirectory + "Tuner/target/Tuner-1.0-jar-with-dependencies.jar";
        Runtime runtime = Runtime.getRuntime();
        
        BenchmarkUtils.cleanupBenchmarkCompletionFile();
        String command = "java -cp " + executableJarPath + " org.tuner.benchmark.BenchmarkWorker " + pathToSerializedConfig;
        runtime.exec(command);
        
        System.out.println("triggered benchmark");
        long benchmarkStart = new Date().getTime();
        while (!BenchmarkUtils.benchmarkCompleted()) {
            long benchmarkRunningTime = new Date().getTime() - benchmarkStart;
            if (benchmarkRunningTime > Config.BENCHMARK_STATUS_MAX_WAIT_TIMEOUT) {
                System.out.println("waiting timeout exceeded, exiting ");
                System.exit(42);
            }
            System.out.println("waiting for results");
            Thread.sleep(Config.BENCHMARK_STATUS_CHECK_TIMEOUT);
        }
        BenchmarkResults results = BenchmarkUtils.read(Config.BENCHMARK_CONFIG_RESULTS, BenchmarkResults.class);
        return results.getResults();
    }
    
    
}
