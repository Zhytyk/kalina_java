package controllers;

import app.Main;
import com.sun.jndi.cosnaming.CNCtx;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import utils.*;

import javax.rmi.CORBA.Util;
import java.io.IOException;
import java.util.*;

public class MainController extends BaseController {
    private BitLength sizeKeyDt;
    private BitLength sizeBlockDt;
    private byte[][] rand;
    private byte[][] kt;
    private String[] Rkeys;
    private RandomGenerator key;

    private List<Map<String, Map<String, String>>> encryptVisualKeys;
    private List<Map<String, Map<String, String>>> decryptVisualKeys;
    private Map<String, String> additionalVisualKey;
    private List<Map<String, String>> evenVisualKeys;
    private List<Map<String, String>> oddVisualKeys;

    public MainController() {
        encryptVisualKeys = new LinkedList<>();
        decryptVisualKeys = new LinkedList<>();
        additionalVisualKey = new LinkedHashMap<>();
        evenVisualKeys = new LinkedList<>();
        oddVisualKeys = new LinkedList<>();
    }

    @FXML
    protected void generate(ActionEvent event) {
        String valOfSizeBlockData = ((RadioButton) sizeBlockData.getSelectedToggle()).getText();
        String valOfSizeKeyData = ((RadioButton) sizeKey.getSelectedToggle()).getText();
        sizeBlockDt = BitLength.parseString(valOfSizeBlockData);
        sizeKeyDt = BitLength.parseString(valOfSizeKeyData);

        RandomGenerator randomGenerator = new RandomGenerator(sizeKeyDt);
        byte[][] random = randomGenerator.random();

        StringBuilder randomSB = new StringBuilder();
        for (byte[] valArr : random) {
            for (byte val : valArr) {
                randomSB.append(String.format("%x", Byte.toUnsignedInt(val)));
            }
        }

        keyTField.setText(randomSB.toString());
    }

    @FXML
    protected void accept(ActionEvent event) {
        if (keyTField.getText().equals(Constants.EMPTY)) {
            generate(null);
        }

        acceptedTField.clear();
        addKeySubTab.setDisable(false);
        additionalVisualKey.clear();

        String valOfSizeBlockData = ((RadioButton) sizeBlockData.getSelectedToggle()).getText();
        String valOfSizeKeyData = ((RadioButton) sizeKey.getSelectedToggle()).getText();
        sizeBlockDt = BitLength.parseString(valOfSizeBlockData);
        sizeKeyDt = BitLength.parseString(valOfSizeKeyData);

        key = new RandomGenerator(sizeKeyDt);

        int dataKeyLength = sizeKeyDt.getBits();
        int dataBlockLength = sizeBlockDt.getBits();

        additionalVisualKey.put("K(a) = K(w)", keyTField.getText());

        String formatDataStr = ((RadioButton) formatData.getSelectedToggle()).getText();

        if (formatDataStr.equals(Constants.HEX)) {
            key.add_hex(Utils.padding(keyTField.getText(), dataKeyLength, formatDataStr));
        }
        if (formatDataStr.equals(Constants.ASCII)) {
            key.add_text(Utils.padding(keyTField.getText(), dataKeyLength,  formatDataStr).getBytes());
        }

        key.rozbuv(dataKeyLength, dataBlockLength);
        rand = key.getData();
        key.firstSum(sizeKeyDt, sizeBlockDt); // 1
        byte[][] buff = key.getBuff();
        additionalVisualKey.put("1. G = C", Utils.getStringByByteArr(buff, sizeBlockDt.getCollums()));
        additionalVisualKey.put("2. G = G ⊞ K(a)", Utils.getStringByByteArr(key.getData(), sizeBlockDt.getCollums()));
        key.s_boxes(); // 2
        additionalVisualKey.put("3. G = Box(G)", Utils.getStringByByteArr(key.getData(), sizeBlockDt.getCollums()));
        key.shift_Rows();//3
        additionalVisualKey.put("4. G = Rows(G)", Utils.getStringByByteArr(key.getData(), sizeBlockDt.getCollums()));
        key.mix_Columns();//4
        additionalVisualKey.put("5. G = Col(G)", Utils.getStringByByteArr(key.getData(), sizeBlockDt.getCollums()));
        key.xor(); // 5
        additionalVisualKey.put("6. G = G ⨁ K(w)", Utils.getStringByByteArr(key.getData(), sizeBlockDt.getCollums()));
        key.s_boxes(); // 6
        additionalVisualKey.put("7. G = Box(G)", Utils.getStringByByteArr(key.getData(), sizeBlockDt.getCollums()));
        key.shift_Rows();//7
        additionalVisualKey.put("8. G = Rows(G)", Utils.getStringByByteArr(key.getData(), sizeBlockDt.getCollums()));
        key.mix_Columns();//8
        additionalVisualKey.put("9. G = Col(G)", Utils.getStringByByteArr(key.getData(), sizeBlockDt.getCollums()));
        key.change();//9
        additionalVisualKey.put("10. G = G ⊞ K(a)", Utils.getStringByByteArr(key.getData(), sizeBlockDt.getCollums()));
        key.s_boxes(); // 10
        additionalVisualKey.put("11. G = Box(G)", Utils.getStringByByteArr(key.getData(), sizeBlockDt.getCollums()));
        key.shift_Rows();//11
        additionalVisualKey.put("12. G = Rows(G)", Utils.getStringByByteArr(key.getData(), sizeBlockDt.getCollums()));
        key.mix_Columns();//12
        kt = key.getData();

        StringBuilder acceptedSB = new StringBuilder();
        for (int j = 0; j < sizeBlockDt.getCollums(); j++) {
            for (int i = 0; i < 8; i++) {
                acceptedSB.append(String.format("%x", Byte.toUnsignedInt(kt[i][j])));
            }
        }
        acceptedTField.setText(acceptedSB.toString());
        additionalVisualKey.put("13. G = Col(G)", acceptedSB.toString());
        generateRoundKeys(null);
    }

