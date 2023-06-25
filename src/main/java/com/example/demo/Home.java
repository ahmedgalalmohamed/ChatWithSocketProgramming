package com.example.demo;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Home implements Initializable {
    static final GetMessage mymessage = new GetMessage();

    @FXML
    public TextField msg_txt;
    static String msg = "";
    public TextArea ms_txt;
    public ObservableList my_groups_list = FXCollections.observableArrayList();
    public ListView<String> my_groups_listview;
    public ObservableList contacts_list = FXCollections.observableArrayList();
    public ListView<String> contacts_listview;
    public ObservableList all_groups_list = FXCollections.observableArrayList();
    public ListView<String> all_groups_listview;
    public ObservableList users_list = FXCollections.observableArrayList();
    @FXML
    public ListView<String> users_listview;

    public Text chat_name_txt;
    public TabPane tab_container;
    public Button btn_send;
    static String state = "";
    Preferences preferences = Preferences.systemNodeForPackage(LoginController.class);
    String username = preferences.get("username", "");


    public void get_contacts(Event event) throws IOException {
        contacts_listview.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        state = "contact&";
        byte[] buffer = ("get_contact" + ";" + username + ";" + state).getBytes();
        Client client = new Client();
        String rep = client.Connect(buffer, "get_contact");
        String[] users = rep.split(";");
        Insets s = new Insets(10);
        contacts_list.clear();
        if (users[0].isEmpty()) return;
        for (String user : users) {
            Text text = new Text();
            VBox vbox = new VBox();
            text.setText(user);
            vbox.setPadding(s);
            vbox.getChildren().add(text);
            contacts_list.add(vbox);
        }
        contacts_listview.setItems(contacts_list);
        visitable_off();
    }

    public void get_my_group(Event event) throws IOException {
        state = "my_group&";
        byte[] buffer = ("get_my_groups" + ";" + username + ";" + state).getBytes();
        Client client = new Client();
        String rep = client.Connect(buffer, "get_my_groups");
        String[] groups = rep.split(";");
        Insets s = new Insets(10);
        my_groups_list.clear();
        if (groups[0].isEmpty()) return;
        for (String group : groups) {
            Text text = new Text();
            VBox vbox = new VBox();
            text.setText(group);
            vbox.setPadding(s);
            vbox.getChildren().add(text);
            my_groups_list.add(vbox);
        }
        my_groups_listview.setItems(my_groups_list);
        visitable_off();
    }


    public void get_users(Event event) throws IOException {
        state = "user&";
        byte[] buffer = ("get_user" + ";" + username + ";" + state).getBytes();
        Client client = new Client();
        String rep = client.Connect(buffer, "get_user");
        String[] users = rep.split(";");
        Insets s = new Insets(10);
        users_list.clear();
        if (users[0].isEmpty()) return;
        for (String user : users) {
            Text text = new Text();
            VBox vbox = new VBox();
            text.setText(user);
            vbox.setPadding(s);
            vbox.getChildren().add(text);
            users_list.add(vbox);
        }
        users_listview.setItems(users_list);
        visitable_off();
    }

    public void get_all_group(Event event) throws IOException {
        state = "all_group&";
        byte[] buffer = ("get_all_groups" + ";" + username + ";" + state).getBytes();
        Client client = new Client();
        String rep = client.Connect(buffer, "get_all_groups");
        String[] groups = rep.split(";");
        Insets s = new Insets(10);
        all_groups_list.clear();
        if (groups[0].isEmpty()) return;
        for (String group : groups) {
            Text text = new Text();
            VBox vbox = new VBox();
            text.setText(group);
            vbox.setPadding(s);
            vbox.getChildren().add(text);
            all_groups_list.add(vbox);
        }
        all_groups_listview.setItems(all_groups_list);
        visitable_off();
    }

    public void send_msg(ActionEvent actionEvent) {
        String current_tab = tab_container.getSelectionModel().getSelectedItem().getText();
        ObservableList<Integer> indices = contacts_listview.getSelectionModel().getSelectedIndices();
        String receiver = chat_name_txt.getText();
        String type = "";
        if (!receiver.equals("Chat name") && !receiver.isEmpty()) {
            byte[] buffer = new byte[1024];
            if (current_tab.equals("Contacts")) {
                type = "chat";
                if (indices.size() <= 1) {
                    buffer = ("chat_contact" + ";" + msg_txt.getText() + ";" + username + ";" + receiver + ";" + username + ";" + state).getBytes();
                } else {
                    receiver = "";
                    for (int index : indices) {
                        VBox container = (VBox) contacts_list.get(index);
                        Text t = (Text) container.getChildren().get(0);
                        receiver += t.getText() + "&";
                    }
                    buffer = ("chat_mul_contacts" + ";" + msg_txt.getText() + ";" + username + ";" + receiver + ";" + username + ";" + state).getBytes();
                }
            } else if (current_tab.equals("My Groups")) {
                type = "chat_group";
                buffer = ("chat_group" + ";" + msg_txt.getText() + ";" + username + ";" + receiver + ";" + username + ";" + state).getBytes();
            }
            Client client = new Client();
            try {
                String rep = client.Connect(buffer, type);
                String[] res = rep.split(";");
                if (res.length > 1 && res[1].equals("1")) {
                    visitable_off();
                    int index = my_groups_listview.getSelectionModel().getSelectedIndex();
                    my_groups_listview.getItems().remove(index);
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("You have been kicked from " + res[0]);
                    alert.show();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            msg_txt.setText("");
        }

    }

    public void getMessage(String Message) {
        msg = Message;
        String rand = String.valueOf(Math.random() * (1000000000 - 1) + 1);
        mymessage.setMessage(rand);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mymessage.messageProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                try {
                    ms_txt.setText(msg);
                    ms_txt.setScrollTop(Double.MAX_VALUE);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
    }

    public void get_user_name(MouseEvent mouseEvent) {
        int index = users_listview.getSelectionModel().getSelectedIndex();
        if (index == -1) return;
        VBox container = (VBox) users_list.get(index);
        Text t = (Text) container.getChildren().get(0);
        state = "user&" + t.getText();
        chat_name_txt.setText("Chat name");
        ms_txt.setText("");
        msg_txt.setDisable(true);
        btn_send.setDisable(true);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are Sure Add " + t.getText() + " as Friend");
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                byte[] buffer = ("add_contact" + ";" + username + ";" + t.getText() + ";" + username + ";" + state).getBytes();
                Client client = new Client();
                try {
                    String rep = client.Connect(buffer, "add_contact");
                    if (rep.equals("200")) {
                        users_listview.getItems().remove(index);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void get_all_group_event(MouseEvent mouseEvent) {
        int index = all_groups_listview.getSelectionModel().getSelectedIndex();
        if (index == -1) return;
        VBox container = (VBox) all_groups_list.get(index);
        Text t = (Text) container.getChildren().get(0);
        state = "all_group&" + t.getText();
        chat_name_txt.setText("Chat name");
        ms_txt.setText("");
        btn_send.setDisable(true);
        msg_txt.setDisable(true);
        //join_group
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to join " + t.getText() + " group");
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                byte[] buffer = ("join_group" + ";" + username + ";" + t.getText() + ";" + username + ";" + state).getBytes();
                Client client = new Client();
                try {
                    String rep = client.Connect(buffer, "join_group");
                    if (rep.equals("200")) {
                        all_groups_listview.getItems().remove(index);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void get_contact_name(MouseEvent mouseEvent) throws IOException {
        btn_send.setDisable(false);
        msg_txt.setDisable(false);
        int index = contacts_listview.getSelectionModel().getSelectedIndex();
        if (index == -1) return;
        VBox container = (VBox) contacts_list.get(index);
        Text t = (Text) container.getChildren().get(0);
        chat_name_txt.setText(t.getText());
        String receiver = t.getText();
        byte[] buffer = new byte[1024];
        state = "contact&" + t.getText();
        buffer = ("get_message_contacts" + ";" + username + ";" + receiver + ";" + username + ";" + state).getBytes();
        Client client = new Client();
        client.Connect(buffer, "chat");
    }

    public void get_my_group_event(MouseEvent mouseEvent) throws IOException {
        btn_send.setDisable(false);
        msg_txt.setDisable(false);
        int index = my_groups_listview.getSelectionModel().getSelectedIndex();
        if (index == -1) return;
        VBox container = (VBox) my_groups_list.get(index);
        Text t = (Text) container.getChildren().get(0);
        chat_name_txt.setText(t.getText());
        String receiver = t.getText();
        byte[] buffer;
        state = "my_group&" + t.getText();
        buffer = ("get_message_groups" + ";" + receiver + ";" + username + ";" + state).getBytes();
        Client client = new Client();
        client.Connect(buffer, "chat");
    }

    public void visitable_off() {
        getMessage("");
        if (chat_name_txt != null)
            chat_name_txt.setText("Chat name");
        if (btn_send != null)
            btn_send.setDisable(true);
        if (msg_txt != null)
            msg_txt.setDisable(true);
    }


    public void logout(ActionEvent actionEvent) throws BackingStoreException {
        Preferences preferences = Preferences.systemNodeForPackage(LoginController.class);
        String user = preferences.get("username", "");
        String port = preferences.get("port", "");
        byte[] buffer = ("Leave" + ";" + user + ";" + port + ";" + username + ";" + state).getBytes();
        Client client = new Client();
        try {
            String rep = client.Connect(buffer, "Leave");
            System.out.println(preferences.get("username", ""));
            preferences.remove("username");
            preferences.remove("port");
            preferences.flush();
            System.out.println(preferences.get("username", ""));
            new ShowWindows(btn_send, "login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void create_group(ActionEvent actionEvent) {
        TextInputDialog td = new TextInputDialog();
        td.setTitle("Create group");
        td.showAndWait().ifPresent(re -> {
            if (!re.trim().isEmpty()) {
                byte[] buffer = ("create_group" + ";" + username + ";" + re + ";" + username + ";" + state).getBytes();
                Client client = new Client();
                try {
                    String rep = client.Connect(buffer, "join_group");
                    if (rep.equals("200")) {
                        Insets s = new Insets(10);
                        Text text = new Text();
                        VBox vbox = new VBox();
                        text.setText(re);
                        vbox.setPadding(s);
                        vbox.getChildren().add(text);
                        my_groups_list.add(vbox);
                    }
                    else{
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("This group does exist");
                        alert.show();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
