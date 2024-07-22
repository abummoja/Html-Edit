package com.program99.html.ide.gui;

import com.program99.html.ide.MainEditorActivity;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import javax.management.Notification;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class NewPrjController {

    @FXML
    Button createBtn;
    @FXML
    Button cancelBtn;
    @FXML
    Button addLibBtn;
    @FXML Button prjDirBtn;
    @FXML TextField prjName;
    @FXML TextField prjDir;
    @FXML TextField prjCSS;
    @FXML TextField prjLib;
    @FXML TextField prjJS;
    @FXML
    Pane pleaseWaitPane;
    @FXML
    TextFlow nprjNotif;
    String originalPrjDir = "";
    String prjPath;

    public void createPrj() throws IOException {
        String name = prjDir.getText()+"\\"+prjName.getText();
        prjPath = name;
        String js = prjJS.getText();
        String css = prjCSS.getText();
        //String html = "index.html";
        if(!js.equals("")){
        if(!js.endsWith(".js")){
            js = js+".js";
        }}else {
            js = "index.js";
        }
        if(!css.equals("")){
        if(!css.endsWith(".css")){
            css = css+".css";
        }}else {
            css = "index.css";
        }
        File prDir = new File(prjDir.getText().toString());
        if(name != " " &&prDir.exists()&&prDir.isDirectory()) {
            createProject(name, js, css);
           // Stage st = new Stage();
            //new MainEditorActivity().start(st);
            openEditor(prjPath);
        }else{
            warnUser();
            //prompt user to enter project name.
        }
    }

    public void createProject(String projectName, String jsFileName, String cssFileName) {
        try {
            // Create the project directory
            Path projectPath = Paths.get(projectName);
            Files.createDirectory(projectPath);


            // Create the index.html file
            Path htmFilePath = projectPath.resolve("index.html");
            //Files.createFile(htmFilePath);
           try{
               BufferedWriter htmlwriter = new BufferedWriter(new FileWriter(htmFilePath.toString()));
               htmlwriter.write("<html>\n\t<head>\n\t<title>"+prjName.getText()+"</title>\n\t</head>\n<body>\n\t\t<p>"+prjName.getText()+"</p>\n</body>\n</html>");
               htmlwriter.close();
           }catch(Exception e){
               System.out.println("html-err: "+e.getMessage());
           }
            prjLib.setText(htmFilePath.toString());
            // Create the index.js file
            Path jsFilePath = projectPath.resolve(jsFileName);
            Files.createFile(jsFilePath);

            // Create the styles.css file
            Path cssFilePath = projectPath.resolve(cssFileName);
            Files.createFile(cssFilePath);

            System.out.println("Project created successfully!");
        } catch (IOException e) {
            System.err.println("Error creating project: " + e.getMessage());
        }
    }

    public void warnUser(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Unspecified location.");
        alert.setContentText("Please specify a project folder that exists to continue.");
        alert.showAndWait();
    }

    public void openFileChooser(){
        DirectoryChooser dirPick = new DirectoryChooser();
        dirPick.setTitle("HTML Edit - Choose a folder");
        File selectedFolder = dirPick.showDialog(prjDir.getScene().getWindow());
        if(selectedFolder != null){
            prjDir.setText(selectedFolder.getAbsolutePath());
            originalPrjDir = selectedFolder.getAbsolutePath();
        }else{
            prjDir.setText("Pick a folder to proceed.");
        }
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
            Stage p = (Stage)createBtn.getScene().getWindow();
            p.close();
            //new MainEditorActivity().start(st);
        }catch (IOException ioe){
            System.out.println("Failed to open project.");
        }
    }
}
