package net.cabezudo.json;

import net.cabezudo.json.objects.Data;
import net.cabezudo.json.values.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 08/03/2016
 */
public class FormatterTest {

  private void createFile(String filename, String text) {
    URL url = this.getClass().getResource("");
    String fullFilename = url.getFile() + filename;
    File file = new File(fullFilename);
    Log.debug("Create file %s.%n", file.getAbsolutePath());
    try (PrintWriter writer = new PrintWriter(file, StandardCharsets.UTF_8)) {
      writer.println(text);
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testIndentTest() {
    Log.debug("Ident a unformated JSON string.");

    String expectedString = "{\n"
        + "   \"version\": {\n"
        + "      \"id\": 1,\n"
        + "      \"minor\": 0,\n"
        + "      \"version\": 1\n"
        + "   },\n"
        + "   \"countryId\": 1,\n"
        + "   \"countryName\": {\n"
        + "      \"version\": {\n"
        + "         \"id\": 1,\n"
        + "         \"minor\": 0,\n"
        + "         \"version\": 1\n"
        + "      },\n"
        + "      \"language\": {\n"
        + "         \"version\": {\n"
        + "            \"id\": 1,\n"
        + "            \"minor\": 0,\n"
        + "            \"version\": 1\n"
        + "         },\n"
        + "         \"charset\": \"utf8\",\n"
        + "         \"collation\": \"utf8_general_ci\",\n"
        + "         \"id\": 97,\n"
        + "         \"letterCode\": \"ps\"\n"
        + "      },\n"
        + "      \"nameType\": {\n"
        + "         \"index\": 1\n"
        + "      },\n"
        + "      \"word\": {\n"
        + "         \"version\": {\n"
        + "            \"id\": 1,\n"
        + "            \"minor\": 0,\n"
        + "            \"version\": 1\n"
        + "         },\n"
        + "         \"id\": 2,\n"
        + "         \"language\": {\n"
        + "            \"version\": {\n"
        + "               \"id\": 1,\n"
        + "               \"minor\": 0,\n"
        + "               \"version\": 1\n"
        + "            },\n"
        + "            \"charset\": \"utf8\",\n"
        + "            \"collation\": \"utf8_general_ci\",\n"
        + "            \"id\": 97,\n"
        + "            \"letterCode\": \"ps\"\n"
        + "         },\n"
        + "         \"string\": \"افغانستان\"\n"
        + "      }\n"
        + "   }\n"
        + "}\n";

    Data data = new Data();
    JSONObject jsonTree = JSON.toJSONObject(data);
    String jsonString = jsonTree.toJSON();

    String indentString = Formatter.indent(jsonString);

    createFile("jsonString.txt", jsonString);
    createFile("expectedString.txt", expectedString);
    createFile("indentString.txt", indentString);

    Log.debug("jsonString: %s.%n", jsonString);
    Log.debug("expectedString: %s.%n", expectedString);
    Log.debug("indentString: %s.%n", indentString);

    assertEquals(expectedString, indentString);
  }
}
