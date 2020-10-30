package me.donghun.tobyspringvol1.user.sqlService;

public interface SqlService {
    String getSql(String key) throws SqlRetrievalFailureException;
}