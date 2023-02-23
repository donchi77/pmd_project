package com.pmdproject.controller;

import com.pmdproject.model.AdminManager;
import com.pmdproject.model.Role;
import com.pmdproject.utils.SystemManager;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import net.synedra.validatorfx.Validator;
import java.util.EnumSet;

public class AdminController extends Controller {
    @FXML
    public TextArea textAreaLogs;
    @FXML
    public Label labelUsername;
    @FXML
    public TextField textFieldUsername;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Label labelPassword;
    @FXML
    public PasswordField passwordFieldConfirm;
    @FXML
    public Label labelConfirmPassword;
    @FXML
    public Button buttonSignup;
    @FXML
    public CheckBox checkBoxAdmin;
    @FXML
    public CheckBox checkBoxManager;
    @FXML
    public CheckBox checkBoxChef;
    @FXML
    public CheckBox checkBoxStockman;
    @FXML
    public Label labelLogs;
    @FXML
    public Label labelError;
    @FXML
    public Label labelSuccess;
    @FXML
    public Label labelCreateUser;
    @FXML
    public Label labelSelectUser;
    @FXML
    public ComboBox<String> comboBoxUsers;
    @FXML
    public Button buttonDelete;
    @FXML
    public Label labelDeleteUser;
    @FXML
    public Label labelDeleteInfo;
    private final AdminManager adminManager = new AdminManager();
    private final Validator validator = new Validator();

    @Override
    public void initialize() {
        if (textAreaLogs.getText().isEmpty())
            textAreaLogs.setText(adminManager.getLogs());

        validator.createCheck()
                .dependsOn("username", textFieldUsername.textProperty())
                .withMethod(c -> {
                    String username = c.get("username");
                    if (adminManager.checkUsername(username))
                        c.error("Username esistente.");
                })
                .decorates(textFieldUsername)
                .immediate();

        comboBoxUsers.setItems(adminManager.getUsernames());
    }

    @FXML
    public void onButtonSignupClicked() {
        if (!labelError.getText().isEmpty())
            labelError.setText("");
        if (!labelSuccess.getText().isEmpty())
            labelSuccess.setText("");

        if (passwordField.getText().isEmpty() || passwordField.getText().isBlank())
            labelError.setText("La password non può essere vuota");
        else if (textFieldUsername.getText().isEmpty() || textFieldUsername.getText().isBlank())
            labelError.setText("Username non può essere vuoto");
        else {
            String username = textFieldUsername.getText().trim();
            String password = passwordField.getText().trim();
            String confirm = passwordFieldConfirm.getText().trim();

            if (password.equals(confirm)) {
                EnumSet<Role> temp = EnumSet.noneOf(Role.class);

                if (checkBoxAdmin.isSelected())
                    temp.add(Role.ADMIN);
                else {
                    if (checkBoxManager.isSelected())
                        temp.add(Role.MANAGER);
                    if (checkBoxChef.isSelected())
                        temp.add(Role.CHEF);
                    if (checkBoxStockman.isSelected())
                        temp.add(Role.STOCKMAN);
                }

                if (!adminManager.createUser(username, password, temp))
                    labelError.setText("Fallimento durante la creazione, controllare se username è univoco");
                else {
                    labelSuccess.setText("Utente creato");
                    log("User " + username + " created");
                }
            } else
                labelError.setText("Le password non combaciano");
        }
    }

    @FXML
    public void onCheckBoxSelected() {
        if (checkBoxAdmin.isSelected())
            checkBoxAdmin.setSelected(Boolean.FALSE);
    }

    @FXML
    public void onCheckBoxAdminSelected() {
        if (checkBoxStockman.isSelected())
            checkBoxStockman.setSelected(Boolean.FALSE);
        if (checkBoxChef.isSelected())
            checkBoxChef.setSelected(Boolean.FALSE);
        if (checkBoxManager.isSelected())
            checkBoxManager.setSelected(Boolean.FALSE);
    }

    public void onButtonDeleteClicked() {
        String username = comboBoxUsers.getValue();
        if (!username.isEmpty()) {
            adminManager.deleteUser(username);
            log("User " + username + " made inactive.");

            labelDeleteInfo.setText("Credenziali di accesso rimosse");
        }
    }

    public void menuItemClicked() {
        super.logout();
    }
}
