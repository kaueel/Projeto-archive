package com.company;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class FileHandler {
    private final String FILE_SEPARATOR = ";&EOF&;";
    private final String REGISTER_SEPARATOR = ";&EOR&;";

    public boolean fileExists(String path) {
        return new File(path).exists();
    }

    public String getFileName(String path) {
        return new File(path).getName();
    }

    public String readFile(String path) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(path), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    public String[] getPreProcessedFilesArray(String path) {
        return this.readFile(path).split(FILE_SEPARATOR);
    }

    public ArrayList<CsvFile> getPreProcessedRegisterArray(String[] preProcessedFilesArray) {
        ArrayList<CsvFile> listOfFiles = new ArrayList<>();
        for (String file : preProcessedFilesArray) {
            String[] fileRegisters = file.split(REGISTER_SEPARATOR);
            listOfFiles.add(new CsvFile(Integer.parseInt(fileRegisters[0]), fileRegisters[1], fileRegisters[2]));
        }
        return listOfFiles;
    }

    public ArrayList<CsvFile> getFiles(String path) {
        return this.getPreProcessedRegisterArray(
                this.getPreProcessedFilesArray(path));
    }

    public int countFilesInCompressedFile(String path) {
        return this
                .readFile(path)
                .split(Pattern.quote(FILE_SEPARATOR), -1)
                .length - 1;
    }

    public boolean writeFile(String fileCompressPath, String fileCompressedPath) {
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(fileCompressPath, true));
            printWriter.printf(
                    "%d" + REGISTER_SEPARATOR + "%s" + REGISTER_SEPARATOR + "%s" + FILE_SEPARATOR,
                    this.countFilesInCompressedFile(fileCompressPath) + 1,
                    this.getFileName(fileCompressedPath),
                    this.readFile(fileCompressedPath));
            printWriter.close();
            return !printWriter.checkError();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean writeFile(ArrayList<CsvFile> files, String toFile) {
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(toFile, true));
            files.forEach(file -> printWriter.printf(
                    "%d" + REGISTER_SEPARATOR + "%s" + REGISTER_SEPARATOR + "%s" + FILE_SEPARATOR,
                    file.getId(),
                    file.getFilename(),
                    file.getContent()));
            printWriter.close();
            return !printWriter.checkError();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean eraseFileContent(String fromFile) throws Exception {
        FileWriter FileWriter = new FileWriter(fromFile, false);
        PrintWriter printWriter = new PrintWriter(FileWriter, false);
        printWriter.flush();
        printWriter.close();
        FileWriter.close();
        return !printWriter.checkError();
    }

    public boolean removeFileFromArchive(int id, String fromFile) throws Exception {
        CsvFile[] files =
                this.getFiles(fromFile)
                        .stream()
                        .filter(file -> file.getId() != id)
                        .toArray(CsvFile[]::new);
        this.eraseFileContent(fromFile);
        return this.writeFile(new ArrayList<>(Arrays.asList(this.reorderIdFilesArray(files))), fromFile);
    }

    public CsvFile[] reorderIdFilesArray(CsvFile[] files){
        for (int i = 1; i <= files.length; i++)
            files[i-1].setId(i);

        return files;
    }

    public boolean writeFileLiteral(String stringToWrite, String toFile) {
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(toFile, true));
            printWriter.printf(stringToWrite);
            printWriter.close();
            return !printWriter.checkError();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean writeFileFromId(String fromFile, int idFromFile, String toFile) {
        try {
            CsvFile fileToWrite = this.getPreProcessedRegisterArray(this.getPreProcessedFilesArray(fromFile))
                    .stream()
                    .filter(file -> file.getId() == idFromFile)
                    .findFirst()
                    .get();

            return writeFileLiteral(fileToWrite.getContent(), this.returnValidFile(toFile).getPath());
        } catch (NoSuchElementException e) {
            System.out.println(fromFile);
            System.out.println(toFile);
            System.out.println(idFromFile);
            e.printStackTrace();
            return false;
        }
    }

    public File returnValidFile(String path) {
        try {
            File fileToReturn = new File(path);
            if (!this.fileExists(path))
                fileToReturn.createNewFile();
            return fileToReturn;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
