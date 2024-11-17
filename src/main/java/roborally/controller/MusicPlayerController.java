package dtu.roborally.controller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

public class MusicPlayerController {
    private MediaPlayer mediaPlayer;

    public MusicPlayerController(String musicFilePath) {
        Media media = new Media(new File(musicFilePath).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    public void play() {
        mediaPlayer.play();
    }
}
