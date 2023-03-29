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
    public boolean saveToDataFileSocks(String json) {
        try {
            cleanDataFileSocks();
            Files.writeString(Path.of(dataFilePath, dataFileSocks), json); // записываем нашу строку в файл
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    @Override
    public String readFromDataFileSocks() {
        try {
            return Files.readString(Path.of(dataFilePath, dataFileSocks)); // прочитать строку из файла
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean cleanDataFileSocks() {
        try {
            Path path = Path.of(dataFilePath, dataFileSocks);
            Files.deleteIfExists(path);  // удалить если существует
            Files.createFile(path); // создаем новый пустой файл
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public File getDataFileSocks() {
        return new File(dataFilePath + "/" + dataFileSocks);  // возвращаем файл (не сам файл, а его служебную информацию)
    }

    @Override
    public boolean uploadDataSocksFile(MultipartFile file) {
        cleanDataFileSocks();
        File dataSocksFile = getDataFileSocks();
        try (
                FileOutputStream fos = new FileOutputStream(dataSocksFile)) {
            IOUtils.copy(file.getInputStream(), fos);  // берем входящий поток из параметров запроса и копируем в выходящий поток
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
