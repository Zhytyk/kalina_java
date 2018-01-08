package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.EvenKeyRow;
import utils.Utils;

import java.util.List;
import java.util.Map;

import static utils.Constants.*;

public class EvenKeyController {
    @FXML
    private TableView<EvenKeyRow> tableView;
    @FXML
    private TableColumn<EvenKeyRow, String> evenIndex;
    @FXML
    private TableColumn<EvenKeyRow, String> k;
    @FXML
    private TableColumn<EvenKeyRow, String> kq;
    @FXML
    private TableColumn<EvenKeyRow, String> si;
    @FXML
    private TableColumn<EvenKeyRow, String> oper1;
    @FXML
    private TableColumn<EvenKeyRow, String> oper2;
    @FXML
    private TableColumn<EvenKeyRow, String> oper3;
    @FXML
    private TableColumn<EvenKeyRow, String> oper4;
    @FXML
    private TableColumn<EvenKeyRow, String> oper5;
    @FXML
    private TableColumn<EvenKeyRow, String> oper6;
    @FXML
    private TableColumn<EvenKeyRow, String> oper7;
    @FXML
    private TableColumn<EvenKeyRow, String> oper8;
    @FXML
    private TableColumn<EvenKeyRow, String> oper9;
    @FXML
    private TableColumn<EvenKeyRow, String> oper10;
    @FXML
    private TableColumn<EvenKeyRow, String> oper11;
    @FXML
    private TableColumn<EvenKeyRow, String> oper12;
    @FXML
    private TableColumn<EvenKeyRow, String> result;

    private List<Map<String, String>> evenKeyData;
    private ObservableList<EvenKeyRow> rows;

    public void initData(List<Map<String, String>> oddKeyData) {
        this.evenKeyData = oddKeyData;
        rows = FXCollections.observableArrayList();

        prepareTable();
        fillTable();
    }

    private void prepareTable() {
        evenIndex.setCellValueFactory(new PropertyValueFactory<EvenKeyRow, String>(EVEN_INDEX));
        si.setCellValueFactory(new PropertyValueFactory<EvenKeyRow, String>(SI));
        kq.setCellValueFactory(new PropertyValueFactory<EvenKeyRow, String>(KQ));
        k.setCellValueFactory(new PropertyValueFactory<EvenKeyRow, String>(K));
        oper1.setCellValueFactory(new PropertyValueFactory<EvenKeyRow, String>(OPER_1));
        oper2.setCellValueFactory(new PropertyValueFactory<EvenKeyRow, String>(OPER_2));
        oper3.setCellValueFactory(new PropertyValueFactory<EvenKeyRow, String>(OPER_3));
        oper4.setCellValueFactory(new PropertyValueFactory<EvenKeyRow, String>(OPER_4));
        oper5.setCellValueFactory(new PropertyValueFactory<EvenKeyRow, String>(OPER_5));
        oper6.setCellValueFactory(new PropertyValueFactory<EvenKeyRow, String>(OPER_6));
        oper7.setCellValueFactory(new PropertyValueFactory<EvenKeyRow, String>(OPER_7));
        oper8.setCellValueFactory(new PropertyValueFactory<EvenKeyRow, String>(OPER_8));
        oper9.setCellValueFactory(new PropertyValueFactory<EvenKeyRow, String>(OPER_9));
        oper10.setCellValueFactory(new PropertyValueFactory<EvenKeyRow, String>(OPER_10));
        oper11.setCellValueFactory(new PropertyValueFactory<EvenKeyRow, String>(OPER_11));
        oper12.setCellValueFactory(new PropertyValueFactory<EvenKeyRow, String>(OPER_12));
        result.setCellValueFactory(new PropertyValueFactory<EvenKeyRow, String>(RESULT));


        tableView.getSelectionModel().setCellSelectionEnabled(true);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        Utils.installCopyPasteHandler(tableView);
    }

    private void fillTable() {
        String evenIndex, kq, k, si, oper1, oper2, oper3, oper4,
                oper5, oper6, oper7, oper8, oper9, oper10,
                oper11, oper12, result;

        for (Map<String, String> oper : evenKeyData) {
            evenIndex = oper.get(EVEN_INDEX);
            kq = oper.get(KQ);
            k = oper.get(K);
            si = oper.get(SI);
            oper1 = oper.get(OPER_1);
            oper2 = oper.get(OPER_2);
            oper3 = oper.get(OPER_3);
            oper4 = oper.get(OPER_4);
            oper5 = oper.get(OPER_5);
            oper6 = oper.get(OPER_6);
            oper7 = oper.get(OPER_7);
            oper8 = oper.get(OPER_8);
            oper9 = oper.get(OPER_9);
            oper10 = oper.get(OPER_10);
            oper11 = oper.get(OPER_11);
            oper12 = oper.get(OPER_12);
            result = oper.get(RESULT);


            EvenKeyRow evenKeyData = new EvenKeyRow(
                    evenIndex, kq, k, si, oper1, oper2, oper3, oper4,
                    oper5, oper6, oper7, oper8, oper9,
                    oper10, oper11, oper12, result
            );
            rows.add(evenKeyData);
        }

        tableView.setItems(rows);
    }
}
