package net.cabezudo.sofia.math;

public class Numbers {
  public static boolean isInteger(String s) {
    if (s == null) {
      return false;
    }
    return s.chars().allMatch(Character::isDigit);
  }
}
