package org.tuner;

import java.io.Serializable;

/**
 * User: Pavlo_Ivanenko
 * Date: 8/28/12
 * Time: 1:04 PM
 */
public class ParameterConfiguration implements Serializable {
    private String name;
    private String value;
    
    public ParameterConfiguration() {
    }
    
    public ParameterConfiguration(String name, String value) {
        
        this.name = name;
        this.value = value;
    }
    
    public String getName() {
        return name;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return String.format(" %s=%s ", name, value);
    }
}
