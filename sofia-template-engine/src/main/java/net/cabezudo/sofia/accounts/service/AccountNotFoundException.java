package net.cabezudo.sofia.accounts.service;

public class AccountNotFoundException extends InvalidAccountException {
  public AccountNotFoundException(String accountName) {
    super(accountName);
  }
}
