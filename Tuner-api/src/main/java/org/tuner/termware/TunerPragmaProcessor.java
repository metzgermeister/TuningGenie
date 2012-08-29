package org.tuner.termware;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.tuner.TuneAbleParamsDomain;

/**
 * User: Pavlo_Ivanenko
 * Date: 8/26/12
 * Time: 2:30 PM
 */
public class TunerPragmaProcessor {

    public static final String PRAGMA_PARAMETER_START = "//tuneAbleParam";
    public static final String PRAGMA_PARAMETERS_SEPARATOR = " ";
    public static final String PRAGMA_PARAM_VALUE_SEPARATOR = "=";
    public static final String PRAGMA_PARAM_NAME_KEY = "name";
    public static final String START = "start";
    public static final String STOP = "stop";
    public static final String STEP = "step";

    public void processPragma(String comment, TuneAbleParamsDomain paramsDomain) {
        if (StringUtils.isNotBlank(comment)) {
            String trimmed = comment.trim();
            if (trimmed.startsWith(PRAGMA_PARAMETER_START)) {
                processParameter(comment, paramsDomain);

            }
        }
    }

    private void processParameter(String comment, TuneAbleParamsDomain paramsDomain) {
        String tunableParameterConfiguration = comment.replace(PRAGMA_PARAMETER_START, "");
        String parameterName = getParameterValue(tunableParameterConfiguration, PRAGMA_PARAM_NAME_KEY);
        String start = getRequiredParameter(tunableParameterConfiguration, START);
        String stop = getRequiredParameter(tunableParameterConfiguration, STOP);
        String step = getParameterValue(tunableParameterConfiguration, STEP);
        int stepValue = step != null ? Integer.valueOf(step) : TuneAbleParamsDomain.DEFAULT_STEP;

        paramsDomain.addParameterRange(parameterName, Integer.valueOf(start), Integer.valueOf(stop), stepValue);

    }

    private String getRequiredParameter(String tunableParameterConfiguration, String parameterName) {
        String parameterValue = getParameterValue(tunableParameterConfiguration, parameterName);
        Validate.notNull(parameterValue, "missing parameter name= " + parameterName);
        return parameterValue;

    }

    private String getParameterValue(String tunableParameterConfiguration, String parameterName) {
        String result = null;
        String[] split = tunableParameterConfiguration.trim().split(PRAGMA_PARAMETERS_SEPARATOR);
        for (String s : split) {
            String[] paramAndValue = s.trim().split(PRAGMA_PARAM_VALUE_SEPARATOR);
            Validate.isTrue(2 == paramAndValue.length, "wrong configuration ");
            if (parameterName.equals(paramAndValue[0])) {
                result = paramAndValue[1];
            }
        }


        return result;
    }
}
