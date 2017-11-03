package com.company;

public class CsvFile {
    private int id;
    private String filename;
    private String content;

    CsvFile(int id, String filename, String content) {
        this.id = id;
        this.filename = filename;
        this.content = content;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
