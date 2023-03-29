package pro.sky.noskoff.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface FilesServiceSocks {
    boolean cleanDataFileRecipes();

    boolean saveToDataFileSocks(String json);

    File getDataFileSocks();

    boolean uploadDataSocksFile(MultipartFile file);
}
