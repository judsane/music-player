module com.olexandryudin.musicplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires de.jensd.fx.glyphs.fontawesome;
    requires com.jfoenix;
    requires mongo.java.driver;


    opens com.olexandryudin.musicplayer to javafx.fxml;
    exports com.olexandryudin.musicplayer;
}