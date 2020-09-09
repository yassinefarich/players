package io.yassinefarich.test;

import io.yassinefarich.test.beans.Message;
import io.yassinefarich.test.beans.Player;
import io.yassinefarich.test.broker.InterprocessMessageBroker;
import io.yassinefarich.test.broker.SimpleMessageBroker;

import java.util.List;

public class Main {

    public static void main(String[] args) {


        List<String> argsAsList = List.of(args);


        if (argsAsList.contains("--multi-process")) {
            String currentPlayerName = argsAsList.contains("Player1") ? "Player1" : "Player2";
            String secondPlayerName = argsAsList.contains("Player1") ? "Player2" : "Player1";

            InterprocessMessageBroker interprocessMessageBroker = new InterprocessMessageBroker();
            Player currentPlayer = Player
                    .aNew(currentPlayerName)
                    .registerMessageBroker(interprocessMessageBroker)
                    .registerMessagehandler(me -> message ->
                            me.sendMessageTo(message.getEmitter(), Message.fromMessage(message, me)))
                    .build();


            if (argsAsList.contains("--initiator")) {
                currentPlayer.sendMessageTo(Player.aNew(secondPlayerName).build(),
                        Message.aNew(currentPlayer, "This is a message", 20));
            }

            interprocessMessageBroker.listen(currentPlayer);
        }

        if (args[0].equals("--single-process")) {
            runSingleProcessMode();
        }

    }

    private static void runSingleProcessMode() {
        SimpleMessageBroker mainMessageBroker = new SimpleMessageBroker();

        Player p1 = Player
                .aNew("Player 1")
                .registerMessageBroker(mainMessageBroker)
                .registerMessagehandler(me -> message ->
                        me.sendMessageTo(message.getEmitter(), Message.fromMessage(message, me)))
                .build();

        Player p2 = Player
                .aNew("Player 2")
                .registerMessageBroker(mainMessageBroker)
                .registerMessagehandler(me -> message ->
                        me.sendMessageTo(message.getEmitter(), Message.fromMessage(message, me)))
                .build();
        p1.sendMessageTo(p2, Message.aNew(p1, "Hello...", 20));
    }
}
