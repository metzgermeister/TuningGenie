package org.tuner;

public final class Config {
    private Config() {
    }
    
    public static final int NUMBER_OF_PROBES = 5;
    
    public static final String BENCHMARK_WORKING_DIRECTORY = "D:\\Projects\\_iss\\pub4\\tuner";
    public static final String BENCHMARK_CONFIG_TO_RUN = BENCHMARK_WORKING_DIRECTORY + "configToRun.json";
    public static final String WORKER_OUT = BENCHMARK_WORKING_DIRECTORY + "worker.out";
    public static final String BENCHMARK_CONFIG_RESULTS = BENCHMARK_WORKING_DIRECTORY + "configResults.json";
    public static final String BENCHMARK_COMPLETION_FILE = BENCHMARK_WORKING_DIRECTORY + "configWasProbed.txt";
    public static final long BENCHMARK_STATUS_CHECK_TIMEOUT = 5000;
    public static final long BENCHMARK_STATUS_MAX_WAIT_TIMEOUT = 15 * 60 * 1000;
    public static final int BENCHMARK_CONFIGURATION_BATCH_SIZE = 20;
    
    public static final String STATS_OUTPUT_DIR = BENCHMARK_WORKING_DIRECTORY + "rawResults\\";
    
    public static final String JAVA = ".java";
    public static final String CLASS = ".class";
    
    public static final String projectDirectory = "D:\\Projects\\_iss\\pub4\\tuner\\sources";
    public static final String examplesProjectDirectory = projectDirectory + "Examples\\";
    public static final String outputClassPath = examplesProjectDirectory + "out\\";
    public static final String outputDirectory = examplesProjectDirectory + "out\\org\\tuner\\sample\\";
    
    public static final String sourceFilePath = "src\\main\\java\\org\\tuner\\sample\\";
    public static final String sourceFileName = "ParallelMergeSort2";
    public static final String className = "org.tuner.sample.ParallelMergeSort2";
    public static final String sourceFileWrapperName = "ParallelMergeSortWrapper";
    public static final String wrapperName = "org.tuner.sample.ParallelMergeSortWrapper";
    
    public static final String fullSourcePath = examplesProjectDirectory + sourceFilePath + sourceFileName + JAVA;
    public static final String fullSourceWrapperPath = examplesProjectDirectory + sourceFilePath + sourceFileWrapperName + JAVA;
    
    public static final String fullOutputSourcePath = outputDirectory + sourceFileName + JAVA;
    public static final String outputSourcePathToClass = outputDirectory + sourceFileName + CLASS;
    public static final String fullOutputSourceWrapperPath = outputDirectory + sourceFileWrapperName + JAVA;
    public static final String outputSourceWrapperPathToClass = outputDirectory + sourceFileWrapperName + CLASS;
    public final static Boolean outputResultsToFile = true;
}
