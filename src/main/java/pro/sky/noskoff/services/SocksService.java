package pro.sky.noskoff.services;

import pro.sky.noskoff.model.Socks;
import pro.sky.noskoff.model.SocksColor;
import pro.sky.noskoff.model.SocksCottonPart;
import pro.sky.noskoff.model.SocksSize;

import java.util.ArrayList;
import java.util.Map;

public interface SocksService {
    Socks addSocks(Socks socks, long quantity);

    ArrayList<Map.Entry<Socks, Long>> getAllSocks();

    Boolean deleteSocks(Socks socks, long quantity);

    String getCountSocksByParameters(SocksColor color, SocksSize size, SocksCottonPart cottonMin, SocksCottonPart cottonMax);
}
