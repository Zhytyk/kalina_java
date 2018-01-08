package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public abstract class BaseController {
    @FXML
    protected TabPane tabPane;
    @FXML
    protected ToolBar mainToolBar;
    @FXML
    protected SplitMenuButton mainAddOn;
    @FXML
    protected SplitMenuButton visualization;
    @FXML
    protected Menu vDeploymentKey;
    @FXML
    protected ToggleGroup sizeKey;
    @FXML
    protected ToggleGroup sizeBlockData;
    @FXML
    protected ToggleGroup formatData;
    @FXML
    protected TextField keyTField;
    @FXML
    protected Button generateBtn;
    @FXML
    protected Button confirmBtn;
    @FXML
    protected TextField acceptedTField;
    @FXML
    protected TextArea roundKeysTAField;
    @FXML
    protected Button roundKeysBtn;
    @FXML
    protected TextArea inputEncryptTAField;
    @FXML
    protected Button encryptBtn;
    @FXML
    protected TextArea outputEncryptTAField;
    @FXML
    protected Button decryptBtn;
    @FXML
    protected TextArea outputDecryptTAField;
    @FXML
    protected TextArea inputDecryptTAField;
    @FXML
    protected RadioButton key128;
    @FXML
    protected RadioButton key256;
    @FXML
    protected RadioButton key512;
    @FXML
    protected MenuItem encryptSubTab;
    @FXML
    protected MenuItem decryptSubTab;
    @FXML
    protected MenuItem addKeySubTab;
    @FXML
    protected MenuItem evenKeySubTab;
    @FXML
    protected MenuItem oddKeySubTab;
}
