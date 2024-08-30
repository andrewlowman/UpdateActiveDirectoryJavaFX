package com.example.updateactivedirectoryjavafx.Selenium;

import javafx.fxml.Initializable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;

public class FillForm{

    WebDriver driver;

    public FillForm(WebDriver driver) {
        this.driver = driver;
    }

    public void nextEntry(int emplID, String location, String mail){
        // System.out.println("Mail code in fill form: " + mail);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(500));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("directory$dir_emp_id")));

        WebElement clearButton = driver.findElement(By.xpath("//input[@value='Clear Criteria']"));

        clearButton.click();

        WebElement idBox = driver.findElement(By.name("directory$dir_emp_id"));

        WebElement searchButton = driver.findElement(By.xpath("//input[@value='Search']"));

        idBox.sendKeys(String.valueOf(emplID));
        searchButton.click();
        // wait.until(ExpectedConditions.presenceOfElementLocated(By.name("directory$dir_emp_id")));
        //
        WebElement saveButton = driver.findElement(By.name("Save"));

        WebElement locCode = driver.findElement(By.id("loc_cd"));

        WebElement mailCode = driver.findElement(By.name("directory$dir_mail_code"));

        locCode.clear();

        mailCode.clear();

        locCode.sendKeys(location);

        mailCode.sendKeys(mail);

        WebElement restrictedCheckbox = driver.findElement(By.name("directory$dir_restricted_yn"));

        WebElement nonCUCheckBox = driver.findElement(By.name("directory$dir_noncu_yn"));

        WebElement directoryCheckBox = driver.findElement(By.name("directory$dir_campus_dir_yn"));

        if((restrictedCheckbox.isSelected())&&(!nonCUCheckBox.isSelected())){
            restrictedCheckbox.click();
            if(!directoryCheckBox.isSelected()){
                directoryCheckBox.click();
            }
        }

        saveButton.click();
    }

    public void clearPage(){
        WebElement clearButton = driver.findElement(By.xpath("//input[@value='Clear Criteria']"));

        clearButton.click();
    }

    public void openWindow(String user, String pw){
        driver.manage().window().maximize();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(500));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ssousername")));

        WebElement usernameTextField = driver.findElement(By.id("ssousername"));
        WebElement passwordTextField = driver.findElement(By.id("ssopassword"));
        WebElement loginButton = driver.findElement(By.name("_eventId_proceed"));

        usernameTextField.sendKeys(user);
        passwordTextField.sendKeys(pw);
        loginButton.click();
    }

}



