package net.cabezudo.sofia.utils;

import java.util.Arrays;

public class StringUtils {
  public static String toString(String... strings) {

    String out = Arrays.stream(strings)
        .collect(StringBuilder::new,
            (sb, str) -> sb.append(str).append(", "),
            StringBuilder::append)
        .toString();

    if (!out.isEmpty()) {
      out = out.substring(0, out.length() - 2);
    }
    return "[" + out + "]";
  }
}
