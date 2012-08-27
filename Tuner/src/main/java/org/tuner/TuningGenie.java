package org.tuner;

import org.apache.commons.io.FileUtils;
import ua.gradsoft.parsers.java5.JavaParserFactory;
import ua.gradsoft.printers.java5.JavaPrinter;
import ua.gradsoft.termware.*;
import ua.gradsoft.termware.strategies.FirstTopStrategy;

import java.io.*;
import java.util.List;

/**
 * User: Pavlo_Ivanenko
 * Date: 8/13/12
 * Time: 3:23 PM
 */
public class TuningGenie {
    private Runtime runtime = Runtime.getRuntime();
    public  final String JAVA = ".java";

    private  final String applicationDirectory = "D:/java_workspace/sorting/Examples/";
    private  final String outputClassPath = applicationDirectory + "out/";
    private  final String outputDirectory = applicationDirectory + "out/org/tuner/sample/";

    private  final String sourceFilePath = "src/main/java/org/tuner/sample/";
    private  final String sourceFileName = "EnhancedQuickSort";
    private  final String sourceFileWrapperName = "EnhancedQuickSortWrapper";

    private  final String fullSourcePath = applicationDirectory + sourceFilePath + sourceFileName + JAVA;
    private  final String fullSourceWrapperPath = applicationDirectory + sourceFilePath + sourceFileWrapperName + JAVA;

    private  final String fullOutputSourcePath = outputDirectory + sourceFileName + JAVA;
    private  final String fullOutputSourceWrapperPath = outputDirectory + sourceFileWrapperName + JAVA;

    public static void main(String[] args) throws Exception {

        TermWare.getInstance().init(args);
        new TuningGenie().tune();
    }

    public void tune() throws TermWareException, IOException {
        //TODO pivanenko cleanup output directory
        TuneAbleParamsDomain paramsDomain = new TuneAbleParamsDomain();
        JavaParserFactory parserFactory = new JavaParserFactory(paramsDomain);
        Term source = TermWare.getInstance().load(fullSourcePath, parserFactory, TermFactory.createNil());
        for (List<TuneAbleParamsDomain.ParameterConfiguration> configuration : paramsDomain.getConfigurations()) {
            Term reduced = reduce(source, configuration);

            writeSourceCode(reduced, fullOutputSourcePath);
            System.out.println("tuned source code was saved to " + fullOutputSourcePath);
            copy(fullSourceWrapperPath, fullOutputSourceWrapperPath);
            System.out.println("copied wrapper to " + fullOutputSourceWrapperPath);

            compileSource(fullOutputSourcePath);
            System.out.println("compiled source");
            compileSource(fullOutputSourceWrapperPath);
            System.out.println("compiled wrapper");

            long executionTime = execute();


        }
    }

    private long execute() {
       return 7;

    }

    private void copy(String sourceFilePath, String destinationFilePath) throws IOException {
        FileUtils.copyFile(new File(sourceFilePath), new File(destinationFilePath));
    }

    private void compileSource(String fullOutputSourcePath) throws IOException {

        runtime.exec("javac " + fullOutputSourcePath + " -classpath " + outputClassPath);
    }

    private Term reduce(Term source, List<TuneAbleParamsDomain.ParameterConfiguration> configuration) throws TermWareException {
        ITermRewritingStrategy strategy = new FirstTopStrategy();
        IFacts facts = new DefaultFacts();
        TermSystem termSystem = new
                TermSystem(strategy, facts, TermWare.getInstance());
        for (TuneAbleParamsDomain.ParameterConfiguration parameterConfiguration : configuration) {
            String inputTerm = String.format("VariableDeclarator(VariableDeclaratorId(Identifier(\"%s\"),0),IntegerLiteral($whatever)) [$whatever!=%s]", parameterConfiguration.getParameterName(), parameterConfiguration.getParameterValue());
            String outputTerm = String.format("VariableDeclarator(VariableDeclaratorId(Identifier(\"%s\"),0),IntegerLiteral(%s))", parameterConfiguration.getParameterName(), parameterConfiguration.getParameterValue());
            String rule = String.format("%s->%s", inputTerm, outputTerm);
            termSystem.addRule(rule);
        }

        return termSystem.reduce(source);
    }

    private void writeSourceCode(Term source, String filePath) throws TermWareException, FileNotFoundException {
        File file = new File(filePath);
        String parentFolder = file.getParent();
        new File(parentFolder).mkdirs();
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)));
        JavaPrinter printer = new JavaPrinter(printWriter, "");
        printer.writeTerm(source);
        printer.flush();
        printWriter.close();
    }


}
