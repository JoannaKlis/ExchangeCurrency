package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExchangeTable {
    // mapuje kod waluty na kurs
    private final Map<String, ExchangeRate> rates = new ConcurrentHashMap<>();

    public ExchangeTable() {
        // PLN jako wirtualny kurs, gdzie 1 PLN = 1 PLN
        rates.put("PLN", new ExchangeRate(1, "PLN", 1.0));
    }


    public void addRate(ExchangeRate rate) {
        rates.put(rate.getCode(), rate);
    }

    public ExchangeRate getRate(String code) {
        return rates.get(code.toUpperCase());
    }

    public List<String> getCurrencies() {
        List<String> codes = new ArrayList<>(rates.keySet());
        Collections.sort(codes);
        return codes;
    }

    public boolean hasRate(String code) {
        return !rates.containsKey(code.toUpperCase());
    }
}