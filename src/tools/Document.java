package tools;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
//===========================
/*
    reader: lê arquivo dado por setInputFile(path)
    writer: escreve em arquivo dado por setOutputFile(path)
    nextReaderLine: guarda próxima linha, usado em casos de HasNextLine()
    inputFile: arquivo usado pelo reader
    outputFile: arquivo usado pelo writer
*/
//===========================
public class Document {
    private BufferedReader reader;
    private BufferedWriter writer;
    private String nextReaderLine;
    private File inputFile;
    private File outputFile;

    public Document(String path) throws IOException {
        setInputFile(path);
    }

    public void setInputFile(String path) throws IOException {
        this.inputFile = new File(path);
        this.nextReaderLine = null;

        // Instanciacao de Stream
        FileInputStream fileInputStream = new FileInputStream(this.inputFile);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

        // Tratamento de I/O
        this.reader = new BufferedReader(inputStreamReader);
    }

    public void setOutputFile(String path) throws IOException {
        File outputFile;
        String safePath = path == null || path.isBlank() ? "output" : path;
        File chosenPath = new File(safePath);

        if (!chosenPath.getName().toLowerCase().endsWith(".csv")) {
            chosenPath.mkdirs();
            String fileName = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".csv";

            outputFile = new File(chosenPath, fileName);
        }
        else {
            outputFile = chosenPath;
            File parentDirectory = outputFile.getParentFile();

            if(parentDirectory != null){
                parentDirectory.mkdirs();
            }
        }

        outputFile.createNewFile();
        this.outputFile = outputFile;

        // Instanciacao de Stream
        FileOutputStream fileOutputStream = new FileOutputStream(this.outputFile, true);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);

        // Tratamento de I/O
        this.writer = new BufferedWriter(outputStreamWriter);
    }

    public boolean readerHasNextLine() throws IOException {
        if (nextReaderLine != null) {
            return true;
        }

        String line;

        // Leitura antecipada
        while ((line = reader.readLine()) != null) {
            if (!line.isBlank()) {
                nextReaderLine = line;
                return true;
            }
        }

        return false;
    }

    public boolean writerHasNextLine() throws IOException {
        writer.flush();

        // Leitura do arquivo de saída cria um reader temp
        try (BufferedReader outputReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(outputFile), StandardCharsets.UTF_8)
        )) {
            String line;

            while ((line = outputReader.readLine()) != null) {
                if (!line.isBlank()) {
                    return true;
                }
            }
        }

        return false;
    }

    public String[] readLine() throws IOException {
        String line = nextReaderLine;
        nextReaderLine = null;

        if (line == null) {
            line = reader.readLine();
        }

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

    public void writeLine(Object data) throws IOException {
        // Escrita append
        writer.write(data.toString());
        writer.newLine();
        writer.flush();
    }
}
