package com.example.updateactivedirectoryjavafx.Controller;

import com.example.updateactivedirectoryjavafx.Selenium.FillForm;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.openqa.selenium.WebDriver;

import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class ErrorFormController implements Initializable {

    public TextField errorDepartmentTextField;
    public TextField errorLocationTextField;
    public TextField errorMailCodeTextField;
    public Button errorSubmitButton;
    public Label errorFormLabel;
    public TextField errorFormEmplID;
    private WebDriver driver;
    private FillForm fillForm;
    private int employeeID = 0;
    private String departmentName = "";
    private String labelText;

    public ErrorFormController(WebDriver driver,int emplID, String deptName,String textForLabel) {
        this.driver = driver;
        fillForm = new FillForm(driver);
        employeeID = emplID;
        departmentName = deptName;
        labelText = textForLabel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        errorFormLabel.setText(labelText);
        errorFormEmplID.setText(String.valueOf(employeeID));

        errorSubmitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(errorLocationTextField.getText().isEmpty()||errorMailCodeTextField.getText().isEmpty()){
                    showDialog("{Please enter a location and mail code");
                    return;
                }

                update();
            }
        });

        errorDepartmentTextField.setText(departmentName);
    }

    public void update(){

        String location = errorLocationTextField.getText();
        String mailCode = errorMailCodeTextField.getText();

        fillForm.nextEntry(employeeID,location,mailCode);

        Stage stage = (Stage) errorSubmitButton.getScene().getWindow();
        stage.close();

    }

    public void showDialog(String text){
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(errorDepartmentTextField.getScene().getWindow());
        VBox dialogVbox = new VBox(20);
        Button button = new Button("OK");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = (Stage) button.getScene().getWindow();
                stage.close();
            }
        });
        dialogVbox.setAlignment(Pos.CENTER);
        dialogVbox.getChildren().add(new Label(text));
        dialogVbox.getChildren().add(button);
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }


}
