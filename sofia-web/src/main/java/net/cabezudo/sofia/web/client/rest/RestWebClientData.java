package net.cabezudo.sofia.web.client.rest;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.users.rest.RestUser;

public record RestWebClientData(String language, Account account, RestUser user) {
}
