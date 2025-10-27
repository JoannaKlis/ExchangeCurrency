package model;

public record ExchangeResult(double originalAmount, double convertedAmount, String fromCurrency, String toCurrency,
                             double usedRate) {

    public String getFormattedResult() {
        return String.format("%.2f %s = %.2f %s (Kurs: %.6f)",
                originalAmount, fromCurrency, convertedAmount, toCurrency, usedRate);
    }
}