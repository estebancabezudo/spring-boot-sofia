package net.cabezudo.sofia.math;

public class Numbers {
  public static final boolean isInteger(String s) {
    if (s == null) {
      return false;
    }
    return s.chars().allMatch(n -> Character.isDigit(n));
  }
}
