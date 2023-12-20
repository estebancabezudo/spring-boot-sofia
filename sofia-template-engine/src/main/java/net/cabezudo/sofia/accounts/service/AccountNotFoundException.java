package net.cabezudo.sofia.accounts.service;

public class AccountNotFoundException extends Exception {
  private final String accountName;

  public AccountNotFoundException(String accountName) {
    this.accountName = accountName;
  }
}
