package pro.sky.noskoff.model.Socks;

public enum SocksCottonPart {
    COTTON_10 (10), COTTON_30(30), COTTON_50(50), COTTON_70(70), COTTON_90(90);
    private final int text;

    SocksCottonPart(int text) {
        this.text = text;
    }

    public int getText() {
        return text;
    }
}
