package com.program99.html.ide.gui;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

public class ImageViewerController {
    public ImageView imageViewerImageView;
    public AnchorPane imageViewParent;
    public static String path;

    public void initialize() throws MalformedURLException, URISyntaxException {
        //imagePathTxt.setText(path);
        Alert al = new Alert(Alert.AlertType.INFORMATION);
        al.setContentText("Viewing "+path);
        al.setTitle("Image Viewer");
        al.setHeaderText("Path of the image");
        al.showAndWait();
        imageViewerImageView.prefHeight(imageViewParent.getPrefHeight());
        imageViewerImageView.prefWidth(imageViewParent.getPrefWidth());
       // imageViewerImageView.setImage(new Image(getClass().getResource()));
        //Image img = new Image(path);
       // imageViewerImageView.setImage(img);
       // imageViewerImageView.setSmooth(true);
    }
}