    @FXML
    private void generateRoundKeys(ActionEvent event) {
        if (acceptedTField.getText().equals(Constants.EMPTY)) {
            accept(null);
        }

        roundKeysTAField.clear();
        evenKeySubTab.setDisable(false);
        oddKeySubTab.setDisable(false);
        evenVisualKeys.clear();
        oddVisualKeys.clear();

        RandomGenerator key = new RandomGenerator();

        int dataKeyLength = sizeKeyDt.getBits();
        int dataBlockLength = sizeBlockDt.getBits();

        String[] con = key.key_forming(dataKeyLength, dataBlockLength);
        int count = 0;
        int cols = sizeKeyDt.getCollums();
        int cols1 = sizeBlockDt.getCollums();

        Rkeys = new String[con.length * 2 - 1];
        for ( int i = 0; i < Rkeys.length; i++) {
            Rkeys[i] = "";
        }

        int rk = 0;
        boolean fir = true;
        boolean fir1 = true;
        Map<String, String> evenVisualKey;
        for (String constanta : con) {
            evenVisualKey = new LinkedHashMap<>();
            evenVisualKey.put(Constants.EVEN_INDEX, Integer.toString(rk));

            evenVisualKey.put(Constants.KQ, acceptedTField.getText());
            evenVisualKey.put(Constants.K, keyTField.getText());
            byte[][] b_con = key.toHex(constanta, dataBlockLength);
            byte[][] _0_0 = null;
            if (dataKeyLength == dataBlockLength) {
                _0_0 = key.zsuvv(rand, 32, count, cols);
                evenVisualKey.put(Constants.SI, Constants.ZSUVV_32);

            } else if (dataKeyLength > dataBlockLength) {
                if (count % 4 == 0) {
                    byte[][] _0 = key.zsuvv(rand, 16, count, cols);
                    evenVisualKey.put(Constants.SI, Constants.ZSUVV_16);

                    byte[][] nn = new byte[8][cols / 2];

                    for (int j = 0; j < cols / 2; j++)
                    {
                        for (int i = 0; i < 8; i++)
                        {
                            nn[i][j] = _0[i][j];
                        }
                    }

                    //
                    _0_0 = nn;
                } else {
                    int o = (int) Math.floor((double) (count / 4));

                    byte[][] _0 = key.zsuvv(rand, 64,o, cols);
                    evenVisualKey.put(Constants.SI, Constants.ZSUVV_64);

                    byte[][] nn = new byte[8][cols / 2];

                    for (int j = cols / 2; j < cols; j++) {
                        for (int i = 0; i < 8; i++) {
                            nn[i][j - cols / 2] = _0[i][j];
                        }
                    }

                    _0_0 = nn;
                }
            }
            count += 2;

            evenVisualKey.put(Constants.OPER_1, Utils.getStringByByteArr(_0_0, cols1));

            evenVisualKey.put(Constants.OPER_2, Utils.getStringByByteArr(b_con, cols1));

            evenVisualKey.put(Constants.OPER_3, Utils.getStringByByteArr(b_con, cols1));

            byte[][] add_rkey = key.addNumbers(kt, b_con, cols1);

            evenVisualKey.put(Constants.OPER_4, Utils.getStringByByteArr(add_rkey, cols1));

            byte[][] kt_round = key.addNumbers(add_rkey, _0_0, cols1);

            evenVisualKey.put(Constants.OPER_5, Utils.getStringByByteArr(kt_round, cols1));

            byte[][] s_box1 = key.sBoxes(kt_round, cols1);

            evenVisualKey.put(Constants.OPER_6, Utils.getStringByByteArr(s_box1, cols1));

            byte[][] s_row1 = key.S_Rows(s_box1, cols1);

            evenVisualKey.put(Constants.OPER_7, Utils.getStringByByteArr(s_row1, cols1));

            byte[][] m_col1 = key.M_Columns(s_row1, cols1);

            evenVisualKey.put(Constants.OPER_8, Utils.getStringByByteArr(m_col1, cols1));

            byte[][] xor_rkey = key.xor_rkey(add_rkey, m_col1, cols1);

            evenVisualKey.put(Constants.OPER_9, Utils.getStringByByteArr(xor_rkey, cols1));

            byte[][] s_box2 = key.sBoxes(xor_rkey, cols1);

            evenVisualKey.put(Constants.OPER_10, Utils.getStringByByteArr(s_box2, cols1));

            byte[][] s_row2 = key.S_Rows(s_box2, cols1);

            evenVisualKey.put(Constants.OPER_11, Utils.getStringByByteArr(s_row2, cols1));

            byte[][] m_col2 = key.M_Columns(s_row2, cols1);

            evenVisualKey.put(Constants.OPER_12, Utils.getStringByByteArr(m_col2, cols1));

            byte[][] _final = key.addNumbers(m_col2, add_rkey, cols1);

            evenVisualKey.put(Constants.RESULT, Utils.getStringByByteArr(_final, cols1));

            for (int j = 0; j < cols1; j++) {
                for (int i = 0; i < 8; i++) {
                    Rkeys[rk] += Integer.toString(_final[i][j]) + "/";
                }
            }

            rk += 2;
            evenVisualKeys.add(evenVisualKey);
        }

        Map<String, String> oddVisualKey;
        for(int ii=0; ii < con.length * 2 - 1; ii+=2) {
            oddVisualKey = new LinkedHashMap<>();

            oddVisualKey.put(Constants.ODD_INDEX, Integer.toString(ii + 1));
            if (ii == con.length * 2 - 2) break;

            String[] buf = Rkeys[ii].split("/");
            int[] num = new int[buf.length];
            int c = 0;
            for (String s : buf) {
                if (!s.isEmpty()) {
                    num[c] = Integer.parseInt(s);
                    c++;
                }
            }

            byte[][] data = new byte[8][cols1];
            c = 0;
            for (int j = 0; j < cols1; j++) {
                for (int i = 0; i < 8; i++) {
                    data[i][j] = (byte) num[c];
                    c++;
                }
            }

            int si = ((int)(dataBlockLength / 4) + 24);

            oddVisualKey.put(Constants.EVEN_KEY, Utils.getStringByByteArr(data, cols1));

            oddVisualKey.put(Constants.SI, Integer.toString(si));

            byte[][] left = key.zsuvvL(data, si, 1, cols1);

            oddVisualKey.put(Constants.RESULT, Utils.getStringByByteArr(left, cols1));
            oddVisualKeys.add(oddVisualKey);

            rk = 0;
            for (int j = 0; j < cols1; j++) {
                for (int i = 0; i < 8; i++) {
                    Rkeys[ii+1] += Integer.toString(left[i][j]) + "/";
                }
            }
        }

        int ccc = 1;
        for (String s : Rkeys) {
            String[] buff = s.split("/");
            int[] num = new int[buff.length];
            int c = 0;
            for (String s1 : buff) {
                if (!s1.isEmpty()) {
                    num[c] = Integer.parseInt(s1);
                    c++;
                }
            }

            byte[][] data = new byte[8][cols1];
            c = 0;
            for (int j = 0; j < cols1; j++) {
                for (int i = 0; i < 8; i++) {
                    data[i][j] = (byte) num[c];
                    c++;
                }
            }

            roundKeysTAField.appendText("Ключ [" + ccc + "] = ");
            for (int j = 0; j < cols1; j++) {
                for (int i = 0; i < 8; i++) {
                    String s2 = String.format("%x", Byte.toUnsignedInt(data[i][j]));
                    if (s.length() < 2) {
                        s2 = "0" + s2;
                    }
                    roundKeysTAField.appendText(s2);
                }
            }
            roundKeysTAField.appendText("\n");
            ccc++;
        }
    }

