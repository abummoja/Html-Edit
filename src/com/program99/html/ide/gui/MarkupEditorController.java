package com.program99.html.ide.gui;

import javafx.scene.control.TextArea;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;

public class MarkupEditorController {
    public TextArea mlEditorTextArea;
    public HTMLEditor mlEditorLiveEditor;
    public WebView mlEditorPreview;
    public static String path;
    public static String content;

    public void initialize(){
        mlEditorTextArea.setText(content);
        mlEditorLiveEditor.setHtmlText(mlEditorTextArea.getText());
        mlEditorPreview.getEngine().loadContent(mlEditorTextArea.getText());
       // System.out.println(content);
    }

    public void setContentTo(String content, String path){
        MarkupEditorController.path = path;
        MarkupEditorController.content = content;
        mlEditorTextArea.setText(content);
        mlEditorLiveEditor.setHtmlText(mlEditorTextArea.getText());
        mlEditorPreview.getEngine().loadContent(mlEditorTextArea.getText());
    }
}
