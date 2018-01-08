package utils;

import java.math.BigInteger;
import java.util.Random;

public class RandomGenerator {
    private byte[][] rand;

    private BitLength length;
    private byte[][] data;

    private byte[][] Kl;
    private byte[][] Kw;
    private byte[][] b;
    private byte[][] buff;

    public byte[][] getData() {
        return data;
    }

    public RandomGenerator() {

    }

    public RandomGenerator(BitLength length) {
        this.length = length;
        this.data = new byte[Constants.COUNT_ROWS][length.getCollums()];
    }

    public byte[][] random() {
        Random random = new Random();

        for (int j = 0; j < length.getCollums(); j++) {
            for (int i = 0; i < Constants.COUNT_ROWS; i++) {
                {
                    this.data[i][j] = (byte) random.nextInt();
                }
            }
        }
        return this.data;
    }

    public void rozbuv(int keyl, int blokl) {
        if (keyl == blokl) {
            Kw = Kl = data;
            return;
        }

        int cols = length.getCollums();
        Kl = new byte[Constants.COUNT_ROWS][cols / 2];
        Kw = new byte[Constants.COUNT_ROWS][cols / 2];
        for (int j = 0; j < cols; j++) {
            for (int i = 0; i < 8; i++) {
                if (j < cols / 2) {
                    Kl[i][j] = this.data[i][j];
                } else {
                    Kw[i][j - (cols / 2)] = this.data[i][j];
                }
            }
        }

    }

    public void firstSum(BitLength keyl, BitLength blokl) {
        int keylBits = keyl.getBits();
        int bloklBits = blokl.getBits();
        
        buff = null;
        if (keylBits == 128 && bloklBits == 128) {
            buff = Constants._128_128;
        } else if (keylBits == 256 && bloklBits == 128) {
            buff = Constants._128_256;
            length = BitLength.BIT_128;
        } else if (keylBits == 256 && bloklBits == 256) {
            buff = Constants._256_256;
        } else if (keylBits == 512 && bloklBits == 256) {
            buff = Constants._256_512;
            length = BitLength.BIT_256;
        } else if (keylBits == 512 && bloklBits == 512) {
            buff = Constants._512_512;
        }

        if (buff == null) {
            throw new RuntimeException("Buff is null!");
        }


        int cols = length.getCollums();
        b = new byte[Constants.COUNT_ROWS][cols];
        for (int i = 0; i < Constants.COUNT_ROWS; i++) {
            for (int j = 0; j < cols; j++) {
                b[i][j] = (byte) (((int) Kl[i][j] + (int) buff[i][j]) % 255);
                if (b[i][j] == -1) {
                    b[i][j] = 0;
                }
            }
        }
        this.data = b;
    }

    public void s_boxes() {
        for (int i = 0; i < Constants.COUNT_ROWS; i++) {
            int start = 0;
            byte[][] sbox = new byte[Constants.TWO_BYTE][Constants.TWO_BYTE];

            for (int n = 0; n < 16; n++) {
                for (int m = 0; m < 16; m++) {
                    if (start <= Constants.sbox_value[i].length() - 2) {
                        sbox[m][n] = (byte) Integer.parseInt(Constants.sbox_value[i].substring(start, start + 2), 16);
                        start = start + 2;
                    }
                }
            }

            for (int j = 0; j < length.getCollums(); j++) {
                int x = (this.data[i][j] & 0xFF) >> 4;
                int y = (this.data[i][j] & 0xFF) & 15;
                this.data[i][j] = sbox[x][y];
            }

        }
    }

    private void simple_512_shift(int row, int num) {
        for (int i = 0; i < num; i++) {
            byte buffer = data[row][7];
            data[row][7] = data[row][6];
            data[row][6] = data[row][5];
            data[row][5] = data[row][4];
            data[row][4] = data[row][3];
            data[row][3] = data[row][2];
            data[row][2] = data[row][1];
            data[row][1] = data[row][0];
            data[row][0] = buffer;
        }
    }

