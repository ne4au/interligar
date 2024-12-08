module com.ne4ay.interligar {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
//    requires jsr305;
//    requires java.net.http;
    requires com.fasterxml.jackson.databind;

    opens com.ne4ay.interligar ;//javafx.fxml, com.ne4ay.interligar.websocket.server;
    exports com.ne4ay.interligar;
    exports com.ne4ay.interligar.udp;
    opens com.ne4ay.interligar.udp to javafx.fxml;
//    opens com.ne4ay.interligar.controllers to javafx.fxml;
    exports com.ne4ay.interligar.utils;
    exports com.ne4ay.interligar.data;
    opens com.ne4ay.interligar.utils to javafx.fxml;
    opens com.ne4ay.interligar.data to javafx.fxml;
    opens com.ne4ay.interligar.websocket ;// to spring.core, spring.beans, spring.context, spring.aop, spring.web, spring.websocket, ALL-UNNAMED;
    opens com.ne4ay.interligar.messages to com.fasterxml.jackson.databind;
    opens com.ne4ay.interligar.messages.data to com.fasterxml.jackson.databind;

    requires static java.sql;  // Example of explicitly naming modules if needed
    requires javax.websocket.api;
    requires jsr305;
    requires org.java_websocket;
    requires java.desktop;
}