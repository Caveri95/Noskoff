package pro.sky.noskoff.services;

import org.springframework.web.multipart.MultipartFile;
import pro.sky.noskoff.model.SocksDTO.SocksDTO;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public interface FilesServiceSocks {

    boolean saveToDataFileSocks(List<SocksDTO> socksDTOS);

    String readFromDataFileSocks();

    File getDataFileSocks();

    boolean uploadDataSocksFile(MultipartFile file);
}
