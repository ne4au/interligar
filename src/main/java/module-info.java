module com.ne4ay.ua.interligar {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires jsr305;

    opens com.ne4ay.interligar to javafx.fxml;
    exports com.ne4ay.interligar;
    exports com.ne4ay.interligar.udp;
    opens com.ne4ay.interligar.udp to javafx.fxml;
//    opens com.ne4ay.interligar.controllers to javafx.fxml;
    exports com.ne4ay.interligar.utils;
    opens com.ne4ay.interligar.utils to javafx.fxml;
}