package io.yassinefarich.test.broker;

import io.yassinefarich.test.beans.Message;
import io.yassinefarich.test.beans.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SimpleMessageBroker implements MessageBroker {

    public Map<Player, Consumer<Message>> broker = new HashMap<>();

    @Override
    public void subscribe(Player player, Consumer<Message> callBack) {
        broker.put(player, callBack);
    }

    @Override
    public void send(Player sender, Player reciver, Message message) {
        if (message.isNotExpired()) {
            System.out.println(sender + " --> " + reciver + " [ " + message + " ]");
            broker.get(reciver).accept(message);
        }
    }

}
