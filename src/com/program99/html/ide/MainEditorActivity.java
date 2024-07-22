package com.program99.html.ide;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class MainEditorActivity extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("gui/main-editor-ui.fxml"));
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("HTML Edit - Web Creator IDE");
        primaryStage.setResizable(true);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.setScene(new Scene(root, 900, 700));
        primaryStage.show();
        //primaryStage.setOnCloseRequest((EventHandler<WindowEvent>) new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST));

    }

    private void beforeClose() {
    }


}
