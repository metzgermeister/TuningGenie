package org.tuner;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Pavlo_Ivanenko
 * Date: 8/26/12
 * Time: 12:26 PM
 */
public class TuneAbleParamsDomain {


    private Map<String, ValuesRange> parameterRanges = new HashMap<String, ValuesRange>();

    public static class ValuesRange {
        private final int start;
        private final int finish;

        public ValuesRange(final int start, final int finish) {
            this.start = start;
            this.finish = finish;
        }

        public int getStart() {
            return start;
        }

        public int getFinish() {
            return finish;
        }
    }

    public void addParameterRange(String parameterName, int start, int stop) {
        Validate.isTrue(StringUtils.isNotBlank(parameterName), "parameter name is blank");
        Validate.isTrue(stop > start, String.format("wrong bounds [{%s}, {%s}] for parameter {%s} "
                , start, stop, parameterName));
        this.parameterRanges.put(parameterName, new ValuesRange(start, stop));
    }
}
