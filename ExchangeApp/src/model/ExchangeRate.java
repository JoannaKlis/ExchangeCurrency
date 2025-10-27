package model;

public record ExchangeRate(int multiplier, String currencyCode, double averageRate) {

    public String getCode() {
        return currencyCode;
    }

    // obliczenie kursu z uwzględnieniem przelicznika
    public double getEffectiveRate() {
        return averageRate / multiplier;
    }
}