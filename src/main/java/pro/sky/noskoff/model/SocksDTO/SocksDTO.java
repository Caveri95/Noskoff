package pro.sky.noskoff.model.SocksDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pro.sky.noskoff.model.Socks.SocksColor;
import pro.sky.noskoff.model.Socks.SocksCottonPart;
import pro.sky.noskoff.model.Socks.SocksSize;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocksDTO {
    private SocksColor socksColor;
    private SocksCottonPart socksCottonPart;
    private SocksSize socksSize;
    private long quantity;
}
