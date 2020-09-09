package io.yassinefarich.test.beans;

public class Message {
    private String message;
    private int timeToLive;
    private Player emitter;

    public static Message aNew(Player emitter, String messageText, int timeToLive) {
        Message message = new Message();
        message.message = messageText;
        message.timeToLive = timeToLive;
        message.emitter = emitter;
        return message;
    }

    public static Message parseFrom(String messageText) {
        String[] parts = messageText.split("_");
        Message message = new Message();

        message.message = parts[1];
        message.timeToLive = Integer.parseInt(parts[2]);
        message.emitter = Player.aNew(parts[0]).build();

        return message;
    }

    public static Message fromMessage(Message oldMessage, Player emitter) {
        Message newMessage = new Message();
        newMessage.message = oldMessage.message;
        newMessage.emitter = emitter;
        newMessage.timeToLive = oldMessage.timeToLive - 1;
        return newMessage;
    }

    @Override
    public String toString() {
        return message;
    }

    public boolean isNotExpired() {
        return timeToLive > 0;
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    public String getMessage() {
        return message;
    }

    public Player getEmitter() {
        return emitter;
    }
}
