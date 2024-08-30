package com.example.updateactivedirectoryjavafx.Controller;

import com.example.updateactivedirectoryjavafx.Selenium.FillForm;
import com.example.updateactivedirectoryjavafx.Utility.LoadDepartmentList;
import com.example.updateactivedirectoryjavafx.Utility.LoadExcel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public AnchorPane mainPanel;
    public TextField mNameTextArea;
    public TextField mUserTextArea;
    public TextField mDepartmentTextArea;
    public TextField mLocationTextArea;
    public TextField mMailcodeTextArea;
    public TextField mPasswordTextArea;
    public Button mLogInButton;
    public Button mLoadDepartmentButton;
    public Button mExcelButton;
    public TextField mSheetNumberTextArea;
    public Button mNextButton;

    private FillForm fillForm;
    private WebDriver driver;

    private File excel = null;
    private LoadExcel loadExcel;

    private int counter = 0;
    private SearchDatabase searchDatabase;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        searchDatabase = new SearchDatabase();

        mLogInButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(mUserTextArea.getText().isEmpty()||mPasswordTextArea.getText().isEmpty()){

                    //open dialog for error
                    showDialog("Please enter your username and password");

                }else{
                    driver = new FirefoxDriver();
                    fillForm = new FillForm(driver);
                    driver.get("https://act.ucsd.edu/telecomlink/directory/manager?jlinkevent=Default");
                    fillForm.openWindow(mUserTextArea.getText(), mPasswordTextArea.getText());

                    mUserTextArea.setText("");
                    mPasswordTextArea.setText("");
                }
            }
        });

        mLoadDepartmentButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                LoadDepartmentList loadDepartmentList = new LoadDepartmentList();

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Department Excel File");
                fileChooser.setInitialDirectory(new File("C:\\Users\\low85\\OneDrive\\Desktop"));
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel File", "*.xlsx"));
                File file = fileChooser.showOpenDialog(mLoadDepartmentButton.getScene().getWindow());
                if(file != null){
                    loadDepartmentList.truncateTable();
                    loadDepartmentList.loadDepartment(file);
                }
            }
        });

        mExcelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(mSheetNumberTextArea.getText().isEmpty()){
                    showDialog("Please enter a sheet number");
                    return;
                }

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Active Excel File");
                fileChooser.setInitialDirectory(new File("C:\\Users\\low85\\OneDrive\\Desktop"));
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel File", "*.xlsx"));
                File file = fileChooser.showOpenDialog(mLoadDepartmentButton.getScene().getWindow());

                if(file != null){
                    try{
                        int sheetNumber = Integer.parseInt(mSheetNumberTextArea.getText());
                        loadExcel = new LoadExcel(file,sheetNumber-1);
                    }catch (Exception e){
                        showDialog("Please enter a valid sheet number");
                    }

                }
            }
        });

        mNextButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                String firstName = loadExcel.getEmployeeFirstName(counter);
                String lastName = loadExcel.getEmployeeLastName(counter);

                //if there is no name in excel then it's the end of the sheet
                if(firstName.equals("empty")||lastName.equals("empty")){
                    showDialog("You've reached the end");
                    return;
                }else {
                    mNameTextArea.setText(firstName + " " + lastName);
                }

                int employeeID = loadExcel.getEmployeeID(counter);
                //employee id returns 0 if the id number cell is colored in/done already
                if(employeeID == 0){
                    showDialog("You already did this one");
                    counter++;
                    return;
                }

                int deptCode = loadExcel.getDeptCode(counter);
                String deptName = loadExcel.getNameOfDept(counter);
                //removing apostrophes
                if(deptName.contains("'")){
                    deptName = deptName.replace("'","");
                }

                ResultSet rs = null;
                //Search By Name -----------------------------------------------------------------
                rs = searchDatabase.searchDatabase(deptName);

                try{
                    if(rs.isBeforeFirst()){
                        rs.next();
                        String locationCode = rs.getString("location_code");
                        String mailCode = rs.getString("mail_code");

                        if(locationCode == null||mailCode == null||mailCode.equals("VARIOUS MAIL CODE")){

                            rs = null;

                            //Search By Code ------------------------------------------------------

                            rs = searchDatabase.searchDatabaseByDeptCode(deptCode);

                            if(rs.isBeforeFirst()){
                                rs.next();
                                String locationCodeForDeptCode = rs.getString("location_code");
                                String mailCodeForDeptCode = rs.getString("mail_code");

                                if(locationCodeForDeptCode == null||mailCodeForDeptCode == null||mailCodeForDeptCode.equals("VARIOUS MAIL CODE")){
                                    ErrorFormController errorFormController = new ErrorFormController(driver,employeeID,deptName);

                                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/updateactivedirectoryjavafx/errorform.fxml"));
                                    fxmlLoader.setController(errorFormController);
                                    Scene scene = new Scene(fxmlLoader.load());
                                    Stage newstage = new Stage();
                                    newstage.setScene(scene);
                                    newstage.show();
                                    counter++;
                                    return;
                                }

                                mDepartmentTextArea.setText(deptName);
                                mMailcodeTextArea.setText(locationCodeForDeptCode);
                                mLocationTextArea.setText(locationCodeForDeptCode);

                                fillForm.nextEntry(employeeID,splitString(locationCodeForDeptCode),splitString(mailCodeForDeptCode));
                                counter++;
                                return;

                            }else{
                                fillForm.clearPage();
                                showDialog("There is no department in the database");
                                mMailcodeTextArea.setText("");
                                mLocationTextArea.setText("");

                                ErrorFormController errorFormController = new ErrorFormController(driver,employeeID,deptName);

                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/updateactivedirectoryjavafx/errorform.fxml"));
                                fxmlLoader.setController(errorFormController);
                                Scene scene = new Scene(fxmlLoader.load());
                                Stage newstage = new Stage();
                                newstage.setScene(scene);
                                newstage.show();

                                counter++;
                                return;
                            }
                        }

                        mDepartmentTextArea.setText(deptName);
                        mLocationTextArea.setText(splitString(locationCode));
                        mMailcodeTextArea.setText(splitString(mailCode));

                        fillForm.nextEntry(employeeID,splitString(locationCode),splitString(mailCode));
                    }else{
                        rs = null;
                        rs = searchDatabase.searchDatabaseByDeptCode(deptCode);

                        if(rs.isBeforeFirst()){
                            rs.next();
                            String locationCode = rs.getString("location_code");
                            String mailCode = rs.getString("mail_code");

                            if(locationCode == null||mailCode == null||mailCode.equals("VARIOUS MAIL CODE")){
                                ErrorFormController efc = new ErrorFormController(driver,employeeID,deptName);

                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/updateactivedirectoryjavafx/errorform.fxml"));
                                fxmlLoader.setController(efc);
                                Scene scene = new Scene(fxmlLoader.load());
                                Stage newstage = new Stage();
                                newstage.setScene(scene);
                                newstage.show();
                                counter++;
                                return;
                            }

                            mDepartmentTextArea.setText(deptName);
                            mLocationTextArea.setText(splitString(locationCode));
                            mMailcodeTextArea.setText(splitString(mailCode));

                            fillForm.nextEntry(employeeID,splitString(locationCode),splitString(mailCode));
                        }else{
                            fillForm.clearPage();
                            showDialog("There is no department in the database");
                            mMailcodeTextArea.setText("");
                            mLocationTextArea.setText("");

                            ErrorFormController errorFormController = new ErrorFormController(driver,employeeID,deptName);

                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/updateactivedirectoryjavafx/errorform.fxml"));
                            fxmlLoader.setController(errorFormController);
                            Scene scene = new Scene(fxmlLoader.load());
                            Stage newstage = new Stage();
                            newstage.setScene(scene);
                            newstage.show();

                            counter++;
                            return;
                        }
                    }
                }catch (SQLException e){
                    throw new RuntimeException();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                counter++;
            }
        });
    }

    public String splitString(String text){
        String newText = text;
        if(text.contains("/")){
            newText = text.split("/")[0];
        }

        return newText;
    }

    public void showDialog(String text){
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(mNextButton.getScene().getWindow());
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