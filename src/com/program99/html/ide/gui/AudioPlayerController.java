package com.program99.html.ide.gui;

import jaco.mp3.player.MP3Player;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.File;

public class AudioPlayerController {
    public Text audioPlayerTitle;
    public Slider audioPlayerSeekBar;
    public Button audioPlayerButton;
    MP3Player player = new MP3Player();

    public static String path;
    public void initialize(){
        File audioFile = new File(path);
        audioPlayerTitle.setFill(Color.DODGERBLUE);
        audioPlayerTitle.setText(audioFile.getName());
        player.addToPlayList(audioFile);
        player.play();
    }

    public void playerControl(MouseEvent mouseEvent) {
        if(player.isPaused()){
            player.play();
        }else{
            player.pause();
        }
    }
}
