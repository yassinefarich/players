package io.yassinefarich.palyersgame.messagebrokers;

import io.yassinefarich.palyersgame.beans.Message;
import io.yassinefarich.palyersgame.beans.Player;

import java.util.function.Consumer;

public interface MessageBroker {

    void subscribe(Player player, Consumer<Message> callBack);

    void send(Player sender, Player reciver, Message message);
}
