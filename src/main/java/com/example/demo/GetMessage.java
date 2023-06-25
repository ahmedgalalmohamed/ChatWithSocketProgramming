package com.example.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class GetMessage {
    private StringProperty message;

    public final String getMessage() {
        if (message != null) return message.get();
        return "";
    }

    public final void setMessage(String message) {
        this.messageProperty().set(message);
    }

    public final StringProperty messageProperty() {
        if (message == null) {
            message = new SimpleStringProperty("");
        }
        return message;
    }
}
