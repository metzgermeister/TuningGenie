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

    public static class ParameterValue {
        private final String parameterName;
        private final String parameterValue;

        public ParameterValue(String parameterName, String parameterValue) {

            this.parameterName = parameterName;
            this.parameterValue = parameterValue;
        }

        public String getParameterName() {
            return parameterName;
        }

        public String getParameterValue() {
            return parameterValue;
        }
    }

    public static class ValuesRange {
        private final int start;
        private final int stop;

        public ValuesRange(final int start, final int stop) {
            this.start = start;
            this.stop = stop;
        }

        public int getStart() {
            return start;
        }

        public int getStop() {
            return stop;
        }
    }

    public void addParameterRange(String parameterName, int start, int stop) {
        Validate.isTrue(StringUtils.isNotBlank(parameterName), "parameter name is blank");
        Validate.isTrue(stop > start, String.format("wrong bounds [{%s}, {%s}] for parameter {%s} "
                , start, stop, parameterName));
        this.parameterRanges.put(parameterName, new ValuesRange(start, stop));
    }

    public ValuesRange getParameterRange(String parameterName) {
        return parameterRanges.get(parameterName);
    }

    @SuppressWarnings("unchecked")
    public List<List<ParameterValue>> getConfigurations() {
        List<List<ParameterValue>> toCalcProduct = fillListsToProduct();
        return cartesian(toCalcProduct);
    }

    private List<List<ParameterValue>> fillListsToProduct() {
        List<List<ParameterValue>> result = new LinkedList<List<ParameterValue>>();

        for (Map.Entry<String, ValuesRange> entry : parameterRanges.entrySet()) {
            ValuesRange value = entry.getValue();
            List<ParameterValue> oneParameterValues = new LinkedList<ParameterValue>();
            for (int i = value.getStart(); i <= value.getStop(); i++) {
                oneParameterValues.add(new ParameterValue(entry.getKey(), String.valueOf(i)));
            }
            result.add(oneParameterValues);
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
