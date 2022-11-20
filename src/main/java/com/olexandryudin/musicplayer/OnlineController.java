package com.olexandryudin.musicplayer;

import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class OnlineController implements Initializable {
    public FontAwesomeIconView searchBtn;
    public JFXTextField searchBar1;
    @FXML
    private ImageView imageView;
    @FXML
    private JFXSlider slider;
    @FXML
    private Text curTO;
    @FXML
    private Text totlTO;

    static MediaPlayer mediaPlayer;
    static double tTimemilO=0;
    public static Media soundO=null;
    public static Duration durationO=null;
    public static double volumeO=0;

    @FXML
    void playOnlineSong(String address,String id1) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Controller controller=new Controller();
        mediaPlayer=controller.retMePlayer();
        tTimemilO=controller.retTime();
        soundO=controller.retMed();
        volumeO=mediaPlayer.getVolume();

        mediaPlayer.setOnReady(() -> {
            System.out.println("HHHHH"); // ?
            durationO = soundO.getDuration();
            tTimemilO = durationO.toMillis();

            var tTime2 = durationO.toSeconds();
            var mnt = (int) tTime2 / 60;
            var sec = (int) tTime2 % 60;
            var minStr0 = String.valueOf(mnt);
            var secStr0 = String.valueOf(sec);

            if (mnt < 10) minStr0 = "0" + mnt;
            if (sec < 10) secStr0 = "0" + sec;

            var str01 = minStr0 + " : " + secStr0;

            Platform.runLater(() -> totlTO.setText(str01));
            mediaPlayer.play();
            mediaPlayer.setVolume(volumeO);
        });

        mediaPlayer.currentTimeProperty().addListener(observable -> Platform.runLater(() -> {
            System.out.println("YES");
            double curTime1 = mediaPlayer.getCurrentTime().toSeconds();
            slider.setValue((curTime1 * 1000 / tTimemilO) * 100);
//            String str01= String.valueOf((int) curTime1 / 60);
//            String str02 = String.valueOf((int) curTime1 % 60);
//            timeLbl1.setText(str1 + " : " + str2);
        }));

        mediaPlayer.setOnEndOfMedia(() -> {
            slider.setValue(0);
            mediaPlayer.dispose();
            mediaPlayer.play();
        });

        imageView.setImage(controller.retImage());
    }
}
