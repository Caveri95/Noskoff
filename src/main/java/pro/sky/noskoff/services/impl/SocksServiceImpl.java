package pro.sky.noskoff.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import pro.sky.noskoff.model.Socks.Socks;
import pro.sky.noskoff.model.Socks.SocksColor;
import pro.sky.noskoff.model.Socks.SocksCottonPart;
import pro.sky.noskoff.model.Socks.SocksSize;
import pro.sky.noskoff.model.SocksDTO.SocksDTO;
import pro.sky.noskoff.services.FilesServiceSocks;
import pro.sky.noskoff.services.SocksService;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class SocksServiceImpl implements SocksService {
    private final FilesServiceSocks filesServiceSocks;
    private static final Map<Socks, Long> socksStock = new HashMap<>();

    public SocksServiceImpl(FilesServiceSocks filesServiceSocks) {
        this.filesServiceSocks = filesServiceSocks;
    }

    @PostConstruct
    private void init() {
        File file = filesServiceSocks.getDataFileSocks();
        if (file.exists()) {
            readFromFileSocks();
        }
    }

    @Override
    public Socks addSocks(SocksDTO socksDTO) {
        Socks socks = mapToSock(socksDTO);
        if (socksStock.containsKey(socks)) {
            socksStock.computeIfPresent(socks, (socks1, aLong) -> aLong + socksDTO.getQuantity());
        }
        socksStock.putIfAbsent(socks, socksDTO.getQuantity());
        saveToFileSocks();
        return socks;
    }

    @Override
    public ArrayList<Map.Entry<Socks, Long>> getAllSocks() {
        return new ArrayList<>(socksStock.entrySet());
    }

    @Override
    public Boolean deleteSocks(SocksDTO socksDTO) {
        Socks socks = mapToSock(socksDTO);
        if (socksStock.containsKey(socks) && socksStock.get(socks) >= socksDTO.getQuantity()) {
            socksStock.computeIfPresent(socks, (socks1, aLong) -> aLong - socksDTO.getQuantity());
            return true;
        }
        saveToFileSocks();
        return false;
    }


    @Override
    public String getCountSocksByParameters(SocksColor color, SocksSize size, SocksCottonPart cottonMin, SocksCottonPart cottonMax) {
        long a = 0;
        for (Map.Entry<Socks, Long> socks : socksStock.entrySet()) {
            if (socks.getKey().getSocksColor() == color &&
                    socks.getKey().getSocksSize() == size &&
                    socks.getKey().getSocksCottonPart().getText() >= cottonMin.getText() &&
                    socks.getKey().getSocksCottonPart().getText() <= cottonMax.getText()) {
                a += socks.getValue();
            }
        }
        return Long.toString(a);
    }

    private void saveToFileSocks() {
        List<SocksDTO> socksList = new ArrayList<>();
        for (Map.Entry<Socks, Long> entry : socksStock.entrySet()) {
            socksList.add(mapToDTO(entry.getKey(), entry.getValue()));
        }
        filesServiceSocks.saveToDataFileSocks(socksList);
    }

    private void readFromFileSocks() {
        try {
            ArrayList<SocksDTO> importList = new ObjectMapper().readValue(filesServiceSocks.readFromDataFileSocks(), new TypeReference<>() {
            });
            for (SocksDTO dto : importList) {
                addSocks(dto);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Socks mapToSock(SocksDTO socksDTO) {
        return new Socks(socksDTO.getSocksColor(), socksDTO.getSocksCottonPart(), socksDTO.getSocksSize());
    }

    private SocksDTO mapToDTO(Socks socks, long quantity) {
        SocksDTO socksDTO = new SocksDTO();
        socksDTO.setSocksColor(socks.getSocksColor());
        socksDTO.setSocksSize(socks.getSocksSize());
        socksDTO.setSocksCottonPart(socks.getSocksCottonPart());
        socksDTO.setQuantity(quantity);
        return socksDTO;
    }
}
