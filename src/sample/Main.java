package sample;

import com.program99.html.ide.MainEditorActivity;
import com.program99.html.ide.StartPage;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.prefs.Preferences;

public class Main extends Application {

    @FXML AnchorPane rootAnchorPane;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        //primaryStage.setTitle("Web Creator IDE");
        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.setScene(new Scene(root, 637, 353));
        primaryStage.show();
        Preferences prefs = Preferences.userRoot().node(this.getClass().getName());
        PauseTransition splashPause = new PauseTransition(Duration.millis(5000));
        splashPause.setOnFinished(event -> {
            // switch to main scene here
            if(!prefs.getBoolean("showStart", true)){
                System.out.println("showstart = false"+prefs.name());
                try {
                    Stage stg = new Stage();
                    new MainEditorActivity().start(stg);
                    primaryStage.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                System.out.println("showstart = true"+prefs.name());
                Stage stg = new Stage();
                try {
                    new StartPage().start(stg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                primaryStage.close();
            }

        });
        splashPause.play();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
