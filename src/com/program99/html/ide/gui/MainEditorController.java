package com.program99.html.ide.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;

//import java.awt.*;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFilePermission;
import java.util.*;
import java.util.regex.Pattern;

public class MainEditorController {

    public TextArea propertiesText;
    public TreeView documentTreeView;
    @FXML
    TabPane tabHolder;
    @FXML
    Button testButton, lBtn;
    @FXML
    ToolBar topBar;
    public String prjPath;
    @FXML
    TreeView projectFiles;

    String fp = "";

    @FXML
    private void initialize() {
        ContextMenu tabContextMenu = new ContextMenu();

        // Create menu items for the context menu
        MenuItem closeMenuItem = new MenuItem("Close");
        MenuItem closeOthersMenuItem = new MenuItem("Close Others");
        MenuItem closeAllMenuItem = new MenuItem("Close All");

        // Set event handlers for menu items
        closeMenuItem.setOnAction(event -> closeSelectedTab());
        closeOthersMenuItem.setOnAction(event -> closeOtherTabs());
        closeAllMenuItem.setOnAction(event -> closeAllTabs());

        // Add menu items to the context menu
        tabContextMenu.getItems().addAll(closeMenuItem, closeOthersMenuItem, closeAllMenuItem);

        // Set the context menu for the tab pane
        tabHolder.setContextMenu(tabContextMenu);
        System.out.println(prjPath);

        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem1 = new MenuItem("New");
        MenuItem menuItem2 = new MenuItem("Copy Path");
        MenuItem menuItem3 = new MenuItem("Properties");
        MenuItem menuItem4 = new MenuItem("Delete");
        MenuItem[] mnuitems = {menuItem1, menuItem2, menuItem3, menuItem4};
        contextMenu.getItems().addAll(mnuitems);


        ContextMenu htmlContextMenu = new ContextMenu();
        MenuItem htmlItem0 = new MenuItem("Open");
        MenuItem htmlItem1 = new MenuItem("Copy path");
        MenuItem htmlItem2 = new MenuItem("Properties");
        SeparatorMenuItem sep = new SeparatorMenuItem();
        MenuItem htmlItem3 = new MenuItem("Delete");
        MenuItem[] htmlItems = {htmlItem0, htmlItem1, htmlItem2, sep, htmlItem3};
        htmlContextMenu.getItems().addAll(htmlItems);

        projectFiles.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
               CustomTreeItem selectedItem = (CustomTreeItem) projectFiles.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    //contextMenu.show(projectFiles, event.getScreenX(), event.getScreenY());
                    if(!selectedItem.isLeaf()){
                        contextMenu.show(projectFiles, event.getScreenX(), event.getScreenY());
                    }else{
                       // File fselectedItemText = new File(selectedItem.getValue().toString());
                        String selectedItemText = selectedItem.getTag();
                        if(selectedItemText.endsWith(".html")){
                            //HTML Context menu
                            MenuItem htmlAnalyzeItem = new MenuItem("Analyze HTML");
                            htmlContextMenu.getItems().clear();
                            htmlContextMenu.getItems().add(0, htmlAnalyzeItem);
                            htmlContextMenu.getItems().addAll(htmlItems);
                        }else if(selectedItemText.endsWith(".css")){
                            //CSS
                            MenuItem htmlAnalyzeItem = new MenuItem("Analyze CSS");
                            htmlContextMenu.getItems().clear();
                            htmlContextMenu.getItems().addAll(htmlItems);
                            htmlContextMenu.getItems().add(0, htmlAnalyzeItem);
                        }else if(selectedItemText.endsWith(".js")){
                            //Show JS CONTEXT MENU
                            MenuItem htmlAnalyzeItem = new MenuItem("Analyze JS");
                            htmlContextMenu.getItems().clear();
                            htmlContextMenu.getItems().addAll(htmlItems);
                            htmlContextMenu.getItems().add(0, htmlAnalyzeItem);
                        }else{
                            htmlContextMenu.show(projectFiles, event.getScreenX(), event.getScreenY());
                        }
                        htmlContextMenu.show(projectFiles, event.getScreenX(), event.getScreenY());
                    }
                }
            }else{
                CustomTreeItem selectedItem = (CustomTreeItem) projectFiles.getSelectionModel().getSelectedItem();
                if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2){
                    if(selectedItem != null&&selectedItem.isLeaf()){
                        //System.out.println("Selected: "+selectedItem.getValue());
                            //File mselectedItemText = new File(selectedItem.getValue().toString());
                              //check if is open & open
                                try {
                                    System.out.println(selectedItem.getTag());
                                    readFile(selectedItem.getTag(), selectedItem.getType());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                    }
                }else if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1){
                    if(selectedItem != null&&selectedItem.isLeaf()){
                        //it is a file
                        File selectedFile = new File(selectedItem.getTag());
                        String fileName = selectedItem.getTitle();
                        String filePath = selectedItem.getTag();
                        Path path = Paths.get(filePath);
                        BasicFileAttributes attributes = null;
                        try {
                            attributes = Files.readAttributes(path, BasicFileAttributes.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        assert attributes != null;
                        FileTime creationTime = attributes.creationTime();
                        long fileSize = selectedFile.length();
                        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
                        long lastModified = selectedFile.lastModified();
                        Date lastModifiedDate = new Date(lastModified);
                        boolean isReadable = selectedFile.canRead();
                        boolean isWritable = selectedFile.canWrite();
                        boolean isExecutable = selectedFile.canExecute();
                        String parentDirectory = selectedFile.getParent();
                        boolean isLink = attributes.isSymbolicLink();
                        String fileProperties = "Name: "+ fileName +"\nType: "+selectedItem.getType()+ "\nPath: "+ filePath + "\nDate created: "+ creationTime + "\nSize: "+ fileSize + "\nExtension: "+ fileExtension
                                 + "\nLast modified: " + lastModifiedDate + "\nReadable: "+ isReadable + "\nWritable: " + isWritable + "\nExecutable: " + isExecutable + "\nParent: "+ parentDirectory + "\nIs Link: "+ isLink;
                        propertiesText.setText(fileProperties);
                    }else  if(selectedItem != null&&!selectedItem.isLeaf()){
                        //is folder
                        File selectedFile = new File(selectedItem.getTag());
                        String fileName = selectedItem.getTitle();
                        String filePath = selectedItem.getTag();
                        Path path = Paths.get(filePath);
                        BasicFileAttributes attributes = null;
                        try {
                            attributes = Files.readAttributes(path, BasicFileAttributes.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        assert attributes != null;
                        FileTime creationTime = attributes.creationTime();
                        String parentDirectory = selectedFile.getParent();
                        boolean isLink = attributes.isSymbolicLink();
                        int items = Objects.requireNonNull(selectedFile.listFiles()).length;
                        String fileProperties = "Name: "+ fileName + "\nPath: "+ filePath + "\nDate created: "+ creationTime + "\nContains: "+ items+" items."
                               + "\nParent: "+ parentDirectory + "\nIs Link: "+ isLink;
                        propertiesText.setText(fileProperties);
                    }
                }
            }
        });

    }

    public void addTab(String title, String path) {
        Tab newTab = new Tab(title);
        newTab.setId(path);
        Tooltip toolTip = new Tooltip(path);
        newTab.setTooltip(toolTip);
        tabHolder.getTabs().add(newTab);
    }

    public void tESTcLICKlISTENER() {
        String ttl = "A new Tab";
        String desc = prjPath;
        addTab(ttl, desc);
    }

    public void toggleToolBar() {
        if (topBar.isVisible()) {
            topBar.setVisible(false);
            topBar.setPrefHeight(0);
        } else {
            topBar.setVisible(true);
        }
    }

    public void setCurrentProject(String currDir) throws IOException {
        this.prjPath = currDir;
        System.out.println(prjPath + "2");
        try {
            //loadTree(currDir);
            loadFiles(currDir);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public String readFile(String filePathRead, String type) throws IOException {
        Path p1 = Paths.get(filePathRead);
        // BufferedReader reader = new BufferedReader(new FileReader(filePathRead));
        String fileContent = "";
        if(type.equals("Markup Language")||type.equals("Plain Text")){
            try {
                Scanner scanner = new Scanner(new File(filePathRead));
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    fileContent = fileContent + "\n" + line;
                    //do stuff
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        File theFile = new File(filePathRead);
        Tab nT = new Tab(theFile.getName());

        switch(type){
            case "Plain Text":
                TextArea tA = new TextArea();
                tA.setText(fileContent);
                tA.setFont(Font.font("monospaced"));
                tA.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!nT.getText().endsWith("*")) {
                        nT.setText(nT.getText() + "*");
                    }
                });
                nT.setContent(tA);
                break;
            case "Markup Language":
                //create the content for mark up editor.
                //FXMLLoader loader = new FXMLLoader();
                MarkupEditorController.content = fileContent;
                try{
                    Parent markupParent = FXMLLoader.load(getClass().getResource("markup-editor.fxml"));
                    nT.setContent(markupParent);
                }catch (IOException ioe){
                    System.out.println("Error "+ioe);
                }
                break;
            case "Picture":
                //FXMLLoader loader = new FXMLLoader();
                ImageViewerController.path = theFile.getPath();
                    AnchorPane imageParent = FXMLLoader.load(getClass().getResource("image-viewer.fxml"));
                    nT.setContent(imageParent);
                break;
            case "Audio":
                AudioPlayerController.path = filePathRead;
                    AnchorPane audioParent = FXMLLoader.load(getClass().getResource("audio-player.fxml"));
                    nT.setContent(audioParent);
                break;
        }
        Tooltip toolTip = new Tooltip(theFile.getPath());
        fp = theFile.getPath();
        nT.setTooltip(toolTip);
       // nT.setGraphic(itm.getGraphic());

            tabHolder.getTabs().add(nT);

        return fileContent;
    }

    private void closeSelectedTab() {
        Tab selectedTab = tabHolder.getSelectionModel().getSelectedItem();
        if (selectedTab != null) {
            tabHolder.getTabs().remove(selectedTab);
        }
    }

    private void closeOtherTabs() {
        Tab selectedTab = tabHolder.getSelectionModel().getSelectedItem();
        if (selectedTab != null) {
            tabHolder.getTabs().retainAll(selectedTab);
        }
    }

    private void closeAllTabs() {
        tabHolder.getTabs().clear();
    }

    public void loadFiles(String path) {
        this.prjPath = path;
        File directory = new File(path);
        CustomTreeItem rootItem = new CustomTreeItem(directory.getName(), directory.getAbsolutePath());
        rootItem.setExpanded(true);
        populateTreeView(directory, rootItem);
        projectFiles.setRoot(rootItem);
        projectFiles.setShowRoot(true);

    }

    private void populateTreeView(File directory, CustomTreeItem rootItem) {
        for (File file : directory.listFiles()) {
           // TreeItem<File> item = new TreeItem<>(file);
            CustomTreeItem customTreeItem = new CustomTreeItem(file.getName(), file.getAbsolutePath());
           // ImageView fileImage = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("com/program99/html/ide/gui/file.png"))));
            Text folderIcon = new Text("\uE188");
            folderIcon.setFont(Font.font("Segoe MDL2 Assets"));
            folderIcon.setFill(Color.BROWN);
            Text fileIcon = new Text(":>>");
            customTreeItem.setGraphic(fileIcon);
            if (file.isDirectory()) {
                customTreeItem.setGraphic(folderIcon);
                // System.out.println("Dir >>> "+file.getName());
                populateTreeView(file, customTreeItem);
            } else if (file.getName().endsWith(".html")) {
                customTreeItem.setGraphic(fileIcon);
                fileIcon.setText("</>");
                fileIcon.setFill(Color.ORANGE);
            } else if (file.getName().endsWith(".css")) {
                customTreeItem.setGraphic(fileIcon);
                fileIcon.setText("3");
                fileIcon.setFill(Color.BLUE);
            } else if (file.getName().endsWith(".js")) {
                customTreeItem.setGraphic(fileIcon);
                fileIcon.setText("JS");
                fileIcon.setFill(Color.YELLOW);
            }else{
                fileIcon.setText("\uE132");
                fileIcon.setFill(Color.DODGERBLUE);
                fileIcon.setFont(Font.font("Segoe MDL2 Assets"));
            }
            rootItem.getChildren().add(customTreeItem);
        }

    }

    public void showProjectChooser() throws IOException {
        DirectoryChooser dirPick = new DirectoryChooser();
        dirPick.setTitle("HTML Edit - Select project folder");
        File selectedFolder = dirPick.showDialog(tabHolder.getScene().getWindow());
        if(selectedFolder != null){
            try{
                loadFiles(selectedFolder.getAbsolutePath());
                System.out.println(selectedFolder.getAbsolutePath()+" is selected.");
            }catch(Exception e){
                System.out.println("err");
            }
        }else{
            System.out.println("No project selected.");
        }
    }

    class CustomTreeItem extends TreeItem{
        String title, tag;
        String audex = "regex:.*(?i:mp3|ogg|avi|wav|flacc|aud|flp)";
        String videx = "regex:.*(?i:mp4|mkv|webm|ts|wmp|mov)";
        String picex = "regex:.*(?i:jpg|jpeg|png|gif|bmp|jpe|jfif)";
        String archex = "regex:.*(?i:zip|rar|7z|aar|jar|gz|tar)";
        String appex = "regex:.*(?i:exe|com|apk|bat|msi|iso|app|sh)";
        String markupex = "regex:.*(?i:ecgml|xslt|xsl|rss|bml|hwpml|uoml|gml|kml|mml|cml|sbml|se|eml|ssml|ps|kprx|atom|xrb|mhtml|html|mht|xml|xhtml|xht|maff|dtd|shtml|stm|ihtml)";
        String phpex = "regex:.*(?i:php|phtml)";
        String jsonex = "regex:.*(?i:json|jsonld)";
        String plaintxtex = "regex:.*(?i:txt|log)";
        String scriptex = "regex:.*(?i:js|ts|vb)";
        String cssext = "regex:.*(?i:css|scss)";

        public CustomTreeItem(String title, String tag){
            this.title = title;
            this.tag = tag;
            this.setValue(title);
        }

        public String getTitle(){
            return this.title;
        }

        public String getTag(){
            return this.tag;
        }

        public String getType(){
            String fileExtension = title.substring(title.lastIndexOf(".") + 1).toLowerCase();
            String
                    type = null;
            //Pattern pattern = Pattern.compile(markupex);
            Path path = Paths.get(tag);
            if(FileSystems.getDefault().getPathMatcher(markupex).matches(path)){
                type = "Markup Language";
            }else if(FileSystems.getDefault().getPathMatcher(phpex).matches(path)){
                type = "PHP";
            }else if(FileSystems.getDefault().getPathMatcher(jsonex).matches(path)){
                type = "JSON";
            }else if(FileSystems.getDefault().getPathMatcher(videx).matches(path)){
                type = "Video";
            }else if(FileSystems.getDefault().getPathMatcher(picex).matches(path)){
                type = "Picture";
            }else if(FileSystems.getDefault().getPathMatcher(audex).matches(path)){
                type = "Audio";
            }else if(FileSystems.getDefault().getPathMatcher(archex).matches(path)){
                type = "Archive";
            }else if(FileSystems.getDefault().getPathMatcher(appex).matches(path)){
                type = "Executable";
            }else if(FileSystems.getDefault().getPathMatcher(plaintxtex).matches(path)) {
                type = "Plain Text";
            }else if(FileSystems.getDefault().getPathMatcher(cssext).matches(path)){
                type = "Stylesheet";
            }else if(FileSystems.getDefault().getPathMatcher(scriptex).matches(path)){
                type = "Script";
            }
            return type;
        }

    }
}
