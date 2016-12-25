package org.tuner.benchmark;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.tuner.Config;
import org.tuner.ParameterConfiguration;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BenchmarkMaster {
    public Map<Long, List<ParameterConfiguration>> benchmark(BenchmarkConfiguration config) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String pathToSerializedConfig = Config.BENCHMARK_CONFIG_TO_RUN;
        mapper.writeValue(new File(pathToSerializedConfig), config);
        
        String executableJarPath = Config.projectDirectory + "Tuner/target/Tuner-1.0-jar-with-dependencies.jar";
        Runtime runtime = Runtime.getRuntime();
    
        String command = "java -cp " + executableJarPath + " org.tuner.benchmark.BenchmarkWorker " + pathToSerializedConfig;
        runtime.exec(command);
        
        return Collections.emptyMap();
    }
}
