package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.prefs.Preferences;


public class LoginController {
    @FXML
    private Button login_btn;
    @FXML
    private TextField user_txt, pass_txt;

    @FXML
    void create_new_acount(ActionEvent event) throws IOException {
        new ShowWindows(login_btn, "register");
    }

    @FXML
    void login(ActionEvent event) {
        //send data
        //valid
        //message=> ("401","404","200")
        //200=>redirect main fx
        try {
            byte[] buffer = ("Login" + ";" + user_txt.getText() + ";" + pass_txt.getText()).getBytes();
            Client client = new Client();
            String rep = client.Connect(buffer,"Login");
            if (rep.equals("401")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Password Is Not Correct");
                alert.show();
            } else if (rep.equals("404")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("User does not exist");
                alert.show();
            } else {
                int port = Integer.parseInt(rep);
                Preferences preferences = Preferences.systemNodeForPackage(LoginController.class);
                preferences.put("username", user_txt.getText());
                preferences.put("port", String.valueOf(port));
                preferences.flush();
                new ShowWindows(login_btn, "home");
                Thread thread = new Thread(new ServerListen(port));
                thread.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}