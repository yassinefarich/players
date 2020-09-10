package io.yassinefarich.palyersgame.messagebrokers;

import io.yassinefarich.palyersgame.beans.Message;
import io.yassinefarich.palyersgame.beans.Player;

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
    public void send(Player emitter, Player receiver, Message message) {
        if (message.isNotExpired()) {
            trace(emitter, receiver, message);
            broker.get(receiver).accept(message);
        }
    }

    private void trace(Player emitter, Player receiver, Message message) {
        System.out.println(emitter.getName() + "-->" + receiver.getName()
                + "(" + message.getMessage() + "|" + message.getTimeToLive() + ")");
    }

}
