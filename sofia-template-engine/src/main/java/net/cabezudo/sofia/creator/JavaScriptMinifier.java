package net.cabezudo.sofia.creator;

import com.google.javascript.jscomp.CommandLineRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.09.26
 */
public class JavaScriptMinifier {

    private final Logger log = LoggerFactory.getLogger(SofiaFile.class);

    public String get(String code) throws IOException {
        Path temporalFile = Files.createTempFile("", ".temp");
        log.debug("Temporal file created: " + temporalFile);
        // TODO Create a temporal file with the code
        String[] arguments = {"--language_out", "ES5", "--js", "input1.js", "input2.js", "--js_output_file", "output.min.js"};

        CommandLineRunner.main(arguments);
        String minifiedCode = null; // TODO read code from the file created
        log.debug("Code generated: " + minifiedCode);
        return minifiedCode;
    }
}
