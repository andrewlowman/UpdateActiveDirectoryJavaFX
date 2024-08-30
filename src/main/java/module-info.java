module com.example.updateactivedirectoryjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.seleniumhq.selenium.api;
    requires org.seleniumhq.selenium.firefox_driver;
    requires java.sql;
    requires org.seleniumhq.selenium.support;
    requires org.apache.poi.ooxml;

    opens com.example.updateactivedirectoryjavafx to javafx.fxml;
    exports com.example.updateactivedirectoryjavafx;
    exports com.example.updateactivedirectoryjavafx.Controller;
    opens com.example.updateactivedirectoryjavafx.Controller to javafx.fxml;
}