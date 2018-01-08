package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import utils.Constants;

public class EncryptRow {
    private StringProperty numberRound;

    private StringProperty inputData;

    private StringProperty sBox;

    private StringProperty sRow;

    private StringProperty mCol;

    private StringProperty oper1;

    private StringProperty roundKey;

    private StringProperty oper2;
    
    private StringProperty oper3;
    
    private StringProperty oper4;
    
    private StringProperty oper5;

    private StringProperty result;

    public EncryptRow(String numberRound, String inputData, String sBox, String sRow, String mCol, String roundKey, String result, String oper1, String oper2, String oper3, String oper4, String oper5) {
        this.numberRound = new SimpleStringProperty(numberRound);
        this.inputData = new SimpleStringProperty(inputData);
        this.sBox = new SimpleStringProperty(sBox);
        this.sRow = new SimpleStringProperty(sRow);
        this.mCol = new SimpleStringProperty(mCol);
        this.roundKey = new SimpleStringProperty(roundKey);
        this.result = new SimpleStringProperty(result);

        this.oper1 = new SimpleStringProperty(oper1);
        this.oper2 = new SimpleStringProperty(oper2);

        this.oper3 = new SimpleStringProperty(oper3);
        this.oper4 = new SimpleStringProperty(oper4);
        this.oper5 = new SimpleStringProperty(oper5);
    }


    public String getNumberRound() {
        return numberRound.get();
    }

    public void setNumberRound(StringProperty numberRound) {
        this.numberRound = numberRound;
    }

    public String getInputData() {
        return inputData.get();
    }

    public void setInputData(StringProperty inputData) {
        this.inputData = inputData;
    }

    public String getSBox() {
        return sBox.get();
    }

    public void setSBox(StringProperty sBox) {
        this.sBox = sBox;
    }

    public String getSRow() {
        return sRow.get();
    }

    public void setSRow(StringProperty sRow) {
        this.sRow = sRow;
    }

    public String getMCol() {
        return mCol.get();
    }

    public void setMCol(StringProperty mCol) {
        this.mCol = mCol;
    }

    public String getRoundKey() {
        return roundKey.get();
    }

    public void setRoundKey(StringProperty roundKey) {
        this.roundKey = roundKey;
    }

    public String getResult() {
        return result.get();
    }

    public void setResult(StringProperty result) {
        this.result = result;
    }

    public String getOper1() {
        return oper1.get();
    }

    public StringProperty oper1Property() {
        return oper1;
    }

    public void setOper1(StringProperty oper1) {
        this.oper1 = oper1;
    }

    public String getOper2() {
        return oper2.get();
    }

    public StringProperty oper2Property() {
        return oper2;
    }

    public void setOper2(StringProperty oper2) {
        this.oper2 = oper2;
    }

    public String getOper3() {
        return oper3.get();
    }

    public StringProperty oper3Property() {
        return oper3;
    }

    public void setOper3(StringProperty oper3) {
        this.oper3 = oper3;
    }

    public String getOper4() {
        return oper4.get();
    }

    public StringProperty oper4Property() {
        return oper4;
    }

    public void setOper4(StringProperty oper4) {
        this.oper4 = oper4;
    }

    public String getOper5() {
        return oper5.get();
    }

    public StringProperty oper5Property() {
        return oper5;
    }

    public void setOper5(StringProperty oper5) {
        this.oper5 = oper5;
    }
}
