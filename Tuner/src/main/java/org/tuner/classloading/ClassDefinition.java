package org.tuner.classloading;

public class ClassDefinition {
    private final String className;
    private final String classFilePath;
    
    public ClassDefinition(String className, String classFilePath) {
        this.className = className;
        this.classFilePath = classFilePath;
    }
    
    public String getClassName() {
        return className;
    }
    
    public String getClassFilePath() {
        return classFilePath;
    }
}
