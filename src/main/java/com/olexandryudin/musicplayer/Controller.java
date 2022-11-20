package com.olexandryudin.musicplayer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.olexandryudin.musicplayer.Main.primaryStage;
import static de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.*;


public class Controller implements Initializable {

    @FXML
    private GridPane gridPane;
    @FXML
    private Pane pan1;
    @FXML
    private Pane pan2;
    @FXML
    private Pane pan3;
    @FXML
    private JFXSlider slider1;
    @FXML
    private ScrollPane scrlPan1;
    @FXML
    private FlowPane flwPan1;
    @FXML
    private JFXSlider volumeSlider;
    @FXML
    private Label timeLbl1;
    @FXML
    private Label timeLbl2;
    @FXML
    private Label songNmLbl1;
    @FXML
    private FontAwesomeIconView pauseBtn;
    @FXML
    private FontAwesomeIconView repeatBtn1;
    @FXML
    private FontAwesomeIconView muteBtn1;
    @FXML
    private FontAwesomeIconView suffleBtn1;
    @FXML
    private FontAwesomeIconView searchBtn;
    @FXML
    private JFXTextField searchBarBtn1;
    @FXML
    private ScrollPane scrolPan2;
    @FXML
    private JFXToggleButton onlinePlayBtn;
    @FXML
    private ImageView mediaImage1;
    @FXML
    private JFXToggleButton newPageBtn;

    static public MediaPlayer mediaPlayer;
    static public boolean setPan1Visib = true;
    static public boolean setPan3Visib = true;
    static public int playCount = 0;
    static public boolean crntPlSts = true;
    static public File[] listOfFiles;
    static public int id0 = -1;
    static public int btnArrPos1 = 0;
    static public double crntVolume1 = 0.5;
    static public Duration tTime1;
    public static double tTimeMil = 0;
    public static JFXButton crntBtn1;
    public static Media sound=null;
    //public static JFXButton[] allFfxBtn1=new JFXButton[1000];
    public static boolean repeatOn=false;
    public static int totalFiles=0;
    public static HashMap<String,Integer> mediaInfo=null;
    public static int[] favList=null;
    public static long currenTime001=0;
    public static boolean firstTim1=true;
    public static boolean suffle1=false;
    public static JFXButton jfxCurButton=null;
    public static boolean curSearch=false;
    public static FlowPane flowPane2=new FlowPane();
    public static boolean onlinePlay=false;
    public static int onLnPlCount1=0;
    public static String onLnId="";
    public static Image curImg1=null;
    public static double xPos=0;
    public static double yPos=0;

