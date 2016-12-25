package org.tuner;

public final class Config {
    private Config() {
    }
    
    public static final int NUMBER_OF_PROBES = 5;
    public static final String STATS_OUTPUT_DIR = "/Users/metzgermeister/temp/";
    
    public static final String JAVA = ".java";
    public static final String CLASS = ".class";
    
    public static final String applicationDirectory = "/Users/metzgermeister/projects/TuningGenie/Examples/";
    public static final String outputClassPath = applicationDirectory + "out/";
    public static final String outputDirectory = applicationDirectory + "out/org/tuner/sample/";
    
    public static final String sourceFilePath = "src/main/java/org/tuner/sample/";
    public static final String sourceFileName = "ParallelMergeSort2";
    public static final String className = "org.tuner.sample.ParallelMergeSort2";
    public static final String sourceFileWrapperName = "ParallelMergeSortWrapper";
    public static final String wrapperName = "org.tuner.sample.ParallelMergeSortWrapper";
    
    public static final String fullSourcePath = applicationDirectory + sourceFilePath + sourceFileName + JAVA;
    public static final String fullSourceWrapperPath = applicationDirectory + sourceFilePath + sourceFileWrapperName + JAVA;
    
    public static final String fullOutputSourcePath = outputDirectory + sourceFileName + JAVA;
    public static final String outputSourcePathToClass = outputDirectory + sourceFileName + CLASS;
    public static final String fullOutputSourceWrapperPath = outputDirectory + sourceFileWrapperName + JAVA;
    public static final String outputSourceWrapperPathToClass = outputDirectory + sourceFileWrapperName + CLASS;
    public final static Boolean outputResultsToFile = true;
}
