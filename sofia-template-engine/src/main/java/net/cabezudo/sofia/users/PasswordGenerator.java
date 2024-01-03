package net.cabezudo.sofia.users;


import java.security.SecureRandom;

public class PasswordGenerator {
  private static final String LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
  private static final String UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final String NUMBERS = "0123456789";
  private static final String SYMBOLS = "!@#$%&*()_-+=[]{}<>?";

  public static String generate(int length, boolean includeUppercase, boolean includeNumbers, boolean includeSymbols) {
    StringBuilder password = new StringBuilder();
    StringBuilder characterToUse = new StringBuilder(LOWERCASE_LETTERS);

    if (includeUppercase) {
      characterToUse.append(UPPERCASE_LETTERS);
    }
    if (includeNumbers) {
      characterToUse.append(NUMBERS);
    }
    if (includeSymbols) {
      characterToUse.append(SYMBOLS);
    }

    SecureRandom random = new SecureRandom();
    for (int i = 0; i < length; i++) {
      int randomIndex = random.nextInt(characterToUse.length());
      char randomChar = characterToUse.charAt(randomIndex);
      password.append(randomChar);
    }
    return password.toString();
  }
}

