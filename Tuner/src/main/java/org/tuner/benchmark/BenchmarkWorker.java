package org.tuner.benchmark;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.NullWriter;
import org.apache.commons.lang3.Validate;
import org.apache.commons.math.stat.StatUtils;
import org.tuner.Config;
import org.tuner.ParameterConfiguration;
import org.tuner.TuneAbleParamsDomain;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.tuner.Config.NUMBER_OF_PROBES;
import static org.tuner.Config.STATS_OUTPUT_DIR;
import static org.tuner.Config.className;
import static org.tuner.Config.examplesProjectDirectory;
import static org.tuner.Config.fullOutputSourcePath;
import static org.tuner.Config.fullOutputSourceWrapperPath;
import static org.tuner.Config.fullSourceWrapperPath;
import static org.tuner.Config.outputClassPath;
import static org.tuner.Config.outputDirectory;
import static org.tuner.Config.outputResultsToFile;
import static org.tuner.Config.outputSourcePathToClass;
import static org.tuner.Config.outputSourceWrapperPathToClass;
import static org.tuner.Config.sourceFilePath;
import static org.tuner.Config.wrapperName;


class BenchmarkWorker {
    private Writer writer;
    private ClassLoadingHelper genericCLHelper = new ClassLoadingHelper();
    private Runtime runtime = Runtime.getRuntime();
    
    public static void main(String[] args) throws Exception {

//        TODO pivanenko to not run from command line
//        args = new String[]{Config.BENCHMARK_CONFIG_TO_RUN};
//        Validate.isTrue(args.length == 1, "unexpected arguments");

        System.out.println(" in Worker");
        
        BenchmarkUtils.cleanupBenchmarkCompletionFile();
        String pathToBenchmarkConfig = Config.BENCHMARK_CONFIG_TO_RUN;
        BenchmarkConfiguration config = BenchmarkUtils.read(pathToBenchmarkConfig, BenchmarkConfiguration.class);
        
        TermWare.getInstance().init(args);
        Term source = TermWare.getInstance()
                .load(config.getPathToFileToTune(), new JavaParserFactory(new TuneAbleParamsDomain()), TermFactory.createNil());
        //TODO pivanenko logging
        BenchmarkResults results = new BenchmarkWorker().benchmark(source, config);
        BenchmarkUtils.write(results, Config.BENCHMARK_CONFIG_RESULTS);
        BenchmarkUtils.writeBenchmarkCompletionFile();
        System.exit(42);
    }
    
    
    public BenchmarkResults benchmark(Term source, BenchmarkConfiguration config) throws Exception {
        writer = buildWriter();
        
        loadGenericClasses();
        
        Map<Long, List<ParameterConfiguration>> benchmarkResults = new HashMap<>();
        for (List<ParameterConfiguration> configuration : config.getConfigurationsToRun()) {
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
        
        writer.flush();
        writer.close();
        BenchmarkResults results = new BenchmarkResults();
        results.setResults(benchmarkResults);
        return results;
    }
    
    private void loadGenericClasses() throws Exception {
        copyAndCompileCache();
        genericCLHelper.load(new ClassDefinition("org.tuner.sample.PoolCache",
                outputDirectory + "PoolCache.class"));
    }
    
    private void copyAndCompileCache() throws IOException {
        FileUtils.copyFile(new File(examplesProjectDirectory + sourceFilePath + "PoolCache.java"),
                new File(outputDirectory + "PoolCache.java"));
        
        compileSource(outputDirectory + "PoolCache.java");
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
    
    private void compileSource(String fullOutputSourcePath) throws IOException {
        runtime.exec("javac " + fullOutputSourcePath + " -classpath " + outputClassPath);
    }
    
    private Writer buildWriter() throws IOException {
        if (outputResultsToFile) {
            File file = new File(STATS_OUTPUT_DIR + "tuner_" + new Date().getTime() + ".csv");
            System.out.println("created output file:" + file.createNewFile());
            return new FileWriter(file);
        } else {
            return new NullWriter();
        }
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
}
