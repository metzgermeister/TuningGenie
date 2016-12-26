package org.tuner.benchmark;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

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
    
}
