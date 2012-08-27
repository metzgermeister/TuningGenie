package org.test;

import org.tuner.TuneAbleParamsDomain;
import ua.gradsoft.parsers.java5.JavaParserFactory;
import ua.gradsoft.printers.java5.JavaPrinter;
import ua.gradsoft.termware.*;
import ua.gradsoft.termware.strategies.FirstTopStrategy;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

/**
 * User: Pavlo_Ivanenko
 * Date: 8/13/12
 * Time: 3:23 PM
 */
public class ParserExample {

    public static void main(String[] args) throws TermWareException {

        TermWare.getInstance().init(args);

        String fileName = "D:\\java_workspace\\sorting\\Tuner\\src\\main\\java\\org\\test\\sample\\Example1.java";

        TuneAbleParamsDomain paramsDomain = new TuneAbleParamsDomain();
        JavaParserFactory parserFactory = new JavaParserFactory(paramsDomain);
        Term source = TermWare.getInstance().load(fileName, parserFactory, TermFactory.createNil());
        source.print(System.out);
        for (List<TuneAbleParamsDomain.ParameterConfiguration>  configuration : paramsDomain.getConfigurations()) {
            Term reduced = reduce(source, configuration);
            printSourceCode(reduced);
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
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

    private static void printSourceCode(Term source) throws TermWareException {
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(System.out));
        JavaPrinter printer = new JavaPrinter(printWriter, "");
        printer.writeTerm(source);
        printer.flush();
    }


}
