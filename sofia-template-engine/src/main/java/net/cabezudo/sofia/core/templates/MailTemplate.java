package net.cabezudo.sofia.core.templates;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MailTemplate {
  private static final Logger log = LoggerFactory.getLogger(MailTemplate.class);
  private static final Pattern PATTERN = Pattern.compile("\\$\\{([^}]+)}");
  private final String subject;
  private final String body;

  public MailTemplate(Builder builder) {
    subject = builder.subject;
    body = builder.body.toString();
  }

  public String getSubject() {
    return subject;
  }

  public String getBody() {
    return body;
  }

  public static class Builder {
    private final Map<String, String> map = new TreeMap<>();
    private final StringBuilder body = new StringBuilder();
    private String subject;
    private File templateFileName;

    public MailTemplate.Builder set(String key, String value) {
      map.put(key, value);
      return this;
    }

    public MailTemplate build() throws IOException {
      log.debug("Loading template " + templateFileName);
      try (BufferedReader reader = new BufferedReader(new FileReader(templateFileName))) {
        int lineNumber = 0;
        while (true) {
          lineNumber++;
          String line = reader.readLine();
          if (line == null) {
            break;
          }
          if (line.isBlank()) {
            readBody(templateFileName, lineNumber, reader);
          }
          if (line.startsWith("Subject: ")) {
            String processedLine = processLine(templateFileName, lineNumber, line.substring(9));
            subject = processedLine;
          }
        }
      }
      return new MailTemplate(this);
    }

    public Builder set(File templateFileName) {
      this.templateFileName = templateFileName;
      return this;
    }

    private void readBody(File file, int lineNumber, BufferedReader reader) throws IOException {
      while (true) {
        lineNumber++;
        String line = reader.readLine();
        if (line == null) {
          break;
        }
        String processedLine = processLine(file, lineNumber, line);
        body.append(processedLine);
      }
    }

    private String processLine(File file, int lineNumber, String line) {
      Matcher matcher = PATTERN.matcher(line);
      StringBuffer output = new StringBuffer();
      while (matcher.find()) {
        String propertyName = matcher.group(1);
        String propertyValue = map.get(propertyName);
        if (propertyValue == null) {
          log.warn("I can't find the property value for " + propertyName + " in " + file + (lineNumber == 0 ? "" : ":" + lineNumber));
        } else {
          matcher.appendReplacement(output, Matcher.quoteReplacement(propertyValue));
        }
      }
      matcher.appendTail(output);
      String out = output.toString();
      return out;
    }
  }
}
