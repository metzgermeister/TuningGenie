package org.tuner;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import java.util.*;

/**
 * User: Pavlo_Ivanenko
 * Date: 8/26/12
 * Time: 12:26 PM
 */
public class TuneAbleParamsDomain {


    private Map<String, ValuesRange> parameterRanges = new HashMap<String, ValuesRange>();
    public static final int DEFAULT_STEP = 1;


    public static class ValuesRange {
        private final int start;
        private final int stop;
        private final int step;

        public ValuesRange(final int start, final int stop, int step) {
            this.start = start;
            this.stop = stop;
            this.step = step;
        }

        public int getStart() {
            return start;
        }

        public int getStop() {
            return stop;
        }

        public int getStep() {
            return step;
        }
    }

    public void addParameterRange(String parameterName, int start, int stop, int step) {
        Validate.isTrue(StringUtils.isNotBlank(parameterName), "parameter name is blank");
        Validate.isTrue(stop > start, String.format("wrong bounds [{%s}, {%s}] for parameter {%s} "
                , start, stop, parameterName));
        this.parameterRanges.put(parameterName, new ValuesRange(start, stop, step));
    }

    public ValuesRange getParameterRange(String parameterName) {
        return parameterRanges.get(parameterName);
    }

    @SuppressWarnings("unchecked")
    public List<List<ParameterConfiguration>> getConfigurations() {
        List<List<ParameterConfiguration>> toCalcProduct = fillListsToProduct();
        return cartesian(toCalcProduct);
    }

    private List<List<ParameterConfiguration>> fillListsToProduct() {
        List<List<ParameterConfiguration>> result = new LinkedList<List<ParameterConfiguration>>();

        for (Map.Entry<String, ValuesRange> entry : parameterRanges.entrySet()) {
            ValuesRange value = entry.getValue();
            List<ParameterConfiguration> oneParameterConfigurations = new LinkedList<ParameterConfiguration>();
            for (int i = value.getStart(); i <= value.getStop(); i = i + value.getStep()) {
                oneParameterConfigurations.add(new ParameterConfiguration(entry.getKey(), String.valueOf(i)));
            }
            result.add(oneParameterConfigurations);
        }
        return result;
    }


    public List cartesian(List list) {
        int n = list.size();
        int m = 1;
        for (Object o : list) {
            m *= ((List) o).size();
        }

        List result = new ArrayList(m);
        for (int i = 0; i < m; i++) {
            result.add(new ArrayList(n));
        }

        int repeat = m;
        for (int i = 0; i < list.size(); i++) {
            List list1 = (List) list.get(i);
            int step = repeat;
            int count = m / step;
            repeat /= list1.size();
            for (int j = 0; j < list1.size(); j++) {
                Object o = list1.get(j);
                int offset = j * repeat;
                for (int k = 0; k < count; k++) {
                    for (int t = 0; t < repeat; t++) {
                        int index = offset + k * step + t;
                        List out = (List) result.get(index);
                        out.add(i, o);
                    }
                }
            }
        }
        return result;
    }
}
