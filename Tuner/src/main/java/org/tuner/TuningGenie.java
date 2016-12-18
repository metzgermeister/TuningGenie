package org.tuner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.NullWriter;
import org.apache.commons.math.stat.StatUtils;
import org.tuner.classloading.ClassDefinition;
import org.tuner.classloading.ClassLoadingHelper;
import ua.gradsoft.parsers.java5.JavaParserFactory;
import ua.gradsoft.printers.java5.JavaPrinter;
import ua.gradsoft.termware.DefaultFacts;
import ua.gradsoft.termware.IFacts;
import ua.gradsoft.termware.ITermRewritingStrategy;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermFactory;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.strategies.FirstTopStrategy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * User: Pavlo_Ivanenko
 * Date: 8/13/12
 * Time: 3:23 PM
 */
public class TuningGenie {
    
    private static final int NUMBER_OF_PROBES = 5;
    private Runtime runtime = Runtime.getRuntime();
    
    private final String JAVA = ".java";
    private final String CLASS = ".class";
    
    //TODO pivanenko property file  for all this configs
    private final String applicationDirectory = "/Users/metzgermeister/projects/TuningGenie/Examples/";
    private final String outputClassPath = applicationDirectory + "out/";
    private final String outputDirectory = applicationDirectory + "out/org/tuner/sample/";
    
    private final String sourceFilePath = "src/main/java/org/tuner/sample/";
    private final String sourceFileName = "ParallelMergeSort2";
    private final String className = "org.tuner.sample.ParallelMergeSort2";
    private final String sourceFileWrapperName = "ParallelMergeSortWrapper";
    private final String wrapperName = "org.tuner.sample.ParallelMergeSortWrapper";
    
    private final String fullSourcePath = applicationDirectory + sourceFilePath + sourceFileName + JAVA;
    private final String fullSourceWrapperPath = applicationDirectory + sourceFilePath + sourceFileWrapperName + JAVA;
    
    private final String fullOutputSourcePath = outputDirectory + sourceFileName + JAVA;
    private final String outputSourcePathToClass = outputDirectory + sourceFileName + CLASS;
    private final String fullOutputSourceWrapperPath = outputDirectory + sourceFileWrapperName + JAVA;
    private final String outputSourceWrapperPathToClass = outputDirectory + sourceFileWrapperName + CLASS;
    
    
    private Writer writer;
    private ClassLoadingHelper genericCLHelper = new ClassLoadingHelper();
    private final static Boolean outputResultsToFile = false;
    
    public static void main(String[] args) throws Exception {
        TermWare.getInstance().init(args);
        long start = new Date().getTime();
        new TuningGenie().tune();
        long stop = new Date().getTime();
        System.out.println(String.format("time spent: %s sec", (stop - start) / 1000));
        System.exit(42);
    }
    
    private void tune() throws Exception {
        
        writer = buildWriter();
        
        
        TuneAbleParamsDomain paramsDomain = new TuneAbleParamsDomain();
        Term source = TermWare.getInstance().load(fullSourcePath, new JavaParserFactory(paramsDomain), TermFactory.createNil());
//        System.out.print("initial term:");
//        source.print(System.out);
        List<List<ParameterConfiguration>> configurations = paramsDomain.getConfigurations();
        
        loadGenericClasses();
        Map<Long, List<ParameterConfiguration>> benchmarkResults = benchmark(source, configurations);
        
        writer.flush();
        writer.close();
        
        List<ParameterConfiguration> optimalConfiguration = getOptimalConfiguration(benchmarkResults);
        getWorstConfiguration(benchmarkResults);
        
        Term reduced = reduce(source, optimalConfiguration);
        writeSourceCode(reduced, fullOutputSourcePath);
    }
    
    private Writer buildWriter() throws IOException {
        if (outputResultsToFile) {
            File file = new File("/Users/metzgermeister/temp/tuningGenie_" + new Date().getTime() + ".csv");
            System.out.println("created output file:" + file.createNewFile());
            return new FileWriter(file);
        } else {
            return new NullWriter();
        }
    }
    
    private void loadGenericClasses() throws Exception {
        copyAndCompileCache();
        genericCLHelper.load(new ClassDefinition("org.tuner.sample.PoolCache",
                outputDirectory + "PoolCache.class"));
    }
    
