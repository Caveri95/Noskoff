package pro.sky.noskoff.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import pro.sky.noskoff.model.Socks;
import pro.sky.noskoff.model.SocksColor;
import pro.sky.noskoff.model.SocksCottonPart;
import pro.sky.noskoff.model.SocksSize;
import pro.sky.noskoff.services.FilesServiceSocks;
import pro.sky.noskoff.services.SocksService;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SocksServiceImpl implements SocksService {
    private final FilesServiceSocks filesServiceSocks;
    private static Map<Socks, Long> socksStock = new HashMap<>();

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
    public Socks addSocks(Socks socks, long quantity) {
        if (socksStock.containsKey(socks)) {
            socksStock.computeIfPresent(socks, (socks1, aLong) -> aLong + quantity);
        }
        socksStock.putIfAbsent(socks, quantity);
        saveToFileSocks();
        return socks;
    }

    @Override
    public ArrayList<Map.Entry<Socks, Long>> getAllSocks() {
        return new ArrayList<>(socksStock.entrySet());

    }

    @Override
    public Boolean deleteSocks(Socks socks, long quantity) {
        if (socksStock.containsKey(socks) && socksStock.get(socks) >= quantity) {
            socksStock.computeIfPresent(socks, (socks1, aLong) -> aLong - quantity);
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
        try {
            Map<Long, Socks> socksStockReverse = socksStock.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
            String json = new ObjectMapper().writeValueAsString(socksStockReverse); // переводим из мапы в json объект
            filesServiceSocks.saveToDataFileSocks(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void readFromFileSocks() {
        String json = filesServiceSocks.readFromDataFileSocks(); // читаем из нашего файла все, что есть и получаем json строку
        try {
            HashMap<Long, Socks> socksStockReverse = new ObjectMapper().readValue(json, new TypeReference<HashMap<Long, Socks>>() {
            });// преобразуем строку в мапу
            socksStock = socksStockReverse.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
