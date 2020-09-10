package io.yassinefarich.palyersgame.beans;

import io.yassinefarich.palyersgame.messagebrokers.MessageBroker;

import java.util.function.Consumer;
import java.util.function.Function;

public class Player {
    private String name;
    private MessageBroker messageBroker;
    private Consumer<Message> messagehandler;

    public String getName() {
        return name;
    }

    public void sendMessageTo(Player p2, Message message) {
        this.messageBroker.send(this, p2, message);
    }

    private void registerMessageBroker(MessageBroker messageBroker) {
        this.messageBroker = messageBroker;
        messageBroker.subscribe(this, messagehandler);
    }

    public static PlayerBuilder aNew(String playerName) {
        return new PlayerBuilder(playerName);
    }

    @Override
    public String toString() {
        return name;
    }

    public static final class PlayerBuilder {
        private MessageBroker messageBroker;
        private Function<Player, Consumer<Message>> messagehandler;
        private String playerName;

        private PlayerBuilder(String playerName) {
            this.playerName = playerName;
        }

        public PlayerBuilder registerMessageBroker(MessageBroker messageBroker) {
            this.messageBroker = messageBroker;
            return this;
        }

        public PlayerBuilder registerMessagehandler(Function<Player, Consumer<Message>> messagehandler) {
            this.messagehandler = messagehandler;
            return this;
        }

        public Player build() {
            Player player = new Player();
            player.name = this.playerName;
            if (this.messagehandler != null) {
                player.messagehandler = this.messagehandler.apply(player);
                player.registerMessageBroker(this.messageBroker);
            }
            return player;
        }
    }
}
