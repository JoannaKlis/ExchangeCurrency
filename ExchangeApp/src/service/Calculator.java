package service;

import model.ExchangeRate;
import model.ExchangeRequest;
import model.ExchangeResult;
import model.ExchangeTable;

public record Calculator(ExchangeTable exchangeTable) {
    private static final String PLN_CODE = "PLN";

    public ExchangeResult calculate(ExchangeRequest request) {
        double amount = request.amount();
        String fromCode = request.fromCurrency();
        String toCode = request.toCurrency();

        // pobierane kursów z uwzględnieniem przelicznika (effective rate)
        ExchangeRate fromRate = exchangeTable.getRate(fromCode);
        ExchangeRate toRate = exchangeTable.getRate(toCode);

        double amountPLN;
        double convertedAmount;

        // waluta źródłowa (PLN)
        if (fromCode.equals(PLN_CODE)) {
            amountPLN = amount;
            convertedAmount = convertFromPLN(amountPLN, toRate);
        }
        // waluta docelowa (PLN)
        else if (toCode.equals(PLN_CODE)) {
            amountPLN = convertToPLN(amount, fromRate);
            convertedAmount = amountPLN;
        }
        // konwersja innej waluty na inną walutę (X -> PLN -> Y)
        else {
            amountPLN = convertToPLN(amount, fromRate);
            convertedAmount = convertFromPLN(amountPLN, toRate);
        }

        // konwersja waluty1 do PLN i z PLN do waluty2.
        double effectiveUsedRate = fromRate.getEffectiveRate() / toRate.getEffectiveRate();

        return new ExchangeResult(amount, convertedAmount, fromCode, toCode, effectiveUsedRate);
    }

    // przeliczenie waluty1 na PLN
    private double convertToPLN(double amount, ExchangeRate rate) {
        return amount * rate.getEffectiveRate();
    }

    // przeliczenie PLN na walute2
    private double convertFromPLN(double amountPLN, ExchangeRate rate) {
        // jeśli waluta to PLN, rate.getEffectiveRate() to 1.0
        if (rate.getCode().equals(PLN_CODE)) {
            return amountPLN;
        }
        return amountPLN / rate.getEffectiveRate();
    }
}