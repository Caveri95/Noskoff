package pro.sky.noskoff.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface FilesServiceSocks {

    boolean saveToDataFileSocks(String json);

    String readFromDataFileSocks();

    File getDataFileSocks();

    boolean uploadDataSocksFile(MultipartFile file);
}
