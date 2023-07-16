package com.ryderbelserion.crazycrates.core.frame.storage;

import java.io.File;

public interface FileManager {

    void addFile(FileExtension fileExtension);

    void saveFile(FileExtension fileExtension);

    void removeFile(FileExtension fileExtension);

    File getFile(FileExtension fileExtension);

}