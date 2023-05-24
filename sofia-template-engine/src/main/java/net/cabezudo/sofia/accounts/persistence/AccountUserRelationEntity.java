package net.cabezudo.sofia.accounts.persistence;

public record AccountUserRelationEntity(int id, int accountId, int userId, boolean owner) {
}
