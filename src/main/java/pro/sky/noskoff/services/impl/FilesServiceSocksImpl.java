package pro.sky.noskoff.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.noskoff.model.SocksDTO.SocksDTO;
import pro.sky.noskoff.services.FilesServiceSocks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class FilesServiceSocksImpl implements FilesServiceSocks {

    @Value("${path.to.data.file}")
    private String dataFilePath;

    @Value("${name.of.data.file}")
    private String dataFileSocks;

    @Override
    public boolean saveToDataFileSocks(List<SocksDTO> socksDTOS) {
        try {
            cleanDataFileSocks();
            ObjectMapper objectMapper = new ObjectMapper();
            Files.write(Path.of(dataFilePath, dataFileSocks), objectMapper.writeValueAsBytes(socksDTOS)); // записываем нашу строку в файл
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean uploadDataSocksFile(MultipartFile file) {
        cleanDataFileSocks();
        File dataSocksFile = getDataFileSocks();
        try (

                FileOutputStream fos = new FileOutputStream(dataSocksFile)) {
            IOUtils.copy(file.getInputStream(), fos);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String readFromDataFileSocks() {
        try {
            return Files.readString(Path.of(dataFilePath, dataFileSocks)); // прочитать строку из файла
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void cleanDataFileSocks() {
        try {
            Path path = Path.of(dataFilePath, dataFileSocks);
            Files.deleteIfExists(path);  // удалить если существует
            Files.createFile(path); // создаем новый пустой файл
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public File getDataFileSocks() {
        return new File(dataFilePath + "/" + dataFileSocks);  // возвращаем файл (не сам файл, а его служебную информацию)
    }
}
