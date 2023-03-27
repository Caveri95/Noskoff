package pro.sky.noskoff.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Socks {

    private SocksColor socksColor;
    private SocksCottonPart socksCottonPart;
    private SocksSize socksSize;
    private int quantity;
}
