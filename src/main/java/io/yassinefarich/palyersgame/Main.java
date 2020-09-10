package io.yassinefarich.palyersgame;

import io.yassinefarich.palyersgame.beans.Message;
import io.yassinefarich.palyersgame.beans.Player;
import io.yassinefarich.palyersgame.messagebrokers.InterprocessMessageBroker;
import io.yassinefarich.palyersgame.messagebrokers.SimpleMessageBroker;

import java.util.List;

import static io.yassinefarich.palyersgame.beans.Message.fromMessage;

public class Main {

    public static final String PLAYER_1 = "Player1";
    public static final String PLAYER_2 = "Player2";

    public static void main(String[] args) {
        List<String> argsAsList = List.of(args);

        if (argsAsList.contains("--multi-process")) {
            runMultiProcessMode(argsAsList);

        } else {
            runSingleProcessMode();
        }
    }

    private static void runMultiProcessMode(List<String> argsAsList) {
        String currentPlayerName = argsAsList.contains(PLAYER_1) ? PLAYER_1 : PLAYER_2;
        String secondPlayerName = argsAsList.contains(PLAYER_1) ? PLAYER_2 : PLAYER_1;

        InterprocessMessageBroker interprocessMessageBroker = new InterprocessMessageBroker();

        Player currentPlayer = Player
                .aNew(currentPlayerName)
                .registerMessageBroker(interprocessMessageBroker)
                .registerMessagehandler(me -> message ->
                        me.sendMessageTo(message.getEmitter(), fromMessage(message, me)))
                .build();


        if (argsAsList.contains("--initiator")) {
            interprocessMessageBroker.initFilesystem();
            currentPlayer.sendMessageTo(Player.aNew(secondPlayerName).build(),
                    Message.aNew(currentPlayer, "Hello Multiprocess", 20));
        }

        interprocessMessageBroker.listen(currentPlayer);
    }

    private static void runSingleProcessMode() {
        SimpleMessageBroker mainMessageBroker = new SimpleMessageBroker();

        Player currentPlayer = Player
                .aNew(PLAYER_1)
                .registerMessageBroker(mainMessageBroker)
                .registerMessagehandler(me -> message ->
                        me.sendMessageTo(message.getEmitter(), fromMessage(message, me)))
                .build();

        Player secondPlayer = Player
                .aNew(PLAYER_2)
                .registerMessageBroker(mainMessageBroker)
                .registerMessagehandler(me -> message ->
                        me.sendMessageTo(message.getEmitter(), fromMessage(message, me)))
                .build();

        currentPlayer.sendMessageTo(secondPlayer, Message.aNew(currentPlayer, "Hello singleprocess ", 20));
    }
}