    @FXML
    private void doEncrypt(ActionEvent event) {
        if (roundKeysTAField.getText().isEmpty() ||
                keyTField.getText().isEmpty() ||
                acceptedTField.getText().isEmpty()) {

            generate(null);
            accept(null);
            generateRoundKeys(null);
        }

        encryptSubTab.setDisable(false);
        outputEncryptTAField.clear();
        encryptVisualKeys.clear();

        int bl_l = sizeBlockDt.getBits() / 4;

        String text1 = inputEncryptTAField.getText();

        int cols = sizeBlockDt.getCollums();
        if (text1.length() % bl_l != 0) {
            if (text1.length() < bl_l) {
                int dop = bl_l - text1.length();
                text1 += "10";
                for (int i = 0; i < dop - 1; i++) {
                    text1 += "00";
                }
            }
            else {
                int dop = text1.length() % bl_l;
                text1 += "1";
                for (int i = 0; i < (16 * cols) - dop - 1; i++) {
                    text1 += "0";
                }
            }
        }

        int kblock = text1.length() / bl_l;

        String[] bl = new String[kblock];
        int blk = 0;

        for (int l = 0; l < kblock; l++) {
            bl[l] = text1.substring(blk, (blk + (16 * cols)) > text1.length() ? text1.length() : (blk + (16 * cols)));
            blk += (16 * cols);
        }

        NumHex nh = new NumHex();

        byte[][] mydata = new byte[8][cols];

        for (int t = 0; t < bl.length; t++) {
            int tt = 0;
            int nn = 0;
            for (int j = 0; j < cols; j++) {
                for (int i = 0; i < 8; i++) {
                    if (nn > bl[t].length()) {
                        break;
                    }
                    mydata[i][j] = (byte) nh.convertInt(bl[t].substring(nn, (nn + 2) > bl[t].length() ? bl[t].length() : (nn + 2)));
                    nn += 2;
                }
            }
            byte[][] buffer = new byte[8][cols];

            Map<String, Map<String, String>> blockCache = new LinkedHashMap<>();
            for (int r = 0; r < Rkeys.length; r++) {
                String[] buff = Rkeys[r].split("/");
                int[] num = new int[buff.length];
                int c = 0;
                for (String s1 : buff) {
                    if (!s1.isEmpty()) {
                        num[c] = Integer.parseInt(s1);
                        c++;
                    }
                }

                byte[][] data = new byte[8][cols];
                c = 0;
                for (int j = 0; j < cols; j++) {
                    for (int i = 0; i < 8; i++) {
                        data[i][j] = (byte) num[c];
                        c++;
                    }
                }


                Map<String, String> steps = new LinkedHashMap<>();
                String bufStr;
                if (r == 0) {
                    bufStr = Utils.getStringByByteArr(mydata, cols);
                    steps.put(Constants.INPUT_DATA, bufStr);

                    bufStr = Utils.getStringByByteArr(data, cols);
                    steps.put(Constants.ROUND_KEY, bufStr);

                    buffer = key.addNumbers(data, mydata, cols);

                    bufStr = Utils.getStringByByteArr(buffer, cols);
                    steps.put(Constants.OPER_1, "⊞");
                    steps.put(Constants.OPER_3, Constants.EMPTY);
                    steps.put(Constants.OPER_4, Constants.EMPTY);
                    steps.put(Constants.OPER_5, Constants.EMPTY);
                    steps.put(Constants.RESULT, bufStr);
                } else if (r == Rkeys.length - 1) {
                    bufStr = Utils.getStringByByteArr(buffer, cols);
                    steps.put(Constants.INPUT_DATA, bufStr);

                    buffer = key.sBoxes(buffer, cols);

                    bufStr = Utils.getStringByByteArr(buffer, cols);
                    steps.put(Constants.S_BOX,bufStr);

                    buffer = key.S_Rows(buffer, cols);

                    bufStr = Utils.getStringByByteArr(buffer, cols);
                    steps.put(Constants.S_ROW, bufStr);

                    buffer = key.M_Columns(buffer, cols);

                    bufStr = Utils.getStringByByteArr(buffer, cols);
                    steps.put(Constants.M_COL, bufStr);

                    bufStr = Utils.getStringByByteArr(data, cols);
                    steps.put(Constants.ROUND_KEY, bufStr);

                    buffer = key.addNumbers(data, buffer, cols);

                    bufStr = Utils.getStringByByteArr(buffer, cols);
                    steps.put(Constants.RESULT, bufStr);


                    steps.put(Constants.OPER_1, "⊞");
                    blockCache.put(Integer.toString(r + 1), steps);
                    outputEncryptTAField.appendText(bufStr);
                } else {
                    bufStr = Utils.getStringByByteArr(buffer, cols);
                    steps.put(Constants.INPUT_DATA, bufStr);

                    buffer = key.sBoxes(buffer, cols);

                    bufStr = Utils.getStringByByteArr(buffer, cols);
                    steps.put(Constants.S_BOX, bufStr);

                    buffer = key.S_Rows(buffer, cols);

                    bufStr = Utils.getStringByByteArr(buffer, cols);
                    steps.put(Constants.S_ROW, bufStr);

                    buffer = key.M_Columns(buffer, cols);

                    bufStr = Utils.getStringByByteArr(buffer, cols);
                    steps.put(Constants.M_COL, bufStr);

                    bufStr = Utils.getStringByByteArr(data, cols);
                    steps.put(Constants.ROUND_KEY, bufStr);

                    buffer = key.xor_rkey(buffer, data, cols);

                    bufStr = Utils.getStringByByteArr(buffer, cols);
                    steps.put(Constants.RESULT, bufStr);
                    steps.put(Constants.OPER_1, "⨁");
                }
                blockCache.put(Integer.toString(r + 1), steps);
            }
            encryptVisualKeys.add(blockCache);

        }
    }

