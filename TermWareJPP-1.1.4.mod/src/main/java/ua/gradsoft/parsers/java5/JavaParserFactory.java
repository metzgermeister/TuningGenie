/*
 * JavaParserFactory.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.parsers.java5;

import org.tuner.TuneAbleParamsDomain;
import ua.gradsoft.parsers.java5.jjt.JJTJavaParser;
import ua.gradsoft.termware.*;

import java.io.Reader;

/**
 * Adapter to TermWare IParserFactory interface
 *
 * @author Ruslan Shevchenko
 */
public class JavaParserFactory implements IParserFactory {

    private final TuneAbleParamsDomain paramsDomain;

    public TuneAbleParamsDomain getParamsDomain() {
        return paramsDomain;
    }

    public JavaParserFactory(TuneAbleParamsDomain paramsDomain) {
        this.paramsDomain = paramsDomain;
    }

    /**
     * create parser object.
     * Term arg can be nil or atom option or list of options.
     * Options are:
     * <ul>
     * <li>simplify</li>
     * </ul>
     */
    public IParser createParser(Reader in, String inFname, Term arg, TermWareInstance instance) throws TermWareException {
        JJTJavaParser parser = new JJTJavaParser(in, this.paramsDomain);
        parser.setInFname(inFname);
        return new JavaParser(parser, arg, this);
    }

    public ASTTransformers getASTTransformers() {
        return astTransformers_;
    }

    private ASTTransformers astTransformers_ = new ASTTransformers();

}
