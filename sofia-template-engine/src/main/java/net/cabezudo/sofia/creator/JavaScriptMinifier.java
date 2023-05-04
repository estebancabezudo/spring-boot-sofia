package net.cabezudo.sofia.creator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.09.26
 */
public class JavaScriptMinifier {

    private final Logger log = LoggerFactory.getLogger(SofiaFile.class);

    public String get(String code) throws IOException {
        log.debug("JavaScript minifier");
        // TODO Minify code
        return code;
    }
}
