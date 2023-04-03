package pro.sky.noskoff.model.Socks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Socks {

    private SocksColor socksColor;
    private SocksCottonPart socksCottonPart;
    private SocksSize socksSize;
}
