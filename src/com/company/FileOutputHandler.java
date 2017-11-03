package com.company;

import java.io.File;
import java.io.IOException;

class FileOutputHandler extends FileHandler {
    private File compressedFile;

    FileOutputHandler(String path) throws Exception {
        this.compressedFile = this.returnValidFile(path);
    }

    public boolean generateFile(int index, String pathToGeneratedFile) throws IOException {
        File generatedFile = this.returnValidFile(pathToGeneratedFile);
        if (generatedFile.canWrite())
            this.writeFileFromId(this.compressedFile.getPath(), index, pathToGeneratedFile);
        return true;
    }
}