    private void copyAndCompileCache() throws IOException {
        FileUtils.copyFile(new File(applicationDirectory + sourceFilePath + "PoolCache.java"),
                new File(outputDirectory + "PoolCache.java"));
        
        compileSource(outputDirectory + "PoolCache.java");
    }
    
    
    private Map<Long, List<ParameterConfiguration>> benchmark(Term source, List<List<ParameterConfiguration>> configurations) throws Exception {
        Map<Long, List<ParameterConfiguration>> benchmarkResults = new HashMap<>();
        for (List<ParameterConfiguration> configuration : configurations) {
            File directory = new File(outputDirectory);
            if (directory.exists()) {
                FileUtils.cleanDirectory(directory);
            }
            copyAndCompileCache();
            System.out.println(String.format("configuration: %s", configuration));
            Term reduced = reduce(source, configuration);
            
            writeSourceCode(reduced, fullOutputSourcePath);
            System.out.print(" instrumented ");
            FileUtils.copyFile(new File(fullSourceWrapperPath), new File(fullOutputSourceWrapperPath));
            
            compileSource(fullOutputSourcePath);
            compileSource(fullOutputSourceWrapperPath);
            System.out.println(" compiled");
            
            Thread.sleep(2000L);
            long executionTime = execute(configuration);
            
            benchmarkResults.put(executionTime, configuration);
            System.gc();
            Thread.sleep(2000L);
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        }
        return benchmarkResults;
    }
    
    private List<ParameterConfiguration> getOptimalConfiguration(Map<Long, List<ParameterConfiguration>> benchmarkResults) {
        Long optimalExecutionTime = Collections.min(benchmarkResults.keySet());
        System.out.println(String.format("optimal execution time = %s", optimalExecutionTime));
        List<ParameterConfiguration> optimalConfiguration = benchmarkResults.get(optimalExecutionTime);
        System.out.println(String.format("Optimal configuration: %s", optimalConfiguration));
        return optimalConfiguration;
    }
    
    
    private List<ParameterConfiguration> getWorstConfiguration(Map<Long, List<ParameterConfiguration>> benchmarkResults) {
        Long worst = Collections.max(benchmarkResults.keySet());
        System.out.println(String.format("worst execution time = %s", worst));
        List<ParameterConfiguration> worstConfiguration = benchmarkResults.get(worst);
        System.out.println(String.format("worst configuration: %s", worstConfiguration));
        return worstConfiguration;
    }
    
    private long execute(List<ParameterConfiguration> configuration) throws Exception {
        double[] executionResults = new double[NUMBER_OF_PROBES];
        System.out.print("execution time = ");
        for (int i = 0; i < NUMBER_OF_PROBES; i++) {
            ClassLoadingHelper clHelper = new ClassLoadingHelper(genericCLHelper.getClassLoader());
            long executionTime = clHelper.loadAndRun(
                    new ClassDefinition(wrapperName, outputSourceWrapperPathToClass),
                    new ClassDefinition(className, outputSourcePathToClass)
            );
            executionResults[i] = executionTime;
            System.out.print(" " + executionTime);
            logResult(configuration, executionTime);
        }
        System.out.println("");
        
        double mean = StatUtils.mean(executionResults);
        long longMean = (long) mean;
        System.out.println(String.format("configuration: %s mean = %s", configuration, longMean));
        return longMean;
    }
    
    private void logResult(List<ParameterConfiguration> configuration, long executionTime) throws IOException {
        writer.write(configuration.stream().map(ParameterConfiguration::getValue).collect(Collectors.joining(",")));
        writer.write("," + executionTime + "\n");
        writer.flush();
    }
    
    private void compileSource(String fullOutputSourcePath) throws IOException {
        
        runtime.exec("javac " + fullOutputSourcePath + " -classpath " + outputClassPath);
    }
    
    private Term reduce(Term source, List<ParameterConfiguration> configuration) throws TermWareException {
        ITermRewritingStrategy strategy = new FirstTopStrategy();
        IFacts facts = new DefaultFacts();
        TermSystem termSystem = new
                TermSystem(strategy, facts, TermWare.getInstance());
        for (ParameterConfiguration parameterConfiguration : configuration) {
            String inputTerm = String.format("VariableDeclarator(VariableDeclaratorId(Identifier(\"%s\"),0),IntegerLiteral($whatever)) [$whatever!=%s]",
                    parameterConfiguration.getName(), parameterConfiguration.getValue());
            String outputTerm = String.format("VariableDeclarator(VariableDeclaratorId(Identifier(\"%s\"),0),IntegerLiteral(%s))",
                    parameterConfiguration.getName(), parameterConfiguration.getValue());
            String rule = String.format("%s->%s", inputTerm, outputTerm);
            termSystem.addRule(rule);
        }
        
        return termSystem.reduce(source);
    }
    
    private void writeSourceCode(Term source, String filePath) throws TermWareException, FileNotFoundException {
        File file = new File(filePath);
        String parentFolder = file.getParent();
        new File(parentFolder).mkdirs();
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)));
        JavaPrinter printer = new JavaPrinter(printWriter, "");
//        source.print(System.out);
        printer.writeTerm(source);
        printer.flush();
        printWriter.close();
    }
    
    
}
