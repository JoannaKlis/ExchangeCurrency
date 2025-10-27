package data;

import model.ExchangeTable;

public interface IParser {
    ExchangeTable parse(String xmlData) throws Exception;
}