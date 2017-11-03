package com.company;

import java.io.File;

class FileInputHandler extends FileHandler {
    private File compressedFile;

    FileInputHandler(String path) throws Exception {
        this.compressedFile = this.returnValidFile(path);
    }

    boolean compressFile(String path) throws Exception {
        if (this.fileExists(path))
            return this.writeFile(this.compressedFile.getPath(), path);
        else {
            System.out.println("Aviso: você tentou comprimir um arquivo inexistente.");
            return false;
        }
    }

    boolean removeFile(int id) throws Exception {
        return this.removeFileFromArchive(id, this.compressedFile.getPath());
    }
}
