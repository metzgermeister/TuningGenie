package org.tuner;

import junit.framework.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * User: Pavlo_Ivanenko
 * Date: 8/27/12
 * Time: 9:47 AM
 */
public class TuneAbleParamsDomainTest {
    @Test
    public void shouldGenerateConfiguration() throws Exception {
        TuneAbleParamsDomain tuneAbleParamsDomain = new TuneAbleParamsDomain();
        tuneAbleParamsDomain.addParameterRange("first", 0, 2);
        tuneAbleParamsDomain.addParameterRange("second", 0, 1);

        List<List<TuneAbleParamsDomain.ParameterConfiguration>> configurations = tuneAbleParamsDomain.getConfigurations();
        Assert.assertEquals(6, configurations.size());
        Assert.assertTrue(assertContainsConfiguration(configurations, "first", "0", "second", "0"));
        Assert.assertTrue(assertContainsConfiguration(configurations, "first", "1", "second", "0"));
        Assert.assertTrue(assertContainsConfiguration(configurations, "first", "2", "second", "0"));
        Assert.assertTrue(assertContainsConfiguration(configurations, "first", "0", "second", "1"));
        Assert.assertTrue(assertContainsConfiguration(configurations, "first", "1", "second", "1"));
        Assert.assertTrue(assertContainsConfiguration(configurations, "first", "2", "second", "1"));

    }

    private boolean assertContainsConfiguration(List<List<TuneAbleParamsDomain.ParameterConfiguration>> configurations, String paramOne, String valueOne, String paramTwo, String valueTwo) {
        for (List<TuneAbleParamsDomain.ParameterConfiguration> configuration : configurations) {
            boolean containsFirst = containsParameterValue(configuration,paramOne, valueOne);
            boolean containsSecond =  containsParameterValue(configuration,paramTwo, valueTwo);
            if (containsFirst && containsSecond) {
                return true;
            }
        }
        return false;
    }

    private boolean containsParameterValue(List<TuneAbleParamsDomain.ParameterConfiguration> configuration, String parameterName, String parameterValue) {
        for (TuneAbleParamsDomain.ParameterConfiguration value: configuration) {
            if (value.getParameterName().equals(parameterName) && value.getParameterValue().equals(parameterValue))  {
                return true;
            }
        }
        return false;
    }

    @Test
    public void testCartesian() throws Exception {
        List list = Arrays.asList(Arrays.asList("a", "b", "c"), Arrays.asList("d"), Arrays.asList(5, 6));
//        System.out.println(list);

        list = new TuneAbleParamsDomain().cartesian(list);

        System.out.println(list);
    }
}


