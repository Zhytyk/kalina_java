package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.AddKeyRow;
import utils.Utils;

import java.util.Map;
import static utils.Constants.*;

public class AdditionalKeyController {
    @FXML
    private TableView<AddKeyRow> tableView;
    @FXML
    private TableColumn<AddKeyRow, String> operation;
    @FXML
    private TableColumn<AddKeyRow, String> result;

    private Map<String, String> additionalKeyData;
    private ObservableList<AddKeyRow> rows;

    public void initData(Map<String, String> additionalKeyData) {
        rows = FXCollections.observableArrayList();
        this.additionalKeyData = additionalKeyData;

        prepareTable();
        fillTable();
    }

    private void prepareTable() {
        operation.setCellValueFactory(new PropertyValueFactory<AddKeyRow, String>(OPERATION));
        result.setCellValueFactory(new PropertyValueFactory<AddKeyRow, String>(RESULT));
        tableView.getSelectionModel().setCellSelectionEnabled(true);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        Utils.installCopyPasteHandler(tableView);
    }

    private void fillTable() {
        for (Map.Entry<String, String> entry : additionalKeyData.entrySet()) {
            String operation = entry.getKey();
            String result = entry.getValue();

            AddKeyRow addKeyRow = new AddKeyRow(operation, result);

            rows.add(addKeyRow);
        }

        tableView.setItems(rows);
    }
}
