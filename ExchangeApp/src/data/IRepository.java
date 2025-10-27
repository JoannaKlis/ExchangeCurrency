package data;

import model.ExchangeTable;

public interface IRepository {
    String fetch(String url) throws Exception;
}