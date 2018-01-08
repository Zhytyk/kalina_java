package utils;

public enum BitLength {
    BIT_128(2, 128), BIT_256(4, 256), BIT_512(8, 512);

    private int collums;
    private int bits;

    BitLength(int collums, int bits) {
        this.collums = collums;
        this.bits = bits;
    }

    public int getCollums() {
        return collums;
    }

    public int getBits() {
        return bits;
    }

    public static BitLength parseString(String val) {
        int value = Integer.parseInt(val);

        switch (value) {
            case 128:
                return BIT_128;
            case 256:
                return BIT_256;
            case 512:
                return BIT_512;
        }

        throw new RuntimeException("Invalid value : " + value);
    }
}
