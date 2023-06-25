package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

public class RegisterController {
    @FXML
    private Button register_btn;
    @FXML
    private TextField user_txt, pass_txt, name_txt;

    public void loginpage(ActionEvent actionEvent) throws IOException {
        new ShowWindows(register_btn, "login");
    }

    public void register(ActionEvent actionEvent) {
        //send data
        //valid
        //message=> ("401","200")
        //store
        //200=>redirect main fx
        try {
            byte[] buffer = ("Register" + ";" + name_txt.getText() + ";" + user_txt.getText() + ";" + pass_txt.getText()).getBytes();
            Client client = new Client();
            String rep = client.Connect(buffer,"Register");
            if (rep.equals("505")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("User does already exist");
                alert.show();
            } else{
                new ShowWindows(register_btn, "login");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