    @FXML
    private void doDecrypt(ActionEvent event) {
        outputDecryptTAField.clear();
        decryptVisualKeys.clear();
        decryptSubTab.setDisable(false);


        int bl_l = sizeBlockDt.getBits() / 4;
        String text1 = inputDecryptTAField.getText();


        int kblock = text1.length() / bl_l;

        String[] bl = new String[kblock];

        int cols = sizeBlockDt.getCollums();
        int blk = 0;
        for (int l = 0; l < kblock; l++) {
            bl[l] = text1.substring(blk, (blk + (16 * cols)) > text1.length() ? text1.length() : (blk + (16 * cols)));
            blk += (16 * cols);
        }

        NumHex nh = new NumHex();

        byte[][] mydata = new byte[8][cols];
        for (int t = 0; t < bl.length; t++) {
            int tt = 0;
            int nn = 0;
            for (int j = 0; j < cols; j++) {
                for (int i = 0; i < 8; i++) {
                    if (nn > bl[t].length()) {
                        break;
                    }
                    mydata[i][j] = (byte)nh.convertInt(bl[t].substring(nn, (nn + 2) > bl[t].length() ? bl[t].length() : (nn + 2)));
                    nn += 2;
                }
            }

            byte[][] buffer = new byte[8][cols];

            Map<String, Map<String, String>> blockCache = new LinkedHashMap<>();
            for (int r = Rkeys.length - 1; r >= 0; r--) {
                String[] buff = Rkeys[r].split("/");
                int[] num = new int[buff.length];
                int c = 0;
                for(String s1 : buff) {
                    if (!s1.isEmpty()) {
                        num[c] = Integer.parseInt(s1);
                        c++;
                    }
                }

                byte[][] data = new byte[8][cols];
                c = 0;
                for (int j = 0; j < cols; j++) {
                    for (int i = 0; i < 8; i++) {
                        data[i][j] = (byte)num[c];
                        c++;
                    }
                }


                Map<String, String> steps = new LinkedHashMap<>();
                String bufStr;
                if (r == 0) {
                    bufStr = Utils.getStringByByteArr(buffer, cols);
                    steps.put(Constants.INPUT_DATA, bufStr);

                    bufStr = Utils.getStringByByteArr(data, cols);
                    steps.put(Constants.ROUND_KEY, bufStr);

                    buffer = key.subNumbers(buffer, data, cols);

                    bufStr = Utils.getStringByByteArr(buffer, cols);
                    steps.put(Constants.OPER_1, "⊟");
                    steps.put(Constants.OPER_3, Constants.EMPTY);
                    steps.put(Constants.OPER_4, Constants.EMPTY);
                    steps.put(Constants.OPER_5, Constants.EMPTY);

                    steps.put(Constants.RESULT, bufStr);

                    outputDecryptTAField.appendText(bufStr);
                } else if (r == Rkeys.length - 1)/*0f0e0d0c0b0a09080706050403020100*/{
                    bufStr = Utils.getStringByByteArr(mydata, cols);
                    steps.put(Constants.INPUT_DATA, bufStr);

                    bufStr = Utils.getStringByByteArr(data, cols);
                    steps.put(Constants.ROUND_KEY, bufStr);

                    buffer = key.subNumbers(mydata, data, cols);

                    bufStr = Utils.getStringByByteArr(buffer, cols);

                    steps.put(Constants.OPER_1, "⊟");
                    steps.put(Constants.RESULT, bufStr);

                    buffer = key.inv_MColumns(buffer, cols);

                    bufStr = Utils.getStringByByteArr(buffer, cols);
                    steps.put(Constants.M_COL, bufStr);

                    buffer = key.inv_S_Rows(buffer, cols);

                    bufStr = Utils.getStringByByteArr(buffer, cols);
                    steps.put(Constants.S_ROW, bufStr);

                    buffer = key.inv_Sboxes(buffer, cols);

                    bufStr = Utils.getStringByByteArr(buffer, cols);
                    steps.put(Constants.S_BOX, bufStr);
                } else {
                    bufStr = Utils.getStringByByteArr(buffer, cols);
                    steps.put(Constants.INPUT_DATA, bufStr);

                    bufStr = Utils.getStringByByteArr(data, cols);
                    steps.put(Constants.ROUND_KEY, bufStr);

                    buffer = key.xor_rkey(buffer, data, cols);

                    bufStr = Utils.getStringByByteArr(buffer, cols);
                    steps.put(Constants.OPER_1, "⨁");
                    steps.put(Constants.RESULT, bufStr);

                    buffer = key.inv_MColumns(buffer, cols);

                    bufStr = Utils.getStringByByteArr(buffer, cols);
                    steps.put(Constants.M_COL, bufStr);

                    buffer = key.inv_S_Rows(buffer, cols);

                    bufStr = Utils.getStringByByteArr(buffer, cols);
                    steps.put(Constants.S_ROW, bufStr);

                    buffer = key.inv_Sboxes(buffer, cols);

                    bufStr = Utils.getStringByByteArr(buffer, cols);
                    steps.put(Constants.S_BOX, bufStr);
                }
                blockCache.put(Integer.toString(r + 1), steps);
            }
            decryptVisualKeys.add(blockCache);

        }
    }