    @FXML
    public void initialize() throws IOException, ClassNotFoundException {
        File[] files = FileManager.createFile();
        System.out.println(files.length);

        var in1 = new ObjectInputStream(new FileInputStream(files[0]));
        var in2 = new ObjectInputStream(new FileInputStream(files[1]));
        var in3 = new ObjectInputStream(new FileInputStream(files[2]));

        listOfFiles = (File[]) in1.readObject();
        favList=(int[])in2.readObject();
        mediaInfo=(HashMap<String, Integer>) in3.readObject();
        totalFiles = mediaInfo.get("total");
        System.out.println(totalFiles);

        in1.close();
        in2.close();
        in3.close();

        if (totalFiles > 0) {
            importButtons(listOfFiles, totalFiles);

            id0=mediaInfo.get("id");
            System.out.println(currenTime001);
            try {
                playSong(id0,false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currenTime001=mediaInfo.get("time");
            System.out.println(Duration.millis(currenTime001));
        }
    }

    @FXML
    void closeBtnAction2() throws IOException {
        var in1 = new ObjectOutputStream(new FileOutputStream("D:/musicPlayer/system/data/playerData.dat"));
        var in2 = new ObjectOutputStream(new FileOutputStream("D:/musicPlayer/system/data/playerFavData.dat"));
        var in3 = new ObjectOutputStream(new FileOutputStream("D:/musicPlayer/system/data/currentMedia.dat"));

        in1.writeObject(listOfFiles);
        in2.writeObject(favList);
        System.out.println(totalFiles);

        mediaInfo.put("total",totalFiles);
        mediaInfo.put("id",id0);

        int curTime007=0;
        if(playCount>0) curTime007 = (int) mediaPlayer.getCurrentTime().toSeconds();

        System.out.println(curTime007);
        mediaInfo.put("time",curTime007);

        in3.writeObject(mediaInfo);
        in1.close();
        in2.close();
        in3.close();
        System.exit(0);
    }

    @FXML
    void settingBtnAction() {
        pan3.setVisible(setPan3Visib = !setPan3Visib);
    }

    @FXML
    void playSong(JFXButton jfxButton) throws InterruptedException {
        if(playCount>0)
            crntBtn1.setStyle("-fx-background-color: #202020");

        jfxButton.getStyleClass().add("button2");
        crntBtn1=jfxButton;
        int num1=Integer.parseInt(jfxButton.getId());
        playSong(num1,false);
    }

    @FXML
    void playSong(int id00,boolean bool001) throws InterruptedException {
        if(id00>=totalFiles)
            id00=0;
        else if(id00<0)
            id00=totalFiles-1;

        if(playCount>0) {
            jfxCurButton=(JFXButton)(flwPan1.lookup("#"+id0));
            jfxCurButton.setStyle("-fx-background-color: #202020");
        }

        if(!bool001)
            if (repeatOn)
                id00 = id0;
            else if (suffle1)
                id00 = (int) (Math.random() * totalFiles);

        id0 = id00;
        jfxCurButton=(JFXButton)flwPan1.lookup("#"+id0);
        jfxCurButton.setStyle("-fx-background-color: #3e1c27");
        File file = listOfFiles[id0];
        sound = new Media(file.toURI().toString());
        //Media sound = new Media("http://37.webmusic.pw/music/hindi/movies/2019/g/gully_boy/Asli-Hip-Hop_(webmusic.in).mp3");
        songNmLbl1.setText(file.getName());
        if (playCount > 0)
            mediaPlayer.dispose();

        extractDuplicate2();

        mediaPlayer.setOnEndOfMedia(() -> {
            try {
                slider1.setValue(0);
                playSong(id0+1,false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        mediaPlayer.setOnPlaying(() -> {
            if(firstTim1) {
                firstTim1=false;
                mediaPlayer.seek(Duration.seconds(currenTime001));
            }

            currenTime001=0;

            if(crntPlSts)
                mediaPlayer.play();
            else
                mediaPlayer.pause();
        });

        codeDuplicateExtract1();
        sound.getMetadata().addListener((MapChangeListener<String, Object>) ch -> {
            if (ch.wasAdded()) {
                if (ch.getKey().equals("image")) {
                    curImg1=(Image)(ch.getValueAdded());
                    mediaImage1.setImage(curImg1);
                    System.out.println(curImg1.getWidth());
                    if(curImg1.getWidth()<550)
                        mediaImage1.setX(100);
                    else if(curImg1.getWidth()<1200)
                        mediaImage1.setX(65);
                    else
                        mediaImage1.setX(0);
                }
            }
        });

        playCount++;
    }

    private void codeDuplicateExtract1() {
        mediaPlayer.currentTimeProperty().addListener(observable -> Platform.runLater(() -> {
            double curTime1 = mediaPlayer.getCurrentTime().toSeconds();
            slider1.setValue((curTime1 * 1000 / tTimeMil) * 100);
            String str1 = String.valueOf((int) curTime1 / 60);
            String str2 = String.valueOf((int) curTime1 % 60);
            timeLbl1.setText(str1 + " : " + str2);
        }));

        curImg1=new Image("beats-music-logo-0121.png");
        mediaImage1.setX(0);
        mediaImage1.setImage(curImg1);
    }

    @FXML
    void playOnlineSong(String address,String id1) throws InterruptedException, IOException {
        sound = new Media(address);
        if(playCount>0)
            mediaPlayer.dispose();

        if (onLnPlCount1 > 0) {
            mediaPlayer.dispose();
            jfxCurButton=(JFXButton)flowPane2.lookup("#"+onLnId);
            jfxCurButton.setStyle("-fx-background-color: #202020");
        }
        jfxCurButton=(JFXButton)flowPane2.lookup("#"+id1);
        String songName=jfxCurButton.getText();
        jfxCurButton.setStyle("-fx-background-color: #3e1c27");
        onLnId=id1;
        songNmLbl1.setText(jfxCurButton.getText());
        extractDuplicate2();

        mediaPlayer.setOnEndOfMedia(() -> {
            try {
                slider1.setValue(0);
                playSong(id0+1,false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        mediaPlayer.setOnPlaying(() -> {
            currenTime001=0;
            if(crntPlSts)
                mediaPlayer.play();
            else
                mediaPlayer.pause();
        });

        codeDuplicateExtract1();
        Database database=new Database();
        Image image=database.imageFind(songName);
        if(image!=null) {
            System.out.println("Image found");
            mediaImage1.setImage(image);
        }

        sound.getMetadata().addListener((MapChangeListener<String, Object>) ch -> {
            if (ch.wasAdded()) {
                if (ch.getKey().equals("image")) {
                    curImg1 = (Image) (ch.getValueAdded());
                    mediaImage1.setImage(curImg1);
                    System.out.println(curImg1.getWidth());
                    if (curImg1.getWidth() < 550) {
                        mediaImage1.setX(80);
                    } else if (curImg1.getWidth() < 1200) {
                        mediaImage1.setX(65);
                    } else {
                        mediaImage1.setX(0);
                    }
                }
            }
        });

        onLnPlCount1++;
    }

    private void extractDuplicate2() {
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setOnReady(() -> {
            tTime1 = sound.getDuration();
            tTimeMil = tTime1.toMillis();
            double tTime2 = tTime1.toSeconds();
            int mnt = (int) tTime2 / 60;
            int sec = (int) tTime2 % 60;
            String minStr0 = String.valueOf(mnt), secStr0 = String.valueOf(sec);
            if (mnt < 10) {
                minStr0 = "0" + mnt;
            }
            if (sec < 10) {
                secStr0 = "0" + sec;
            }
            final String str01 = minStr0 + " : " + secStr0;
            Platform.runLater(() -> timeLbl2.setText(str01));
            mediaPlayer.play();
            mediaPlayer.setVolume(crntVolume1);
        });
    }

    @FXML
    void mediaOnAction() {
        pan1.setVisible(setPan1Visib = !setPan1Visib);
    }

    @FXML
    void sliderAction1() {
        double cPos1 = slider1.getValue();
        mediaPlayer.seek(tTime1.multiply(cPos1).divide(100));
    }

    @FXML
    void nextAction() throws InterruptedException {
        playSong((id0 + 1),false);
    }

    @FXML
    void pauseAction() {
        if (playCount != 0) {
            if (crntPlSts) {
                mediaPlayer.pause();
                pauseBtn.setIcon(PLAY);
                pauseBtn.setGlyphStyle("-fx-size: 2em");
                crntPlSts = false;
            } else {
                mediaPlayer.play();
                pauseBtn.setIcon(PAUSE);
                crntPlSts = true;
            }
        }
    }

    @FXML
    void prevAction() throws InterruptedException {
        playSong((id0 - 1),false);
    }

    @FXML
    void importBtnAction() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("MusicPlayer");
        File defaultDirectory = new File("c:\\");
        chooser.setInitialDirectory(defaultDirectory);
        File selectedDirectory = chooser.showDialog(primaryStage);
        System.out.println(selectedDirectory);
        if(selectedDirectory!=null) {
            File[] listOfFiles1 = selectedDirectory.listFiles();
            importButtons(listOfFiles1, listOfFiles1.length);
        }
    }

    @FXML
    void importButtons(File[] listOfFiles1,int last)
    {
        for (int i = 0; i < last; i++) {
            if (listOfFiles1[i].isFile()) {
                //System.out.println("File " + listOfFiles1[i].getName());
                listOfFiles[i+totalFiles]=listOfFiles1[i];
                JFXButton jfxButton = new JFXButton(listOfFiles1[i].getName());
                jfxButton.setButtonType(JFXButton.ButtonType.RAISED);
                jfxButton.setPrefSize(270, 50);
                jfxButton.setId(String.valueOf(i+totalFiles));
                //System.out.println(i+totalFiles);
                jfxButton.getStyleClass().add("button1");
                jfxButton.setOnAction(event -> {
                    try {
                        int num1=Integer.parseInt(jfxButton.getId());
                        playSong(num1,true);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                flwPan1.getChildren().add(jfxButton);

            } else if (listOfFiles1[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles1[i].getName());
            }
        }
        totalFiles+=last;
        scrlPan1.setContent(flwPan1);
    }

    @FXML
    void volumeAction1() {
        if (playCount > 0) {
            crntVolume1 = volumeSlider.getValue() / 100;
            mediaPlayer.setVolume(crntVolume1);
        }
    }

    @FXML
    void minMzBtnAction1(MouseEvent event) {
        Node source = (Node) event.getSource();
        Stage stage=(Stage)(source.getScene().getWindow());
        stage.setIconified(true);
    }

    @FXML
    void reapeatAction() {
        repeatBtn1.setGlyphStyle("-fx-fill: " + ((repeatOn = !repeatOn) ? "#4f1b2c" : "#87053d"));
    }

    @FXML
    void mute() {
        if (playCount <= 0) return;

        if (mediaPlayer.isMute()) {
            mediaPlayer.setMute(false);
            icnClrChngOff(muteBtn1);
        }
        else {
            mediaPlayer.setMute(true);
            icnClrChngOn(muteBtn1);
        }
    }

    @FXML
    void shuffle() {
        if (suffle1) {
            suffle1=false;
            icnClrChngOff(suffleBtn1);
            System.out.println("YES");
        }
        else {
            suffle1=true;
            icnClrChngOn(suffleBtn1);
            System.out.println("NO");
        }
    }

    @FXML
    void search() {
        if (curSearch) {
            searchBarBtn1.setText("");
            searchAnother();
            return;
        }

        String query = searchBarBtn1.getText();
        if (query.length() > 0) {
            searchBtn.setIcon(CLOSE);
            curSearch=true;
            flowPane2.setStyle("-fx-background-color: #202020");
            flowPane2.setPrefHeight(450);
            for(int i=0;i<totalFiles;i++) {
                String str001=listOfFiles[i].toString().toLowerCase();
                if(str001.contains(query)) {
                    //System.out.println("File " + listOfFiles[i]);
                    JFXButton jfxButton = new JFXButton(listOfFiles[i].getName());
                    jfxButton.setButtonType(JFXButton.ButtonType.RAISED);
                    jfxButton.setPrefSize(270, 50);
                    jfxButton.getStyleClass().add("button1");
                    final int fin=i;
                    jfxButton.setOnAction(event -> {
                        try {
                            searchAnother();
                            playSong(fin, true);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                    flowPane2.getChildren().add(jfxButton);
                }
            }
            scrlPan1.setContent(flowPane2);
        }
    }
    @FXML
    void searchAnother() {
        searchBtn.setIcon(SEARCH);
        scrlPan1.setContent(flwPan1);
        flowPane2.getChildren().clear();
        curSearch=false;
    }

    @FXML
    void icnClrChngOn(FontAwesomeIconView icon0) {
        icon0.setGlyphStyle("-fx-fill: #87053d");
    }

    @FXML
    void icnClrChngOff(FontAwesomeIconView icon0) {
        icon0.setGlyphStyle("-fx-fill: #4f1b2c");
    }

    @FXML
    void onlinePlayAction() {
        if (onlinePlay) {
            onlinePlay=false;
            scrlPan1.setContent(flwPan1);
            return;
        }

        onlinePlay = true;
        Database database1 = new Database();
        flowPane2.getChildren().clear();
        flowPane2.setStyle("-fx-background-color: #202020");
        flowPane2.setPrefHeight(450);
        var arrayList = database1.list();
        var count1=0;
        for (String str001 : arrayList) {
            String[] strArr1=str001.split("77");
            str001=strArr1[0];
            JFXButton jfxButton = new JFXButton(str001);
            jfxButton.setButtonType(JFXButton.ButtonType.RAISED);
            jfxButton.setPrefSize(270, 50);
            jfxButton.getStyleClass().add("button1");
            jfxButton.setId("O"+count1);
            jfxButton.setOnAction(event1 -> {
                try {
                    playOnlineSong(strArr1[1],jfxButton.getId());
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            });
            flowPane2.getChildren().add(jfxButton);
            count1++;
        }
        scrlPan1.setContent(flowPane2);
    }

    @FXML
    void newPageAction(ActionEvent event) throws IOException {

        Parent window1;
        window1 = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("online-view.fxml")));
        window1.setOnMousePressed(event12 -> {
            xPos = event12.getSceneX();
            yPos = event12.getSceneY();
        });

        Stage mainStage;
        mainStage = (Stage)  ((Node)event.getSource()).getScene().getWindow();
        window1.setOnMouseDragged(event1 -> {
            primaryStage.setX(event1.getScreenX() - xPos);
            primaryStage.setY(event1.getScreenY() - yPos);
        });
        Scene scene=new Scene(window1);
        mainStage.setScene(scene);
    }

    MediaPlayer retMePlayer()
    {
        return mediaPlayer;
    }
    Image retImage()
    {
        return curImg1;
    }
    double retTime()
    {
        return tTimeMil;
    }
    Media retMed(){ return sound; }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            initialize();
        } catch (Exception ignored) {
        }
    }

}
