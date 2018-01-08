package controllers;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import models.EncryptRow;
import utils.Utils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import static utils.Constants.*;

public class EncryptController {
    @FXML
    private TableView<EncryptRow> tableView;
    @FXML
    private TableColumn<EncryptRow, String> numberRound;
    @FXML
    private TableColumn<EncryptRow, String> inputData;
    @FXML
    private TableColumn<EncryptRow, String> sBox;
    @FXML
    private TableColumn<EncryptRow, String> sRow;
    @FXML
    private TableColumn<EncryptRow, String> mCol;
    @FXML
    private TableColumn<EncryptRow, String> roundKey;
    @FXML
    private TableColumn<EncryptRow, String> result;
    @FXML
    private TableColumn<EncryptRow, String> oper1;
    @FXML
    private TableColumn<EncryptRow, String> oper2;
    @FXML
    private TableColumn<EncryptRow, String> oper3;
    @FXML
    private TableColumn<EncryptRow, String> oper4;
    @FXML
    private TableColumn<EncryptRow, String> oper5;


    private List<Map<String, Map<String, String>>> encryptVisualData;
    private ObservableList<EncryptRow> rows;

    public void initData(List<Map<String, Map<String, String>>> encyptVisualData) {
        rows = FXCollections.observableArrayList();
        this.encryptVisualData = encyptVisualData;

        prepareTable();
        fillTable();
    }

    private void prepareTable() {
        numberRound.setCellValueFactory(new PropertyValueFactory<EncryptRow, String>(NUMBER_ROUND));
        inputData.setCellValueFactory(new PropertyValueFactory<EncryptRow, String>(INPUT_DATA));
        sBox.setCellValueFactory(new PropertyValueFactory<EncryptRow, String>(S_BOX));
        sRow.setCellValueFactory(new PropertyValueFactory<EncryptRow, String>(S_ROW));
        mCol.setCellValueFactory(new PropertyValueFactory<EncryptRow, String>(M_COL));
        roundKey.setCellValueFactory(new PropertyValueFactory<EncryptRow, String>(ROUND_KEY));
        result.setCellValueFactory(new PropertyValueFactory<EncryptRow, String>(RESULT));
        oper1.setCellValueFactory(new PropertyValueFactory<EncryptRow, String>(OPER_1));
        oper2.setCellValueFactory(new PropertyValueFactory<EncryptRow, String>(OPER_2));
        oper3.setCellValueFactory(new PropertyValueFactory<EncryptRow, String>(OPER_3));
        oper4.setCellValueFactory(new PropertyValueFactory<EncryptRow, String>(OPER_4));
        oper5.setCellValueFactory(new PropertyValueFactory<EncryptRow, String>(OPER_5));
        tableView.getSelectionModel().setCellSelectionEnabled(true);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        Utils.installCopyPasteHandler(tableView);
    }



    private void fillTable() {

        for (Map<String, Map<String, String>> map : encryptVisualData) {

            Set<Map.Entry<String, Map<String, String>>> outerEntrySet = map.entrySet();


            for (Map.Entry<String, Map<String, String>> entry : outerEntrySet) {
                String outerKey = entry.getKey();
                Map<String, String> values = entry.getValue();

                String numberRound = outerKey;
                String inputData = EMPTY;
                String sBox = EMPTY;
                String sRow = EMPTY;
                String mCol = EMPTY;
                String roundKey = EMPTY;
                String result = EMPTY;
                String oper1 = EMPTY;
                String oper2 = EQUALS;
                String oper3 = ARROW;
                String oper4 = ARROW;
                String oper5 = ARROW;

                Set<Map.Entry<String, String>> innerEntrySet = values.entrySet();
                for (Map.Entry<String, String> val : innerEntrySet) {
                    String innerKey = val.getKey();


                    switch (innerKey) {
                        case INPUT_DATA:
                            inputData = val.getValue();
                            break;
                        case S_BOX:
                            sBox = val.getValue();
                            break;
                        case S_ROW:
                            sRow = val.getValue();
                            break;
                        case M_COL:
                            mCol = val.getValue();
                            break;
                        case ROUND_KEY:
                            roundKey = val.getValue();
                            break;
                        case RESULT:
                            result = val.getValue();
                            break;
                        case OPER_1:
                            oper1 = val.getValue();
                            break;
                        case OPER_2:
                            oper2 = val.getValue();
                            break;
                        case OPER_3:
                            oper3 = val.getValue();
                            break;
                        case OPER_4:
                            oper4 = val.getValue();
                            break;
                        case OPER_5:
                            oper5 = val.getValue();
                            break;
                    }
                }

                EncryptRow row = new EncryptRow(
                        numberRound,
                        inputData,
                        sBox,
                        sRow,
                        mCol,
                        roundKey,
                        result,
                        oper1,
                        oper2,
                        oper3,
                        oper4,
                        oper5
                );

                row.setOper2(new SimpleStringProperty(oper2));

                rows.add(row);
            }
            rows.add(new EncryptRow(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY));
        }
        rows.remove(rows.size() - 1);

        tableView.setItems(rows);
    }
}
