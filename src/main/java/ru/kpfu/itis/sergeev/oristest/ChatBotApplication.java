package ru.kpfu.itis.sergeev.oristest;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChatBotApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("chat-bot.fxml"));
        AnchorPane root = loader.load();

        loader.getController();

        primaryStage.setTitle("Chat Bot");
        primaryStage.setScene(new Scene(root, 1000, 350));
        primaryStage.show();
    }
}