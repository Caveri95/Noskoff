package pro.sky.noskoff.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import pro.sky.noskoff.model.Socks;
import pro.sky.noskoff.model.SocksColor;
import pro.sky.noskoff.model.SocksCottonPart;
import pro.sky.noskoff.model.SocksSize;
import pro.sky.noskoff.services.FilesServiceSocks;
import pro.sky.noskoff.services.SocksService;

import java.util.*;

@Service
public class SocksServiceImpl implements SocksService {
    private final FilesServiceSocks filesServiceSocks;

    public static long SocksQuantity = 0L;
    private static Map<Long, Socks> socksStock = new LinkedHashMap<>();

    public SocksServiceImpl(FilesServiceSocks filesServiceSocks) {
        this.filesServiceSocks = filesServiceSocks;
    }

    @Override
    public Socks addSocks(Socks socks) {
        for (int i = 0; i < socks.getQuantity(); i++) {
            socksStock.putIfAbsent(SocksQuantity++, socks);
        }
        saveToFileRecipes();
        return socks;
    }

    @Override
    public List<Socks> getAllSocks() {
        return new ArrayList<>(socksStock.values());
    }

    @Override
    public Boolean deleteSocks(SocksColor color, SocksSize size, SocksCottonPart cotton, int quantity) {
        if (getCountSocks(color, size, cotton) < quantity) {
            return false;
        }
        int a = 1;
        Iterator<Map.Entry<Long, Socks>> i = socksStock.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry<Long, Socks> socks = i.next();
            if (socks.getValue().getSocksColor() == color &&
                    socks.getValue().getSocksSize() == size &&
                    socks.getValue().getSocksCottonPart() == cotton) {
                i.remove();
                if (a++ == quantity) {
                    break;
                }
            }
        }
        saveToFileRecipes();
        return true;
    }

    @Override
    public String getCountSocksByParameters(SocksColor color, SocksSize size, SocksCottonPart cottonMin, SocksCottonPart cottonMax) {
        int a = 0;
        for (Map.Entry<Long, Socks> socks : socksStock.entrySet()) {
            if (socks.getValue().getSocksColor() == color &&
                    socks.getValue().getSocksSize() == size &&
                    socks.getValue().getSocksCottonPart().getText() >= cottonMin.getText() &&
                    socks.getValue().getSocksCottonPart().getText() <= cottonMax.getText()) {
                a++;

            }
        }
        return Integer.toString(a);
    }

    private int getCountSocks(SocksColor color, SocksSize size, SocksCottonPart cottonPart) {
        int a = 0;
        for (Map.Entry<Long, Socks> socks : socksStock.entrySet()) {
            if (socks.getValue().getSocksColor() == color &&
                    socks.getValue().getSocksSize() == size &&
                    socks.getValue().getSocksCottonPart() == cottonPart) {
                a++;
            }
        }
        return a;
    }

    private void saveToFileRecipes() {
        try {
            String json = new ObjectMapper().writeValueAsString(socksStock);
            filesServiceSocks.saveToDataFileSocks(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
