package app;

import data.DataLoader;
import data.IParser;
import data.IRepository;
import data.NBPRepository;
import data.XMLParser;
import model.ExchangeRequest;
import model.ExchangeResult;
import model.ExchangeTable;
import model.ValidationResult;
import service.Calculator;
import service.InputValidator;

import java.util.List;
import java.util.Scanner;

public class ExchangeApp {
    private static ExchangeApp instance;

    private final DataLoader dataLoader;
    private InputValidator validator;
    private Calculator calculator;
    private ExchangeTable exchangeTable;

    private ExchangeApp() {
        IRepository repository = new NBPRepository();
        IParser parser = new XMLParser();
        this.dataLoader = new DataLoader(repository, parser);
    }

    public static ExchangeApp getInstance() {
        if (instance == null) {
            instance = new ExchangeApp();
        }
        return instance;
    }

    public void run() {
        System.out.println("--- System Przewalutowania NBP ---");
        exchangeTable = dataLoader.loadRates();

        if (exchangeTable == null) {
            System.err.println("Aplikacja została zamknięta z powodu braku danych.");
            return;
        }

        this.validator = new InputValidator(exchangeTable);
        this.calculator = new Calculator(exchangeTable);

        System.out.println(exchangeTable);
        showMenu();

        Scanner scanner = new Scanner(System.in);
        String input;
        boolean running = true;

        while (running) {
            System.out.print("\n1. Aby przewalutować pieniądze wpisz: KWOTA (bez przecinka) KOD_WALUTY_Z KOD_WALUTY_DO (np. 100.51 USD EUR)" +
                    "\n2. Wpisz 'menu', aby wyświetlić kody walut" +
                    "\n3. Wpisz 0 lub 'exit', aby zakończyć: ");
            input = scanner.nextLine();

            if (validator.isExitCommand(input)) {
                handleExit();
                running = false;
            } else if (validator.isMenuCommand(input)) {
                showMenu();
            } else {
                processInput(input);
            }
        }
        scanner.close();
    }

    private void showMenu() {
        System.out.println("\n--- KODY WALUT ---");
        List<String> codes = exchangeTable.getCurrencies();
        System.out.println(String.join("\n", codes));
    }

    private void processInput(String input) {
        ValidationResult validation = validator.validate(input);

        if (!validation.isValid()) {
            System.err.println("Błąd: " + validation.errorMessage());
            return;
        }

        try {
            ExchangeRequest request = validator.parseInput(input);
            assert request != null;
            ExchangeResult result = calculator.calculate(request);

            System.out.println("\nWynik: " + result.getFormattedResult());
        } catch (Exception e) {
            System.err.println("Błąd przetwarzania: " + e.getMessage());
        }
    }

    private void handleExit() {
        System.out.println("\nAplikacja została zamknięta!");
    }
}