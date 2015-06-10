package org.tuner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.math.stat.StatUtils;
import org.tuner.classloading.ClassLoadingHelper;
import ua.gradsoft.parsers.java5.JavaParserFactory;
import ua.gradsoft.printers.java5.JavaPrinter;
import ua.gradsoft.termware.*;
import ua.gradsoft.termware.strategies.FirstTopStrategy;

import java.io.*;
import java.util.*;

/**
 * User: Pavlo_Ivanenko
 * Date: 8/13/12
 * Time: 3:23 PM
 */
public class TuningGenie {
    public static final int NUMBER_OF_PROBES = 3;
    private Runtime runtime = Runtime.getRuntime();

    public final String JAVA = ".java";
    public final String CLASS = ".class";

    private final String applicationDirectory = "C:/Java workspace/sorting/Examples/";
    private final String outputClassPath = applicationDirectory + "out/";
    private final String outputDirectory = applicationDirectory + "out/org/tuner/sample/";

    private final String sourceFilePath = "src/main/java/org/tuner/sample/";
//    private final String sourceFileName = "ParallelMergeSort";
//    private final String className = "org.tuner.sample.ParallelMergeSort";
//    private final String sourceFileWrapperName = "ParallelMergeSortWrapper";
//    private final String wrapperName = "org.tuner.sample.ParallelMergeSortWrapper";

    private final String sourceFileName = "EnhancedQuickSort";
    private final String className = "org.tuner.sample.EnhancedQuickSort";
    private final String sourceFileWrapperName = "EnhancedQuickSortWrapper";
    private final String wrapperName = "org.tuner.sample.EnhancedQuickSortWrapper";

    private final String fullSourcePath = applicationDirectory + sourceFilePath + sourceFileName + JAVA;
    private final String fullSourceWrapperPath = applicationDirectory + sourceFilePath + sourceFileWrapperName + JAVA;

    private final String fullOutputSourcePath = outputDirectory + sourceFileName + JAVA;
    private final String outputSourcePathToClass = outputDirectory + sourceFileName + CLASS;
    private final String fullOutputSourceWrapperPath = outputDirectory + sourceFileWrapperName + JAVA;
    private final String outputSourceWrapperPathToClass = outputDirectory + sourceFileWrapperName + CLASS;


    public static void main(String[] args) throws Exception {
        TermWare.getInstance().init(args);
        long start = new Date().getTime();
        new TuningGenie().tune();
        long stop = new Date().getTime();
        System.out.println(String.format("time spent: %s sec", (stop - start) / 1000));
        System.exit(42);
    }

    public void tune() throws Exception {
        TuneAbleParamsDomain paramsDomain = new TuneAbleParamsDomain();
        Term source = TermWare.getInstance().load(fullSourcePath, new JavaParserFactory(paramsDomain), TermFactory.createNil());
        source.print(System.out);
        List<List<ParameterConfiguration>> configurations = paramsDomain.getConfigurations();

        Map<Long, List<ParameterConfiguration>> benchmarkResults = benchmark(source, configurations);

        List<ParameterConfiguration> optimalConfiguration = getOptimalConfiguration(benchmarkResults);
        getWorstConfiguration(benchmarkResults);

        Term reduced = reduce(source, optimalConfiguration);
        writeSourceCode(reduced, fullOutputSourcePath);
    }


    private Map<Long, List<ParameterConfiguration>> benchmark(Term source, List<List<ParameterConfiguration>> configurations) throws Exception {
        Map<Long, List<ParameterConfiguration>> benchmarkResults = new HashMap<Long, List<ParameterConfiguration>>();
        for (List<ParameterConfiguration> configuration : configurations) {
            System.out.println(String.format("configuration: %s", configuration));
            Term reduced = reduce(source, configuration);

            writeSourceCode(reduced, fullOutputSourcePath);
            System.out.print("tuned ");
            copy(fullSourceWrapperPath, fullOutputSourceWrapperPath);

            compileSource(fullOutputSourcePath);
            compileSource(fullOutputSourceWrapperPath);
            System.out.println(" compiled");

            Thread.sleep(1000L);
            long executionTime = execute();

            benchmarkResults.put(executionTime, configuration);
            System.gc();
            Thread.sleep(1000L);
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

    private long execute() throws Exception {
        double[] executionResults = new double[NUMBER_OF_PROBES];
        System.out.print("execution time = ");
        for (int i = 0; i < NUMBER_OF_PROBES; i++) {
            long executionTime = new ClassLoadingHelper().loadAndRun(className,
                    outputSourcePathToClass,
                    wrapperName,
                    outputSourceWrapperPathToClass
            );
            executionResults[i] = executionTime;
            System.out.print(" " + executionTime);
        }
        System.out.println("");

        double mean = StatUtils.mean(executionResults);
        long longMean = (long) mean;
        System.out.println(String.format("mean = %s", longMean));
        return longMean;
    }

    private void copy(String sourceFilePath, String destinationFilePath) throws IOException {
        FileUtils.copyFile(new File(sourceFilePath), new File(destinationFilePath));
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
            String inputTerm = String.format("VariableDeclarator(VariableDeclaratorId(Identifier(\"%s\"),0),IntegerLiteral($whatever)) [$whatever!=%s]", parameterConfiguration.getName(), parameterConfiguration.getValue());
            String outputTerm = String.format("VariableDeclarator(VariableDeclaratorId(Identifier(\"%s\"),0),IntegerLiteral(%s))", parameterConfiguration.getName(), parameterConfiguration.getValue());
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
        source.print(System.out);
        printer.writeTerm(source);
        printer.flush();
        printWriter.close();
    }


}
