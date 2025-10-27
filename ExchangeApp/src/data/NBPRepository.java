package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class NBPRepository implements IRepository {

    @Override
    public String fetch(String path) throws Exception {
        return fetchFromFile(path);
    }

    private String fetchFromFile(String filePath) throws Exception {
        System.out.println("Ładowanie danych z pliku: " + filePath);
        File file = new File(filePath);

        if (!file.exists()) {
            throw new Exception("Plik nie istnieje pod ścieżką: " + filePath);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
            return content.toString();
        } catch (Exception e) {
            System.err.println("Wystąpił błąd podczas odczytu pliku: " + filePath);
            throw e;
        }
    }
}