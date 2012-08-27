package org.test;

import org.tuner.Configuration;
import org.tuner.TuneAbleParamsDomain;
import ua.gradsoft.parsers.java5.JavaParserFactory;
import ua.gradsoft.printers.java5.JavaPrinter;
import ua.gradsoft.termware.*;
import ua.gradsoft.termware.strategies.FirstTopStrategy;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * User: Pavlo_Ivanenko
 * Date: 8/13/12
 * Time: 3:23 PM
 */
public class ParserExample {

    public static void main(String[] args) throws TermWareException {

        TermWare.getInstance().init(args);

        String fileName = "D:\\java_workspace\\sorting\\Tuner\\src\\main\\java\\org\\test\\Example1.java";

        TuneAbleParamsDomain paramsDomain = new TuneAbleParamsDomain();
        JavaParserFactory parserFactory = new JavaParserFactory(paramsDomain);
        Term source = TermWare.getInstance().load(fileName, parserFactory, TermFactory.createNil());
//        source.print(System.out);

        for (Configuration configuration : paramsDomain.getConfigurations()) {
            Term reduced = reduce(source);
            printSourceCode(reduced);
//            Class.forName()
        }


    }

    private static Term reduce(Term source) throws TermWareException {
        ITermRewritingStrategy strategy = new FirstTopStrategy();
        IFacts facts = new DefaultFacts();
        TermSystem termSystem = new
                TermSystem(strategy, facts, TermWare.getInstance());
        termSystem.addRule("Identifier(\"ohFindMe\")->Identifier(\"found\")");
        return termSystem.reduce(source);
    }

    private static void printSourceCode(Term source) throws TermWareException {
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(System.out));
        JavaPrinter printer = new JavaPrinter(printWriter, "");
        printer.writeTerm(source);
        printer.flush();
    }


}
