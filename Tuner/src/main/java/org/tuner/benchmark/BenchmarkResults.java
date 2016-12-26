package org.tuner.benchmark;

import org.tuner.ParameterConfiguration;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class BenchmarkResults implements Serializable {
    
    private static final long serialVersionUID = -7150726148660590787L;
    private Map<Long, List<ParameterConfiguration>> results;
    
    public Map<Long, List<ParameterConfiguration>> getResults() {
        return results;
    }
    
    public void setResults(Map<Long, List<ParameterConfiguration>> results) {
        this.results = results;
    }
}
