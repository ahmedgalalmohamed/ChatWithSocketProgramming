package com.example.demo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.prefs.Preferences;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("login"), 640, 480);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    @Override
    public void stop(){
        Preferences preferences = Preferences.systemNodeForPackage(LoginController.class);
        String user = preferences.get("username","");
        String port = preferences.get("port","");
        byte[] buffer = ("Leave" + ";" + user + ";" + port).getBytes();
        Client client = new Client();
        try {
            String rep = client.Connect(buffer,"Leave");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Save file
    }
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}