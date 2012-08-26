package org.test;

import ua.gradsoft.parsers.java5.JavaParserFactory;
import ua.gradsoft.printers.java5.JavaPrinter;
import ua.gradsoft.termware.*;
import ua.gradsoft.termware.strategies.FirstTopStrategy;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Created with IntelliJ IDEA.
 * User: Pavlo_Ivanenko
 * Date: 8/13/12
 * Time: 3:23 PM
 */
public class ParserExample {

    public static void main(String[] args) throws TermWareException {

        TermWare.getInstance().init(args);

        String fileName = "D:\\java_workspace\\sorting\\experiments\\src\\org\\test\\Example1.java";

        JavaParserFactory parserFactory = new JavaParserFactory();
        Term source = TermWare.getInstance().load(fileName, parserFactory, TermFactory.createNil());
        source.print(System.out);


//        Term reduced = reduce(source);
//        printSourceCode(source);

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