    private void simple_256_shift(int row, int num) {
        for (int j = row; j < row + 2; j++) {
            for (int i = 0; i < num; i++) {
                byte buffer = data[j][3];
                data[j][3] = data[j][2];
                data[j][2] = data[j][1];
                data[j][1] = data[j][0];
                data[j][0] = buffer;
            }
        }
    }

    private void simple_128_shift() {
        for (int i = 4; i < Constants.COUNT_ROWS; i++) {
            byte buffer = data[i][1];
            data[i][1] = data[i][0];
            data[i][0] = buffer;
        }
    }

    public void shift_Rows() {
        byte buffer;

        switch (length) {
            case BIT_128:
                simple_128_shift();
                break;
            case BIT_256:
                for (int i = 1; i <= 3; i++) {
                    simple_256_shift(2 * i, i);
                }
                break;
            case BIT_512:
                for (int i = 0; i < Constants.COUNT_ROWS; i++) {
                    simple_512_shift(i, i);
                }
        }
    }



    public void mix_Columns() {
        for (int j = 0; j < length.getCollums(); j++) {
            // temp
            byte a = this.data[0][j];
            byte b = this.data[1][j];
            byte c = this.data[2][j];
            byte d = this.data[3][j];
            byte e = this.data[4][j];
            byte f = this.data[5][j];
            byte g = this.data[6][j];
            byte h = this.data[7][j];
            
            this.data[0][j] = (byte)(Utils.gMul( (byte) 0x01, a) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, b) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x05, c) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, d) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x08, e) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x06, f) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x07, g) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x04, h) & 0xFF);
            this.data[1][j] = (byte)(Utils.gMul( (byte) 0x04, a) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, b) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, c) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x05, d) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, e) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x08, f) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x06, g) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x07, h) & 0xFF);
            this.data[2][j] = (byte)(Utils.gMul( (byte) 0x07, a) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x04, b) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, c) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, d) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x05, e) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, f) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x08, g) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x06, h) & 0xFF);
            this.data[3][j] = (byte)(Utils.gMul( (byte) 0x06, a) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x07, b) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x04, c) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, d) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, e) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x05, f) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, g) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x08, h) & 0xFF);
            this.data[4][j] = (byte)(Utils.gMul( (byte) 0x08, a) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x06, b) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x07, c) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x04, d) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, e) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, f) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x05, g) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, h) & 0xFF);
            this.data[5][j] = (byte)(Utils.gMul( (byte) 0x01, a) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x08, b) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x06, c) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x07, d) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x04, e) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, f) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, g) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x05, h) & 0xFF);
            this.data[6][j] = (byte)(Utils.gMul( (byte) 0x05, a) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, b) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x08, c) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x06, d) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x07, e) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x04, f) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, g) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, h) & 0xFF);
            this.data[7][j] = (byte)(Utils.gMul( (byte) 0x01, a) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x05, b) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, c) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x08, d) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x06, e) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x07, f) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x04, g) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, h) & 0xFF);
        }
    }

    public void inv_MixColumns() {
        for (int j = 0; j < length.getCollums(); j++) {
            byte a = data[0][j];
            byte b = data[1][j];
            byte c = data[2][j];
            byte d = data[3][j];
            byte e = data[4][j];
            byte f = data[5][j];
            byte g = data[6][j];
            byte h = data[7][j];

            this.data[0][j] = (byte)(Utils.gMul( (byte) (byte) 0xad, a) & 0xFF ^ Utils.gMul( (byte) (byte) 0x95, b) & 0xFF ^ Utils.gMul( (byte) (byte) 0x76, c) & 0xFF ^ Utils.gMul( (byte) (byte) 0xa8, d) & 0xFF ^ Utils.gMul( (byte) (byte) 0x2f, e) & 0xFF ^ Utils.gMul( (byte) (byte) 0x49, f) & 0xFF ^ Utils.gMul( (byte) (byte) 0xd7, g) & 0xFF ^ Utils.gMul( (byte) (byte) 0xca, h));
            this.data[1][j] = (byte)(Utils.gMul( (byte) (byte) 0xca, a) & 0xFF ^ Utils.gMul( (byte) (byte) 0xad, b) & 0xFF ^ Utils.gMul( (byte) (byte) 0x95, c) & 0xFF ^ Utils.gMul( (byte) (byte) 0x76, d) & 0xFF ^ Utils.gMul( (byte) (byte) 0xa8, e) & 0xFF ^ Utils.gMul( (byte) (byte) 0x2f, f) & 0xFF ^ Utils.gMul( (byte) (byte) 0x49, g) & 0xFF ^ Utils.gMul( (byte) (byte) 0xd7, h));
            this.data[2][j] = (byte)(Utils.gMul( (byte) (byte) 0xd7, a) & 0xFF ^ Utils.gMul( (byte) (byte) 0xca, b) & 0xFF ^ Utils.gMul( (byte) (byte) 0xad, c) & 0xFF ^ Utils.gMul( (byte) (byte) 0x95, d) & 0xFF ^ Utils.gMul( (byte) (byte) 0x76, e) & 0xFF ^ Utils.gMul( (byte) (byte) 0xa8, f) & 0xFF ^ Utils.gMul( (byte) (byte) 0x2f, g) & 0xFF ^ Utils.gMul( (byte) (byte) 0x49, h));
            this.data[3][j] = (byte)(Utils.gMul( (byte) (byte) 0x49, a) & 0xFF ^ Utils.gMul( (byte) (byte) 0xd7, b) & 0xFF ^ Utils.gMul( (byte) (byte) 0xca, c) & 0xFF ^ Utils.gMul( (byte) (byte) 0xad, d) & 0xFF ^ Utils.gMul( (byte) (byte) 0x95, e) & 0xFF ^ Utils.gMul( (byte) (byte) 0x76, f) & 0xFF ^ Utils.gMul( (byte) (byte) 0xa8, g) & 0xFF ^ Utils.gMul( (byte) (byte) 0x2f, h));
            this.data[4][j] = (byte)(Utils.gMul( (byte) (byte) 0x2f, a) & 0xFF ^ Utils.gMul( (byte) (byte) 0x49, b) & 0xFF ^ Utils.gMul( (byte) (byte) 0xd7, c) & 0xFF ^ Utils.gMul( (byte) (byte) 0xca, d) & 0xFF ^ Utils.gMul( (byte) (byte) 0xad, e) & 0xFF ^ Utils.gMul( (byte) (byte) 0x95, f) & 0xFF ^ Utils.gMul( (byte) (byte) 0x76, g) & 0xFF ^ Utils.gMul( (byte) (byte) 0xa8, h));
            this.data[5][j] = (byte)(Utils.gMul( (byte) (byte) 0xa8, a) & 0xFF ^ Utils.gMul( (byte) (byte) 0x2f, b) & 0xFF ^ Utils.gMul( (byte) (byte) 0x49, c) & 0xFF ^ Utils.gMul( (byte) (byte) 0xd7, d) & 0xFF ^ Utils.gMul( (byte) (byte) 0xca, e) & 0xFF ^ Utils.gMul( (byte) (byte) 0xad, f) & 0xFF ^ Utils.gMul( (byte) (byte) 0x95, g) & 0xFF ^ Utils.gMul( (byte) (byte) 0x76, h));
            this.data[6][j] = (byte)(Utils.gMul( (byte) (byte) 0x76, a) & 0xFF ^ Utils.gMul( (byte) (byte) 0xa8, b) & 0xFF ^ Utils.gMul( (byte) (byte) 0x2f, c) & 0xFF ^ Utils.gMul( (byte) (byte) 0x49, d) & 0xFF ^ Utils.gMul( (byte) (byte) 0xd7, e) & 0xFF ^ Utils.gMul( (byte) (byte) 0xca, f) & 0xFF ^ Utils.gMul( (byte) (byte) 0xad, g) & 0xFF ^ Utils.gMul( (byte) (byte) 0x95, h));
            this.data[7][j] = (byte)(Utils.gMul( (byte) (byte) 0x95, a) & 0xFF ^ Utils.gMul( (byte) (byte) 0x76, b) & 0xFF ^ Utils.gMul( (byte) (byte) 0xa8, c) & 0xFF ^ Utils.gMul( (byte) (byte) 0x2f, d) & 0xFF ^ Utils.gMul( (byte) (byte) 0x49, e) & 0xFF ^ Utils.gMul( (byte) (byte) 0xd7, f) & 0xFF ^ Utils.gMul( (byte) (byte) 0xca, g) & 0xFF ^ Utils.gMul( (byte) (byte) 0xad, h));
        }
    }


    public void xor() {
        for (int j = 0; j < length.getCollums(); j++) {
            for (int i = 0; i < Constants.COUNT_ROWS; i++) {
                this.data[i][j] = (byte) ((this.data[i][j] & 0xFF) & 0xFF ^ (Kw[i][j] & 0xFF));
            }
        }
    }

    public void change() {
        int cols = length.getCollums();

        byte[][] result = new byte[Constants.COUNT_ROWS][cols];

        for (int j = 0; j < cols; j++) {
            int over = 0;
            for (int i = 0; i < Constants.COUNT_ROWS; i++) {
                int sum = (this.data[i][j] & 0xFF) + (Kl[i][j] & 0xFF) + over;
                if (sum > 255) {
                    over = 1;
                    sum = sum - 256;
                }
                else {
                    over = 0;
                }
                result[i][j] = (byte) sum;
            }
        }
        this.data = result;
    }

    public void add_hex(String datastr) {
        int start = 0;

        for (int j = 0; j < length.getCollums(); j++) {
            for (int i = 0; i < 8; i++) {
                if (start <= datastr.length() - 2) {
                    this.data[i][j] = (byte) Integer.parseInt(datastr.substring(start, start + 2), 16);
                    start = start + 2;
                }
            }
        }
    }

    public byte[][] toHex(String datastr, int var_key_length) {
        BitLength keyDt = BitLength.parseString(Integer.toString(var_key_length));
        int cols = keyDt.getCollums();

        NumHex nh = new NumHex();
        int start = 0;
        byte[][] b = new byte[8][cols];
        for (int j = 0; j < cols; j++) {
            for (int i = 0; i < 8; i++) {
                b[i][j] = (byte) nh.convertInt(datastr.substring(start, start + 2));
                start = start + 2;
            }
        }

        return b;
    }

    public byte[][] zsuvv(byte[][] rand, int zsuv, int con, int cols2)
    {
        byte[] rightShift = new byte[8 * cols2];
        byte[][] rightShiftFinal = new byte[8][cols2];

        byte[] buf = new byte[8 * cols2];
        int c_buf = 0;

        for (int j = 0; j < cols2; j++) {
            for (int i = 0; i < 8; i++) {
                buf[c_buf] = rand[i][j];
                c_buf++;
            }
        }

        int shiftCount = 0;
        for (byte[] i : rand) {
            for (byte b : i) {
                rightShift[(shiftCount + ((zsuv / 8) * con) * 7) % (8 * cols2)] = buf[shiftCount];
                shiftCount++;
            }
        }

        shiftCount = 0;
        for (int j = 0; j < cols2; j++) {
            for (int i = 0; i < 8; i++) {
                rightShiftFinal[i][j] = rightShift[shiftCount];
                shiftCount++;
            }
        }

        return rightShiftFinal;
    }

    public void add_text(byte[] massive) {
        int index = 0;

        for (int j = 0; j < length.getCollums(); j++) {
            for (int i = 0; i < 8; i++) {
                this.data[i][j] = massive[index];
                index++;
            }
        }
    }

    public String[] key_forming(int blokl, int keyl) {
        if (keyl == 128 && blokl == 128) {
            return Constants.TMV_128_128;
        } else if (keyl == 256 && blokl == 128 || keyl == 128 && blokl == 256) {
            return Constants.TMV_128_256;
        } else if (keyl == 256 && blokl == 256) {
            return Constants.TMV_256_256;
        } else if (keyl == 512 && blokl == 256 || keyl == 256 && blokl == 512) {
            return Constants.TMV_256_512;
        } else if (keyl == 512 && blokl == 512) {
            return Constants.TMV_512_512;
        } else {
            return Constants.TMV_128_256;
        }
    }

    public byte[][] addNumbers(byte[][] number1, byte[][] number2, int cols) {
        byte[][] result = new byte[8][cols];

        for (int j = 0; j < cols; j++) {
            NumHex nh = new NumHex();
            String s = "";
            String v = "";
            for (int i = 7; i >= 0; i--) {
                String _1 = "0000000" + Integer.toString(number1[i][j] & 0xFF, 2);
                s += _1.substring(_1.length() - 8, _1.length());
                String _2 = "0000000" + Integer.toString(number2[i][j] & 0xFF, 2);
                v += _2.substring(_2.length() - 8, _2.length());
            }

            BigInteger n1 = new BigInteger(s, 2);
            BigInteger n2 = new BigInteger(v, 2);

            BigInteger nn = n1.add(n2);

            String s1 = nn.toString(2);

            s = "000000000000000000000000000000000000000000000000000000000000000" + s1;
            s = s.substring(s.length() - 64, s.length());

            int co = 0;
            for (int i = 7; i >= 0; i--) {
                result[i][j] = (byte) Integer.parseInt(s.substring(co, co + 8), 2);
                co += 8;
            }
        }

        return result;
    }

    public byte[][] subNumbers(byte[][] number1, byte[][] number2, int cols) {
        byte[][] result = new byte[8][cols];

        for (int j = 0; j < cols; j++) {
            NumHex nh = new NumHex();
            String s = "";
            String v = "";
            for (int i = 7; i >= 0; i--) {
                String _1 = "0000000" + Integer.toString(number1[i][j] & 0xFF, 2);
                s += _1.substring(_1.length() - 8, _1.length());
                String _2 = "0000000" +  Integer.toString(number2[i][j] & 0xFF, 2);
                v += _2.substring(_2.length() - 8, _2.length());
            }

            long n1 = new BigInteger(s, 2).longValue();
            long n2 = new BigInteger(v, 2).longValue();

            long nn = n1 - n2;

            String s1 = Long.toBinaryString(nn);

            if (s1.startsWith("-")) {
                s1 = s1.substring(1, s1.length() - 1);
            }

            s = "000000000000000000000000000000000000000000000000000000000000000" + s1;
            s = s.substring(s.length() - 64, s.length());

            int co = 0;
            for (int i = 7; i >= 0; i--) {
                result[i][j] = (byte)Integer.parseUnsignedInt(s.substring(co, co + 8), 2);
                co += 8;
            }
        }

        return result;
    }

    public byte[][] sBoxes(byte[][] kt_raund, int cols1) {

        for (int i = 0; i < 8; i++) {
            int start = 0;
            byte[][] sbox = new byte[16][16];

            for (int n = 0; n < 16; n++) {
                for (int m = 0; m < 16; m++) {
                    if (start <= Constants.sbox_value[i].length() - 2) {
                        sbox[m][n] = (byte) Integer.parseInt(Constants.sbox_value[i].substring(start, start + 2), 16);
                        start += 2;
                    }
                }
            }

            for (int j = 0; j < cols1; j++) {
                int x = (kt_raund[i][j] & 0xFF) >> 4;
                int y = (kt_raund[i][j] & 0xFF) & 15;
                kt_raund[i][j] = sbox[x][y];
            }
        }

        return kt_raund;
    }

    public byte[][] S_Rows(byte[][] s_box1, int cols1) {
        byte buffer;
        if (cols1 == 2) {
            for (int i = 4; i < 8; i++) {
                buffer = s_box1[i][1];
                s_box1[i][1] = s_box1[i][0];
                s_box1[i][0] = buffer;
            }
        }

        if (cols1 == 4) {
            s_box1 = simple_256_shift1(s_box1, 2, 1);
            s_box1 = simple_256_shift1(s_box1, 4, 2);
            s_box1 = simple_256_shift1(s_box1, 6, 3);
        }

        if (cols1 == 8) {
            for (int i = 0; i < 8; i++) {
                s_box1 = simple_512_shift1(s_box1, i, i);
            }
        }

        return s_box1;
    }

    private byte[][] simple_512_shift1(byte[][] s_box1, int row, int num)
    {
        for (int iter = 0; iter < num; iter++) {
            byte buffer = s_box1[row][7];
            s_box1[row][7] = s_box1[row][6];
            s_box1[row][6] = s_box1[row][5];
            s_box1[row][5] = s_box1[row][4];
            s_box1[row][4] = s_box1[row][3];
            s_box1[row][3] = s_box1[row][2];
            s_box1[row][2] = s_box1[row][1];
            s_box1[row][1] = s_box1[row][0];
            s_box1[row][0] = buffer;
        }

        return s_box1;
    }

    private byte[][] simple_256_shift1(byte[][] s_box1, int row, int num) {
        for (int j = row; j < row + 2; j++) {
            for (int i = 0; i < num; i++) {
                byte buffer = s_box1[j][3];
                s_box1[j][3] = s_box1[j][2];
                s_box1[j][2] = s_box1[j][1];
                s_box1[j][1] = s_box1[j][0];
                s_box1[j][0] = buffer;
            }
        }
        return s_box1;
    }

    public byte[][] M_Columns(byte[][] s_row, int cols1) {
        for (int j = 0; j < cols1; j++) {

            byte a = s_row[0][j];
            byte b = s_row[1][j];
            byte c = s_row[2][j];
            byte d = s_row[3][j];
            byte e = s_row[4][j];
            byte f = s_row[5][j];
            byte g = s_row[6][j];
            byte h = s_row[7][j];
            
            s_row[0][j] = (byte)(Utils.gMul( (byte) 0x01, a) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, b) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x05, c) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, d) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x08, e) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x06, f) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x07, g) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x04, h) & 0xFF);
            s_row[1][j] = (byte)(Utils.gMul( (byte) 0x04, a) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, b) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, c) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x05, d) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, e) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x08, f) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x06, g) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x07, h) & 0xFF);
            s_row[2][j] = (byte)(Utils.gMul( (byte) 0x07, a) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x04, b) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, c) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, d) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x05, e) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, f) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x08, g) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x06, h) & 0xFF);
            s_row[3][j] = (byte)(Utils.gMul( (byte) 0x06, a) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x07, b) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x04, c) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, d) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, e) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x05, f) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, g) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x08, h) & 0xFF);
            s_row[4][j] = (byte)(Utils.gMul( (byte) 0x08, a) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x06, b) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x07, c) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x04, d) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, e) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, f) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x05, g) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, h) & 0xFF);
            s_row[5][j] = (byte)(Utils.gMul( (byte) 0x01, a) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x08, b) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x06, c) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x07, d) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x04, e) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, f) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, g) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x05, h) & 0xFF);
            s_row[6][j] = (byte)(Utils.gMul( (byte) 0x05, a) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, b) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x08, c) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x06, d) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x07, e) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x04, f) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, g) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, h) & 0xFF);
            s_row[7][j] = (byte)(Utils.gMul( (byte) 0x01, a) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x05, b) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, c) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x08, d) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x06, e) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x07, f) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x04, g) & 0xFF & 0xFF ^ Utils.gMul( (byte) 0x01, h) & 0xFF);
        }

        return s_row;
    }

    public byte[][] xor_rkey(byte[][] num1, byte[][] num2, int cols1) {
        byte[][] result = new byte[8][cols1];

        for (int j = 0; j < cols1; j++) {
            for (int i = 0; i < 8; i++) {
                result[i][j] = (byte) ((num1[i][j] & 0xFF) ^ (num2[i][j] & 0xFF));
            }
        }

        return result;
    }

    public byte[][] addNumbers1(byte[][] number1, byte[][] number2, byte[][] number3, int cols)
    {
        byte[] buf1 = new byte[number1.length];
        byte[] buf2 = new byte[number2.length];
        byte[] buf4 = new byte[number2.length];
        byte[] buf3 = new byte[number2.length];
        int buf;
        byte[][] result = new byte[8][cols];
        int c_buf = 0;

        for (int j = 0; j < cols; j++) {
            int over = 0;
            for (int i = 7; i >= 0; i--) {
                buf = number1[i][j] + number2[i][j] + over;
                if (buf > 255) {
                    over = 1;
                    buf = buf - 256;
                } else {
                    over = 0;
                }
                result[i][j] = (byte) buf;
            }
        }

        return result;
    }

    public byte[][] zsuvvL(byte[][] rand, int zsuv, int con, int cols2)
    {
        byte[] leftShift = new byte[8 * cols2];
        byte[][] leftShiftFinal = new byte[8][cols2];

        byte[] buf = new byte[8 * cols2];
        int c_buf = 0;
        for (int j = 0; j < cols2; j++) {
            for (int i = 0; i < 8; i++) {
                buf[c_buf] = rand[i][j];
                c_buf++;
            }
        }

        int shiftCount = 0;
        for (byte[] i : rand) {
            for (byte b : i) {
                int a = (shiftCount - ((zsuv / 8) * con));
                if (a < 0) {
                    a = a + (8 * cols2);
                }

                leftShift[a % (8 * cols2)] = buf[shiftCount];
                shiftCount++;
            }
        }

        shiftCount = 0;
        for (int j = 0; j < cols2; j++) {
            for (int i = 0; i < 8; i++) {
                leftShiftFinal[i][j] = leftShift[shiftCount];
                shiftCount++;
            }
        }

        return leftShiftFinal;
    }

    public byte[][] inv_MColumns(byte[][] n1, int cols1) {
        for (int j = 0; j < cols1; j++) {
            byte a = n1[0][j];
            byte b = n1[1][j];
            byte c = n1[2][j];
            byte d = n1[3][j];
            byte e = n1[4][j];
            byte f = n1[5][j];
            byte g = n1[6][j];
            byte h = n1[7][j];

            n1[0][j] = (byte)(Utils.gMul( (byte) 0xad, a) & 0xFF ^ Utils.gMul( (byte) 0x95, b) & 0xFF ^ Utils.gMul( (byte) 0x76, c) & 0xFF ^ Utils.gMul( (byte) 0xa8, d) & 0xFF ^ Utils.gMul( (byte) 0x2f, e) & 0xFF ^ Utils.gMul( (byte) 0x49, f) & 0xFF ^ Utils.gMul( (byte) 0xd7, g) & 0xFF ^ Utils.gMul( (byte) 0xca, h) & 0xFF);
            n1[1][j] = (byte)(Utils.gMul( (byte) 0xca, a) & 0xFF ^ Utils.gMul( (byte) 0xad, b) & 0xFF ^ Utils.gMul( (byte) 0x95, c) & 0xFF ^ Utils.gMul( (byte) 0x76, d) & 0xFF ^ Utils.gMul( (byte) 0xa8, e) & 0xFF ^ Utils.gMul( (byte) 0x2f, f) & 0xFF ^ Utils.gMul( (byte) 0x49, g) & 0xFF ^ Utils.gMul( (byte) 0xd7, h) & 0xFF);
            n1[2][j] = (byte)(Utils.gMul( (byte) 0xd7, a) & 0xFF ^ Utils.gMul( (byte) 0xca, b) & 0xFF ^ Utils.gMul( (byte) 0xad, c) & 0xFF ^ Utils.gMul( (byte) 0x95, d) & 0xFF ^ Utils.gMul( (byte) 0x76, e) & 0xFF ^ Utils.gMul( (byte) 0xa8, f) & 0xFF ^ Utils.gMul( (byte) 0x2f, g) & 0xFF ^ Utils.gMul( (byte) 0x49, h) & 0xFF);
            n1[3][j] = (byte)(Utils.gMul( (byte) 0x49, a) & 0xFF ^ Utils.gMul( (byte) 0xd7, b) & 0xFF ^ Utils.gMul( (byte) 0xca, c) & 0xFF ^ Utils.gMul( (byte) 0xad, d) & 0xFF ^ Utils.gMul( (byte) 0x95, e) & 0xFF ^ Utils.gMul( (byte) 0x76, f) & 0xFF ^ Utils.gMul( (byte) 0xa8, g) & 0xFF ^ Utils.gMul( (byte) 0x2f, h) & 0xFF);
            n1[4][j] = (byte)(Utils.gMul( (byte) 0x2f, a) & 0xFF ^ Utils.gMul( (byte) 0x49, b) & 0xFF ^ Utils.gMul( (byte) 0xd7, c) & 0xFF ^ Utils.gMul( (byte) 0xca, d) & 0xFF ^ Utils.gMul( (byte) 0xad, e) & 0xFF ^ Utils.gMul( (byte) 0x95, f) & 0xFF ^ Utils.gMul( (byte) 0x76, g) & 0xFF ^ Utils.gMul( (byte) 0xa8, h) & 0xFF);
            n1[5][j] = (byte)(Utils.gMul( (byte) 0xa8, a) & 0xFF ^ Utils.gMul( (byte) 0x2f, b) & 0xFF ^ Utils.gMul( (byte) 0x49, c) & 0xFF ^ Utils.gMul( (byte) 0xd7, d) & 0xFF ^ Utils.gMul( (byte) 0xca, e) & 0xFF ^ Utils.gMul( (byte) 0xad, f) & 0xFF ^ Utils.gMul( (byte) 0x95, g) & 0xFF ^ Utils.gMul( (byte) 0x76, h) & 0xFF);
            n1[6][j] = (byte)(Utils.gMul( (byte) 0x76, a) & 0xFF ^ Utils.gMul( (byte) 0xa8, b) & 0xFF ^ Utils.gMul( (byte) 0x2f, c) & 0xFF ^ Utils.gMul( (byte) 0x49, d) & 0xFF ^ Utils.gMul( (byte) 0xd7, e) & 0xFF ^ Utils.gMul( (byte) 0xca, f) & 0xFF ^ Utils.gMul( (byte) 0xad, g) & 0xFF ^ Utils.gMul( (byte) 0x95, h) & 0xFF);
            n1[7][j] = (byte)(Utils.gMul( (byte) 0x95, a) & 0xFF ^ Utils.gMul( (byte) 0x76, b) & 0xFF ^ Utils.gMul( (byte) 0xa8, c) & 0xFF ^ Utils.gMul( (byte) 0x2f, d) & 0xFF ^ Utils.gMul( (byte) 0x49, e) & 0xFF ^ Utils.gMul( (byte) 0xd7, f) & 0xFF ^ Utils.gMul( (byte) 0xca, g) & 0xFF ^ Utils.gMul( (byte) 0xad, h) & 0xFF);
        }
        return n1;
    }

    public byte[][] inv_S_Rows(byte[][] n1, int cols1) {
        byte buffer;

        if (cols1 == 2) {
            for (int i = 4; i < 8; i++) {
                buffer = n1[i][1];
                n1[i][1] = n1[i][0];
                n1[i][0] = buffer;
            }
        }

        if (cols1 == 4) {
            n1 = simple_256_shift1(n1, 2, 3);
            n1 = simple_256_shift1(n1, 4, 2);
            n1 = simple_256_shift1(n1, 6, 1);
        }

        if (cols1 == 8) {
            for (int i = 1; i < 8; i++) {
                n1 = simple_512_shift1(n1, i, 8 - i);
            }
        }

        return n1;
    }

    public byte[][] inv_Sboxes(byte[][] n1, int cols1)
    {

        for (int i = 0; i < 8; i++) {
            int start = 0;
            byte[][] invsbox = new byte[16][16];
            for (int n = 0; n < 16; n++) {
                for (int m = 0; m < 16; m++) {
                    if (start <= Constants.inv_sbox_value[i].length() - 2) {
                        invsbox[m][n] = (byte) Integer.parseInt(Constants.inv_sbox_value[i].substring(start, start + 2), 16);
                        start = start + 2;
                    }
                }
            }

            for (int j = 0; j < cols1; j++) {
                int x = (n1[i][j] & 0xFF) >> 4;
                int y = (n1[i][j] & 0xFF) & 15;
                n1[i][j] = invsbox[x][y];
            }
        }
        return n1;

    }

    public byte[][] getBuff() {
        return buff;
    }

    public void setBuff(byte[][] buff) {
        this.buff = buff;
    }
}
