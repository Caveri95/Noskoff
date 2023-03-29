package pro.sky.noskoff.services.impl;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.noskoff.services.FilesServiceSocks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FilesServiceSocksImpl implements FilesServiceSocks {
    @Value("${path.to.data.file}")
    private String dataFilePath;

    @Value("${name.of.data.file}")
    private String dataFileSocks;

    @Override
    public boolean cleanDataFileRecipes() {
        try {
            Path path = Path.of(dataFilePath, dataFileSocks);
            Files.deleteIfExists(path);
            Files.createFile(path);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean saveToDataFileSocks(String json) {
        try {
            cleanDataFileRecipes();
            Files.writeString(Path.of(dataFilePath, dataFileSocks), json);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public File getDataFileSocks() {
        return new File(dataFilePath + "/" + dataFileSocks);
    }

    @Override
    public boolean uploadDataSocksFile(MultipartFile file) {
        cleanDataFileRecipes();
        File dataRecipeFile = getDataFileSocks();

        try (
                FileOutputStream fos = new FileOutputStream(dataRecipeFile)) {
            IOUtils.copy(file.getInputStream(), fos);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
