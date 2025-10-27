package model;

public record ExchangeRequest(double amount, String fromCurrency, String toCurrency) {
    public ExchangeRequest(double amount, String fromCurrency, String toCurrency) {
        this.amount = amount;
        this.fromCurrency = fromCurrency.toUpperCase();
        this.toCurrency = toCurrency.toUpperCase();
    }

    @Override
    public String toString() {
        return String.format("%.2f %s na %s", amount, fromCurrency, toCurrency);
    }
}