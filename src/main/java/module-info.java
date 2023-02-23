module com.andrearigamonti.pmdproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires net.synedra.validatorfx;
    requires java.sql;
    requires de.mkammerer.argon2.nolibs;
    requires org.jetbrains.annotations;
    requires com.sun.jna;

    opens com.pmdproject to javafx.fxml;
    opens com.pmdproject.controller to javafx.fxml;
    exports com.pmdproject;
    exports com.pmdproject.model;
    exports com.pmdproject.controller;
}