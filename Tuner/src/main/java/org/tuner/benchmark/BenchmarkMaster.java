package org.tuner.benchmark;

import org.tuner.Config;
import org.tuner.ParameterConfiguration;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BenchmarkMaster {
    public Map<Long, List<ParameterConfiguration>> benchmark(BenchmarkConfiguration config) throws Exception {
        String pathToSerializedConfig = Config.BENCHMARK_CONFIG_TO_RUN;
        BenchmarkUtils.write(config, pathToSerializedConfig);
        
        String executableJarPath = Config.projectDirectory + "Tuner/target/Tuner-1.0-jar-with-dependencies.jar";
        Runtime runtime = Runtime.getRuntime();
        
        String command = "java -cp " + executableJarPath + " org.tuner.benchmark.BenchmarkWorker " + pathToSerializedConfig;
        runtime.exec(command);
        
        return Collections.emptyMap();
    }
    
    
}
