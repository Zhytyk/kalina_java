package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AddKeyRow {
    private StringProperty operation;

    private StringProperty result;

    public AddKeyRow(String operation, String result) {
        this.operation = new SimpleStringProperty(operation);
        this.result = new SimpleStringProperty(result);
    }

    public String getOperation() {
        return operation.get();
    }

    public StringProperty operationProperty() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation.set(operation);
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
