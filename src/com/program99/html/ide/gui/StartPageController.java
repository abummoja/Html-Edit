package com.program99.html.ide.gui;

import com.program99.html.ide.MainEditorActivity;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import sample.Main;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

public class StartPageController {

    @FXML
    Pane newPrjBtn;

    @FXML
    CheckBox showStartChBox;

    public void switchToNewProjectPage() throws IOException {
        Stage sc = new Stage();
        new NewProjectPage().start(sc);
        close();
    }

    public void setToFalse(){
        Preferences prefs = Preferences.userRoot().node(Main.class.getName());
        if(showStartChBox.isSelected()){
            prefs.putBoolean("showStart", true);
            System.out.println("set to true"+prefs.name());
        }else{
            prefs.putBoolean("showStart", false);
            System.out.println("set to false"+prefs.name());
        }
    }

    public void openDirPicker(MouseEvent mouseEvent) {
        DirectoryChooser dirPick = new DirectoryChooser();
        dirPick.setTitle("HTML Edit - Choose a folder");
        File selectedFolder = dirPick.showDialog(newPrjBtn.getScene().getWindow());
        if(selectedFolder != null){
            //open the editor
            openEditor(selectedFolder.getAbsolutePath());
            close();
        }else{
            //prjDir.setText("Pick a folder to proceed.");
        }
    }
    public void close(){
        Stage p = (Stage)newPrjBtn.getScene().getWindow();
        p.close();
    }
    public void openEditor(String projectPath){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main-editor-ui.fxml"));
            Scene editorScene = new Scene(loader.load());
            MainEditorController controller = loader.getController();
            controller.setCurrentProject(projectPath);
            Stage st = new Stage();
            st.setTitle("HTML Edit");
            st.setScene(editorScene);
            st.show();
            Stage p = (Stage)newPrjBtn.getScene().getWindow();
            p.close();
            //new MainEditorActivity().start(st);
        }catch (IOException ioe){
            System.out.println("Failed to open project.");
        }
    }
}
