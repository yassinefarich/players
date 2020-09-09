package io.yassinefarich.test.broker;

import io.yassinefarich.test.beans.Message;
import io.yassinefarich.test.beans.Player;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface MessageBroker {

    void subscribe(Player player, Consumer<Message> callBack);

    void send(Player sender, Player reciver, Message message);
}
