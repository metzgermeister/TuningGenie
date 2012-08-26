package org.tuner;

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

        new ValuesRange(start, stop);
    }
}
