package pro.sky.noskoff.services;

import pro.sky.noskoff.model.Socks;
import pro.sky.noskoff.model.SocksColor;
import pro.sky.noskoff.model.SocksCottonPart;
import pro.sky.noskoff.model.SocksSize;

import java.util.List;

public interface SocksService {
    Socks addSocks(Socks socks);

    List<Socks> getAllSocks();


    Boolean deleteSocks(SocksColor color, SocksSize size, SocksCottonPart cotton, int quantity);

    String getCountSocksByParameters(SocksColor color, SocksSize size, SocksCottonPart cottonMin, SocksCottonPart cottonMax);
}