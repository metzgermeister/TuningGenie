package org.tuner;

import ua.gradsoft.parsers.java5.JavaParserFactory;
import ua.gradsoft.printers.java5.JavaPrinter;
import ua.gradsoft.termware.*;
import ua.gradsoft.termware.strategies.FirstTopStrategy;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

/**
 * User: Pavlo_Ivanenko
 * Date: 8/13/12
 * Time: 3:23 PM
 */
public class TuningGenie {

    private static final String applicationDirectory = "D:/java_workspace/sorting/Examples/";
    private static final String outputDirectory = applicationDirectory + "/out/";

    private static final String sourceFilePath = "src/main/java/org/tuner/sample/";
    private static final String sourceFileName = "EnhancedQuickSort.java";
    private static final String fullSourcePath = applicationDirectory + sourceFilePath + sourceFileName;

    private static final String fullOutputSourcePath = outputDirectory + sourceFileName;

    public static void main(String[] args) throws TermWareException, FileNotFoundException {

        TermWare.getInstance().init(args);

        TuneAbleParamsDomain paramsDomain = new TuneAbleParamsDomain();
        JavaParserFactory parserFactory = new JavaParserFactory(paramsDomain);
        Term source = TermWare.getInstance().load(fullSourcePath, parserFactory, TermFactory.createNil());
        for (List<TuneAbleParamsDomain.ParameterConfiguration>  configuration : paramsDomain.getConfigurations()) {
            Term reduced = reduce(source, configuration);
            printSourceCode(reduced, fullOutputSourcePath);
            System.out.println("tik");
        }

    }

    private static Term reduce(Term source, List<TuneAbleParamsDomain.ParameterConfiguration> configuration) throws TermWareException {
        ITermRewritingStrategy strategy = new FirstTopStrategy();
        IFacts facts = new DefaultFacts();
        TermSystem termSystem = new
                TermSystem(strategy, facts, TermWare.getInstance());
        for (TuneAbleParamsDomain.ParameterConfiguration parameterConfiguration : configuration) {
            String inputTerm =  String.format("VariableDeclarator(VariableDeclaratorId(Identifier(\"%s\"),0),IntegerLiteral($whatever)) [$whatever!=%s]", parameterConfiguration.getParameterName(),parameterConfiguration.getParameterValue());
            String outputTerm = String.format("VariableDeclarator(VariableDeclaratorId(Identifier(\"%s\"),0),IntegerLiteral(%s))", parameterConfiguration.getParameterName(), parameterConfiguration.getParameterValue());
            String rule = String.format("%s->%s", inputTerm, outputTerm);
            termSystem.addRule(rule);
        }

        return termSystem.reduce(source);
    }

    private static void printSourceCode(Term source, String filePath) throws TermWareException, FileNotFoundException {
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filePath)));
        JavaPrinter printer = new JavaPrinter(printWriter, "");
        printer.writeTerm(source);
        printer.flush();
        printWriter.close();
    }


}
