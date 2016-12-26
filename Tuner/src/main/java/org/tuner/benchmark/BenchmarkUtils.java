package org.tuner.benchmark;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.tuner.Config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public final class BenchmarkUtils {
    private BenchmarkUtils() {
    }
    
    public static <T> T read(String pathToInputFile, Class<T> valueType) throws IOException {
        return new ObjectMapper()
                .readValue(new File(pathToInputFile), valueType);
    }
    
    public static void write(Object config, String outputFilePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(outputFilePath), config);
    }
    
    public static void cleanupBenchmarkCompletionFile() throws IOException {
        File file = new File(Config.BENCHMARK_COMPLETION_FILE);
        file.delete();
    }
    
    public static boolean benchmarkCompleted() throws IOException {
        return new File(Config.BENCHMARK_COMPLETION_FILE).exists();
    }
    
    public static void writeBenchmarkCompletionFile() throws IOException {
        File file = new File(Config.BENCHMARK_COMPLETION_FILE);
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        writer.write(String.valueOf(new Date().getTime()));
        writer.flush();
        writer.close();
    }
}
