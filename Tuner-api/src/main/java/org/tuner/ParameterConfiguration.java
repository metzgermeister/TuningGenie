package org.tuner;

/**
 * User: Pavlo_Ivanenko
 * Date: 8/28/12
 * Time: 1:04 PM
 */
public class ParameterConfiguration {
    private final String name;
    private final String value;

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

    @Override
    public String toString() {
        return String.format(" %s=%s ", name, value);
    }
}
