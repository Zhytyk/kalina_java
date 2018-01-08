package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EvenKeyRow {
    private StringProperty evenIndex;
    private StringProperty kq;
    private StringProperty k;
    private StringProperty si;
    private StringProperty oper1;
    private StringProperty oper2;
    private StringProperty oper3;
    private StringProperty oper4;
    private StringProperty oper5;
    private StringProperty oper6;
    private StringProperty oper7;
    private StringProperty oper8;
    private StringProperty oper9;
    private StringProperty oper10;
    private StringProperty oper11;
    private StringProperty oper12;
    private StringProperty result;

    public EvenKeyRow(String evenIndex, String kq, String k, String si, String oper1, String oper2, String oper3, String oper4, String oper5, String oper6, String oper7, String oper8, String oper9, String oper10, String oper11, String oper12, String result) {
        this.evenIndex = new SimpleStringProperty(evenIndex);
        this.kq = new SimpleStringProperty(kq);
        this.k = new SimpleStringProperty(k);
        this.si = new SimpleStringProperty(si);
        this.oper1 = new SimpleStringProperty(oper1);
        this.oper2 = new SimpleStringProperty(oper2);
        this.oper3 = new SimpleStringProperty(oper3);
        this.oper4 = new SimpleStringProperty(oper4);
        this.oper5 = new SimpleStringProperty(oper5);
        this.oper6 = new SimpleStringProperty(oper6);
        this.oper7 = new SimpleStringProperty(oper7);
        this.oper8 = new SimpleStringProperty(oper8);
        this.oper9 = new SimpleStringProperty(oper9);
        this.oper10 = new SimpleStringProperty(oper10);
        this.oper11 = new SimpleStringProperty(oper11);
        this.oper12 = new SimpleStringProperty(oper12);
        this.result = new SimpleStringProperty(result);
    }

    public String getEvenIndex() {
        return evenIndex.get();
    }

    public StringProperty evenIndexProperty() {
        return evenIndex;
    }

    public void setEvenIndex(String evenIndex) {
        this.evenIndex.set(evenIndex);
    }

    public String getOper1() {
        return oper1.get();
    }

    public StringProperty oper1Property() {
        return oper1;
    }

    public void setOper1(String oper1) {
        this.oper1.set(oper1);
    }

    public String getOper2() {
        return oper2.get();
    }

    public StringProperty oper2Property() {
        return oper2;
    }

    public void setOper2(String oper2) {
        this.oper2.set(oper2);
    }

    public String getOper3() {
        return oper3.get();
    }

    public StringProperty oper3Property() {
        return oper3;
    }

    public void setOper3(String oper3) {
        this.oper3.set(oper3);
    }

    public String getOper4() {
        return oper4.get();
    }

    public StringProperty oper4Property() {
        return oper4;
    }

    public void setOper4(String oper4) {
        this.oper4.set(oper4);
    }

    public String getOper5() {
        return oper5.get();
    }

    public StringProperty oper5Property() {
        return oper5;
    }

    public void setOper5(String oper5) {
        this.oper5.set(oper5);
    }

    public String getOper6() {
        return oper6.get();
    }

    public StringProperty oper6Property() {
        return oper6;
    }

    public void setOper6(String oper6) {
        this.oper6.set(oper6);
    }

    public String getOper7() {
        return oper7.get();
    }

    public StringProperty oper7Property() {
        return oper7;
    }

    public void setOper7(String oper7) {
        this.oper7.set(oper7);
    }

    public String getOper8() {
        return oper8.get();
    }

    public StringProperty oper8Property() {
        return oper8;
    }

    public void setOper8(String oper8) {
        this.oper8.set(oper8);
    }

    public String getOper9() {
        return oper9.get();
    }

    public StringProperty oper9Property() {
        return oper9;
    }

    public void setOper9(String oper9) {
        this.oper9.set(oper9);
    }

    public String getOper10() {
        return oper10.get();
    }

    public StringProperty oper10Property() {
        return oper10;
    }

    public void setOper10(String oper10) {
        this.oper10.set(oper10);
    }

    public String getOper11() {
        return oper11.get();
    }

    public StringProperty oper11Property() {
        return oper11;
    }

    public void setOper11(String oper11) {
        this.oper11.set(oper11);
    }

    public String getOper12() {
        return oper12.get();
    }

    public StringProperty oper12Property() {
        return oper12;
    }

    public void setOper12(String oper12) {
        this.oper12.set(oper12);
    }

    public String getResult() {
        return result.get();
    }

    public StringProperty resultProperty() {
        return result;
    }

    public void setResult(String result) {
        this.result.set(result);
    }

    public String getKq() {
        return kq.get();
    }

    public StringProperty kqProperty() {
        return kq;
    }

    public void setKq(String kq) {
        this.kq.set(kq);
    }

    public String getK() {
        return k.get();
    }

    public StringProperty kProperty() {
        return k;
    }

    public void setK(String k) {
        this.k.set(k);
    }

    public String getSi() {
        return si.get();
    }

    public StringProperty siProperty() {
        return si;
    }

    public void setSi(String si) {
        this.si.set(si);
    }
}
