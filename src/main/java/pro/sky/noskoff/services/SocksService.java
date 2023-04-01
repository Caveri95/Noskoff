package pro.sky.noskoff.services;

import org.springframework.web.multipart.MultipartFile;
import pro.sky.noskoff.model.Socks.Socks;
import pro.sky.noskoff.model.Socks.SocksColor;
import pro.sky.noskoff.model.Socks.SocksCottonPart;
import pro.sky.noskoff.model.Socks.SocksSize;
import pro.sky.noskoff.model.SocksDTO.SocksDTO;

import java.util.ArrayList;
import java.util.Map;

public interface SocksService {
    Socks addSocks(SocksDTO socksDTO);

    ArrayList<Map.Entry<Socks, Long>> getAllSocks();

    Boolean deleteSocks(SocksDTO socksDTO);

    String getCountSocksByParameters(SocksColor color, SocksSize size, SocksCottonPart cottonMin, SocksCottonPart cottonMax);

}
