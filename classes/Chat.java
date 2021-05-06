package classes;

import java.util.ArrayList;

public class Chat {
    private String sender;
    private String receiver;
    private ArrayList<Message> chat;

    public Chat(String sender, String receiver, ArrayList<Message> chat) {
        this.sender = sender;
        this.receiver = receiver;
        this.chat = chat;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public ArrayList<Message> getChat() {
        return chat;
    }

    public void setChat(ArrayList<Message> chat) {
        this.chat = chat;
    }
}
