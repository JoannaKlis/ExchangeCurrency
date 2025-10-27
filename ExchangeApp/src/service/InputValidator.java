package service;

import model.ExchangeRequest;
import model.ExchangeTable;
import model.ValidationResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record InputValidator(ExchangeTable exchangeTable) {
    // prawidłowy format: Kwota (liczba), Kod_FROM (3 znaki), Kod_TO (3 znaki); np. 100 USD EUR
    private static final String VALID_INPUT_PATTERN = "^(\\d+(\\.\\d+)?)\\s+([a-zA-Z]{3})\\s+([a-zA-Z]{3})$";

    public ValidationResult validate(String input) {
        String trimmedInput = input.trim();

        if (trimmedInput.isEmpty()) {
            return new ValidationResult(false, "Nic nie wpisano! Spróbuj ponownie.");
        }

        Pattern pattern = Pattern.compile(VALID_INPUT_PATTERN);
        Matcher matcher = pattern.matcher(trimmedInput);

        if (!matcher.matches()) {
            return new ValidationResult(false, "Nieprawidłowe dane!\nOczekiwano: KWOTA (bez przecinka) KOD_WALUTY_Z KOD_WALUTY_DO (np. 100.51 USD EUR)");
        }

        // walidacja kodów walut
        String fromCode = matcher.group(3).toUpperCase();
        String toCode = matcher.group(4).toUpperCase();

        if (fromCode.equals(toCode)) {
            return new ValidationResult(false, "Kody walut muszą być różne!");
        }

        if (exchangeTable.hasRate(fromCode)) {
            return new ValidationResult(false, "Nieznany kod waluty źródłowej: " + fromCode);
        }

        if (exchangeTable.hasRate(toCode)) {
            return new ValidationResult(false, "Nieznany kod waluty docelowej: " + toCode);
        }

        return new ValidationResult(true, "Walidacja pomyślna.");
    }

    public ExchangeRequest parseInput(String input) {
        // sprawdzenie czy wprowadzony kod pasuje do wzorca
        if (!validate(input).isValid()) {
            throw new IllegalArgumentException("Próba parsowania niewalidacyjnego inputu.");
        }

        // wyodrębnienie danych z inputu
        Pattern pattern = Pattern.compile(VALID_INPUT_PATTERN);
        Matcher matcher = pattern.matcher(input.trim());

        // dopasowanie danych wyodrębnionych z VALID_INPUT_PATTERN
        if (matcher.matches()) {
            double amount = Double.parseDouble(matcher.group(1));
            String fromCurrency = matcher.group(3);
            String toCurrency = matcher.group(4);

            return new ExchangeRequest(amount, fromCurrency, toCurrency);
        }
        return null;
    }

    public boolean isExitCommand(String input) {
        return "0".equalsIgnoreCase(input.trim()) || "exit".equalsIgnoreCase(input.trim());
    }

    public boolean isMenuCommand(String input) {
        return "menu".equalsIgnoreCase(input.trim());
    }
}