package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Menu {
    private String compressedPath = null;
    private FileInputHandler fileInputHandler = null;
    private FileOutputHandler fileOutputHandler = null;

    public void init() throws Exception {
        String response = "";

        if (this.initializeComponents())
            while (!response.equalsIgnoreCase("exit")) {
                response = this.showOptions(
                        "Olá, escolha a opção desejada :",
                        new ArrayList<String>(
                                Arrays.asList(
                                        "1 - Comprimir arquivos",
                                        "2 - Gerar arquivos",
                                        "3 - Remover arquivos",
                                        "4 - Ver arquivos",
                                        "",
                                        "exit - Sair e fechar o programa"
                                ))
                );

                switch (response) {
                    case "1":
                        this.compressFiles();
                        break;
                    case "2":
                        this.outputFiles();
                        break;
                    case "3":
                        this.removeElement();
                        break;
                    case "4":
                        this.viewFiles();
                        break;
                    default:
                        break;
                }
            }
    }

    private void compressFiles() throws Exception {
        String response = "";

        while (!response.equalsIgnoreCase("exit")) {
            response = this.makeQuestion("Digite o nome dos arquivos separados por um enter, " +
                    "quando não tiver mais arquivos pra serem compactados, por favor, digite exit");

            if (!response.isEmpty() && !response.equalsIgnoreCase("exit"))
                this.fileInputHandler.compressFile(response);
        }
    }

    private void outputFiles() throws Exception {
        String response = "yes";
        String id = "";
        String pathToRecreate = "";

        while (response.equalsIgnoreCase("yes")) {
            this.showFiles(new FileHandler().getFiles(this.compressedPath));
            id = this.makeQuestion("Escolha um id para recriar (se quiser cancelar, digite 'no')");
            if (!id.equalsIgnoreCase("no")) {
                pathToRecreate = this.makeQuestion("Escolha o lugar para recriar o arquivo (se quiser cancelar, digite 'no')");
                if (!pathToRecreate.equalsIgnoreCase("no")) {
                    if (this.isNumeric(id) && !pathToRecreate.isEmpty())
                        this.fileOutputHandler.generateFile(Integer.parseInt(id), pathToRecreate);

                    response = this.makeQuestion("Deseja comprimir outro arquivo? yes/no");
                }
            }
        }
    }

    private void removeElement() throws Exception {
        String response = "";

        while (!response.equalsIgnoreCase("exit")) {
            this.showFiles(new FileHandler().getFiles(this.compressedPath));
            response = this.makeQuestion("Digite o id do arquivo que deseja remover do archive ou exit para voltar");

            if (!response.isEmpty() && this.isNumeric(response))
                this.fileInputHandler.removeFile(Integer.parseInt(response));
        }
    }

    private void viewFiles() throws Exception {
        this.showFiles(new FileHandler().getFiles(this.compressedPath));
    }

    final boolean isNumeric(String possibleNumericString) {
        return possibleNumericString.matches("\\d+");
    }

    private boolean initializeComponents() throws Exception {
        this.compressedPath = makeQuestion("Por favor, nos diga onde vai ficar o arquivo comprimido");
        if (!compressedPath.isEmpty() && new FileHandler().returnValidFile(compressedPath).exists()) {
            fileInputHandler = new FileInputHandler(compressedPath);
            fileOutputHandler = new FileOutputHandler(compressedPath);
            return true;
        } else
            return false;
    }

    private String makeQuestion(String question) {
        System.out.println(question);
        return this.receiveAnswer();
    }

    private String receiveAnswer() {
        return new Scanner(System.in).next();
    }

    private void showList(String title, ArrayList<String> listOfString) {
        System.out.println(title);
        listOfString.forEach(System.out::println);
    }

    private String showOptions(String title, ArrayList<String> listOfOptions) {
        this.showList(title, listOfOptions);
        return this.receiveAnswer();
    }

    public void showFiles(ArrayList<CsvFile> files) {
        files.forEach(file -> {
            System.out.println(file.getId() + " - " + file.getFilename() + ":");
            System.out.println("    " + file.getContent());
        });
    }
}
