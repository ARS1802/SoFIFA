package tools;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Document {
    private final BufferedReader reader;

    public Document(String path) throws IOException {
        // Instanciacao de Stream
        FileInputStream fileInputStream = new FileInputStream(path);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

        // Tratamento de I/O
        this.reader = new BufferedReader(inputStreamReader);
    }

    public String[] readLine() throws IOException {
        String line = reader.readLine();

        if (line == null) {
            return null;
        }

        List<String> columns = new ArrayList<>();
        StringBuilder currentColumn = new StringBuilder();
        boolean insideQuotes = false;

        // Parser de CSV
        for (int i = 0; i < line.length(); i++) {
            char currentChar = line.charAt(i);

            if (currentChar == '"') {
                insideQuotes = !insideQuotes;
                continue;
            }

            if (currentChar == ',' && !insideQuotes) {
                columns.add(currentColumn.toString());
                currentColumn.setLength(0);
                continue;
            }

            currentColumn.append(currentChar);
        }

        columns.add(currentColumn.toString());

        return columns.toArray(new String[0]);
    }
}
