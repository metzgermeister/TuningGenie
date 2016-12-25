package org.tuner.benchmark;

import org.tuner.ParameterConfiguration;

import java.io.Serializable;
import java.util.List;

public class BenchmarkConfiguration implements Serializable {
    private static final long serialVersionUID = 42L;
    
    private List<List<ParameterConfiguration>> configurationsToRun;
    private String pathToFileToTune;
    
    public BenchmarkConfiguration() {
    }
    
    public BenchmarkConfiguration(List<List<ParameterConfiguration>> configurations, String pathToFileToTune) {
        setConfigurationsToRun(configurations);
        setPathToFileToTune(pathToFileToTune);
    }
    
    public List<List<ParameterConfiguration>> getConfigurationsToRun() {
        return configurationsToRun;
    }
    
    public void setConfigurationsToRun(List<List<ParameterConfiguration>> configurationsToRun) {
        this.configurationsToRun = configurationsToRun;
    }
    
    public String getPathToFileToTune() {
        return pathToFileToTune;
    }
    
    public void setPathToFileToTune(String pathToFileToTune) {
        this.pathToFileToTune = pathToFileToTune;
    }
}
