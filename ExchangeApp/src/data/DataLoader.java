package data;

import model.ExchangeTable;

public record DataLoader(IRepository repository, IParser parser) {
    public ExchangeTable loadRates() {
        try {
            //fetch ścieżki z katalogu resources i parsowanie danych
            String xmlData = repository.fetch("resources/LastA.xml");
            return parser.parse(xmlData);
        } catch (Exception e) {
            System.err.println("Bład: " + e.getMessage());
            return null;
        }
    }
}