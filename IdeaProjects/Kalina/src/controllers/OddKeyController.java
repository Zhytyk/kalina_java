package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.OddKeyRow;
import utils.Utils;

import java.util.List;
import java.util.Map;

import static utils.Constants.*;

public class OddKeyController {
    @FXML
    private TableView<OddKeyRow> tableView;
    @FXML
    private TableColumn<OddKeyRow, String> oddIndex;
    @FXML
    private TableColumn<OddKeyRow, String> evenKey;
    @FXML
    private TableColumn<OddKeyRow, String> si;
    @FXML
    private TableColumn<OddKeyRow, String> result;

    private List<Map<String, String>> oddKeyData;
    private ObservableList<OddKeyRow> rows;

    public void initData(List<Map<String, String>> oddKeyData) {
        this.rows = FXCollections.observableArrayList();
        this.oddKeyData = oddKeyData;

        prepareTable();
        fillTable();
    }

    private void prepareTable() {
        oddIndex.setCellValueFactory(new PropertyValueFactory<OddKeyRow, String>(ODD_INDEX));
        evenKey.setCellValueFactory(new PropertyValueFactory<OddKeyRow, String>(EVEN_KEY));
        si.setCellValueFactory(new PropertyValueFactory<OddKeyRow, String>(SI));
        result.setCellValueFactory(new PropertyValueFactory<OddKeyRow, String>(RESULT));

        tableView.getSelectionModel().setCellSelectionEnabled(true);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        Utils.installCopyPasteHandler(tableView);

    }

    private void fillTable() {
        String oddIndex, evenKey, si, result;

        for (Map<String, String> oper : oddKeyData) {
            oddIndex = oper.get(ODD_INDEX);
            evenKey = oper.get(EVEN_KEY);
            si = oper.get(SI);
            result = oper.get(RESULT);


            OddKeyRow evenKeyData = new OddKeyRow(
                    oddIndex, evenKey, si, result
            );
            rows.add(evenKeyData);
        }

        tableView.setItems(rows);
    }
}
