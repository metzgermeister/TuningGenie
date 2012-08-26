package org.tuner.termware;

import org.junit.Test;
import org.tuner.TuneAbleParamsDomain;

import static junit.framework.Assert.*;

/**
 * User: Pavlo_Ivanenko
 * Date: 8/26/12
 * Time: 3:10 PM
 */
public class TunerPragmaProcessorTest {

    @Test
    public void shouldProcessPragma() throws Exception {
        TunerPragmaProcessor processor = new TunerPragmaProcessor();
        TuneAbleParamsDomain paramsDomain = new TuneAbleParamsDomain();
        String pragma = "//tuneAbleParam name=findMe start=2 stop=20";
        processor.processPragma(pragma, paramsDomain);
        TuneAbleParamsDomain.ValuesRange parameterRange = paramsDomain.getParameterRange("findMe");
        assertNotNull(parameterRange);
        assertEquals(2, parameterRange.getStart());
        assertEquals(20, parameterRange.getStop());
    }

    @Test
    public void shouldByPassUnknownPragma() throws Exception {
        TunerPragmaProcessor processor = new TunerPragmaProcessor();
        TuneAbleParamsDomain paramsDomain = new TuneAbleParamsDomain();
        String pragma = "//unknownPragma name=findMe start=2 stop=20";
        processor.processPragma(pragma, paramsDomain);
        TuneAbleParamsDomain.ValuesRange parameterRange = paramsDomain.getParameterRange("findMe");
        assertNull(parameterRange);

    }

    @Test(expected = IllegalArgumentException.class)
     public void shouldThrowExceptionWhenParameterNameIsNotDefined() throws Exception {
        TunerPragmaProcessor processor = new TunerPragmaProcessor();
        TuneAbleParamsDomain paramsDomain = new TuneAbleParamsDomain();
        String pragma = "//tuneAbleParam start=2 stop=20";
        processor.processPragma(pragma, paramsDomain);
    }

    @Test(expected = IllegalArgumentException.class)
     public void shouldThrowExceptionWhenTunableParameterStartIsNotDefined() throws Exception {
        TunerPragmaProcessor processor = new TunerPragmaProcessor();
        TuneAbleParamsDomain paramsDomain = new TuneAbleParamsDomain();
        String pragma = "//tuneAbleParam name=findMe  stop=20";
        processor.processPragma(pragma, paramsDomain);
    }

    @Test(expected = IllegalArgumentException.class)
     public void shouldThrowExceptionWhenTunableParameterStopIsNotDefined() throws Exception {
        TunerPragmaProcessor processor = new TunerPragmaProcessor();
        TuneAbleParamsDomain paramsDomain = new TuneAbleParamsDomain();
        String pragma = "//tuneAbleParam name=findMe  start=2";
        processor.processPragma(pragma, paramsDomain);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenTunableParameterStopIsNotInteger() throws Exception {
        TunerPragmaProcessor processor = new TunerPragmaProcessor();
        TuneAbleParamsDomain paramsDomain = new TuneAbleParamsDomain();
        String pragma = "//tuneAbleParam  name=findMe start=2 stop=notInteger";
        processor.processPragma(pragma, paramsDomain);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenTunableParameterStartIsNotInteger() throws Exception {
        TunerPragmaProcessor processor = new TunerPragmaProcessor();
        TuneAbleParamsDomain paramsDomain = new TuneAbleParamsDomain();
        String pragma = "//tuneAbleParam  name=findMe start=notInteger stop=20";
        processor.processPragma(pragma, paramsDomain);
    }
}
