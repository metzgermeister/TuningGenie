package org.tuner;

import org.tuner.benchmark.BenchmarkMaster;
import ua.gradsoft.parsers.java5.JavaParserFactory;
import ua.gradsoft.termware.TermFactory;
import ua.gradsoft.termware.TermWare;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.tuner.Config.fullSourcePath;

/**
 * User: Pavlo_Ivanenko
 * Date: 8/13/12
 * Time: 3:23 PM
 */
public class TuningGenie {
    
    public static void main(String[] args) throws Exception {
        TermWare.getInstance().init(args);
        long start = new Date().getTime();
        new TuningGenie().tune();
        long stop = new Date().getTime();
        System.out.println(String.format("time spent: %s sec", (stop - start) / 1000));
        System.exit(42);
    }
    
    private void tune() throws Exception {
        TuneAbleParamsDomain paramsDomain = new TuneAbleParamsDomain();
        TermWare.getInstance().load(fullSourcePath, new JavaParserFactory(paramsDomain), TermFactory.createNil());
//        System.out.print("initial term:");
//        source.print(System.out);
        List<List<ParameterConfiguration>> configurations = paramsDomain.getConfigurations();
        Map<Long, List<ParameterConfiguration>> benchmarkResults = new BenchmarkMaster()
                .benchmark(configurations, fullSourcePath);
        
        findOptimalConfigturation(benchmarkResults);
        findWorstConfiguration(benchmarkResults);

//        Term reduced = reduce(source, optimalConfiguration);
//        writeSourceCode(reduced, fullOutputSourcePath);
    }
    
    
    private List<ParameterConfiguration> findOptimalConfigturation(Map<Long, List<ParameterConfiguration>> benchmarkResults) {
        Long optimalExecutionTime = Collections.min(benchmarkResults.keySet());
        System.out.println(String.format("optimal execution time = %s", optimalExecutionTime));
        List<ParameterConfiguration> optimalConfiguration = benchmarkResults.get(optimalExecutionTime);
        System.out.println(String.format("Optimal configuration: %s", optimalConfiguration));
        return optimalConfiguration;
    }
    
    
    private List<ParameterConfiguration> findWorstConfiguration(Map<Long, List<ParameterConfiguration>> benchmarkResults) {
        Long worst = Collections.max(benchmarkResults.keySet());
        System.out.println(String.format("worst execution time = %s", worst));
        List<ParameterConfiguration> worstConfiguration = benchmarkResults.get(worst);
        System.out.println(String.format("worst configuration: %s", worstConfiguration));
        return worstConfiguration;
    }
    
    
}
