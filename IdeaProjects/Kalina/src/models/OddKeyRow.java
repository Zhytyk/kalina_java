package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class OddKeyRow {
    private StringProperty oddIndex;
    private StringProperty evenKey;
    private StringProperty si;
    private StringProperty result;

    public OddKeyRow(String oddIndex, String evenKey, String si, String result) {
        this.oddIndex = new SimpleStringProperty(oddIndex);
        this.evenKey = new SimpleStringProperty(evenKey);
        this.si = new SimpleStringProperty(si);
        this.result = new SimpleStringProperty(result);
    }

    public String getOddIndex() {
        return oddIndex.get();
    }

    public StringProperty oddIndexProperty() {
        return oddIndex;
    }

    public void setOddIndex(String oddIndex) {
        this.oddIndex.set(oddIndex);
    }

    public String getEvenKey() {
        return evenKey.get();
    }

    public StringProperty evenKeyProperty() {
        return evenKey;
    }

    public void setEvenKey(String evenKey) {
        this.evenKey.set(evenKey);
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

    public String getResult() {
        return result.get();
    }

    public StringProperty resultProperty() {
        return result;
    }

    public void setResult(String result) {
        this.result.set(result);
    }
}