    @FXML
    private void onDirectChange(ActionEvent event) {
        final FXMLLoader loader = new FXMLLoader(Main.class.getResource(Constants.DIRECT_CHANGES_PATH));

        Pane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(new StackPane(pane));
        Stage stage = new Stage();
        stage.setTitle("Таблиця прямої заміни");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void onInDirectChange(ActionEvent event) {
        final FXMLLoader loader = new FXMLLoader(Main.class.getResource(Constants.IN_DIRECT_CHANGES_PATH));

        Pane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(new StackPane(pane));
        Stage stage = new Stage();
        stage.setTitle("Таблиця оберненої заміни");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void showEncryptVisual(ActionEvent event) {
        final FXMLLoader loader = new FXMLLoader(Main.class.getResource(Constants.ENCRYPT_VISUAL_PATH));

        TableView tableView = null;
        try {
            tableView = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(new StackPane(tableView));
        Stage stage = new Stage();
        stage.setTitle("Візуалізація зашифрування");
        stage.setScene(scene);

        EncryptController controller = loader.<EncryptController>getController();

        controller.initData(encryptVisualKeys);

        stage.show();
    }

    @FXML
    private void showDecryptVisual(ActionEvent event) {
        final FXMLLoader loader = new FXMLLoader(Main.class.getResource(Constants.DECRYPT_VISUAL_PATH));

        TableView tableView = null;
        try {
            tableView = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(new StackPane(tableView));
        Stage stage = new Stage();
        stage.setTitle("Візуалізація розшифрування");
        stage.setScene(scene);

        DecryptController controller = loader.<DecryptController>getController();

        controller.initData(decryptVisualKeys);

        stage.show();
    }

    @FXML
    private void showAddKeyVisual(ActionEvent event) {
        final FXMLLoader loader = new FXMLLoader(Main.class.getResource(Constants.ADDITIONAL_KEY_VISUAL_PATH));

        TableView tableView = null;
        try {
            tableView = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(new StackPane(tableView));
        Stage stage = new Stage();
        stage.setTitle("Візуалізація формування допоміжного ключа");
        stage.setScene(scene);

        AdditionalKeyController controller = loader.<AdditionalKeyController>getController();

        controller.initData(additionalVisualKey);

        stage.show();
    }

    @FXML
    private void showEvenKeyVisual(ActionEvent event) {
        final FXMLLoader loader = new FXMLLoader(Main.class.getResource(Constants.EVEN_KEY_VISUAL_PATH));

        TableView tableView = null;
        try {
            tableView = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(new StackPane(tableView));
        Stage stage = new Stage();
        stage.setTitle("Візуалізація формування ключів з парними індексами");
        stage.setScene(scene);

        EvenKeyController controller = loader.<EvenKeyController>getController();

        controller.initData(evenVisualKeys);

        stage.show();
    }

    @FXML
    private void showOddKeyVisual(ActionEvent event) {
        final FXMLLoader loader = new FXMLLoader(Main.class.getResource(Constants.ODD_KEY_VISUAL_PATH));

        TableView tableView = null;
        try {
            tableView = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(new StackPane(tableView));
        Stage stage = new Stage();
        stage.setTitle("Візуалізація формування ключів з непарними індексами");
        stage.setScene(scene);

        OddKeyController controller = loader.<OddKeyController>getController();

        controller.initData(oddVisualKeys);

        stage.show();
    }

    @FXML
    private void changeBlockData(ActionEvent event) {
        key128.setDisable(false);
        key256.setDisable(false);
        key512.setDisable(false);

        RadioButton size = (RadioButton) sizeBlockData.getSelectedToggle();
        RadioButton key = (RadioButton) sizeKey.getSelectedToggle();

        String sizeVal = size.getText();

        switch (Integer.parseInt(sizeVal)) {
            case 128:
                key512.setDisable(true);
                key128.setSelected(true);
                break;
            case 256:
                key128.setDisable(true);
                key256.setSelected(true);
                break;
            case 512:
                key128.setDisable(true);
                key256.setDisable(true);
                key512.setSelected(true);
                break;
        }
    }
}
