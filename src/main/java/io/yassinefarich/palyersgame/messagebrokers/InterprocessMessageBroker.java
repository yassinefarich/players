package io.yassinefarich.palyersgame.messagebrokers;

import io.yassinefarich.palyersgame.beans.Message;
import io.yassinefarich.palyersgame.beans.Player;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class InterprocessMessageBroker implements MessageBroker {

    private static final String MAPPED_FILE = "/tmp/memoryMappedFile";
    private static final byte EOF = 0x1a;
    private static final int BUFFER_SIZE = 10000; //chars
    public static final String OUTGOING = "-->";
    public static final String INCOMING = "<--";

    private int offset = 0;
    private boolean stillWaitingForTheLastMessgae = true;
    public Map<Player, Consumer<Message>> handlers = new HashMap<>();
    private MappedByteBuffer buffer;

    public InterprocessMessageBroker() {
        try {
            RandomAccessFile memoryMappedFile = new RandomAccessFile(MAPPED_FILE, "rw");
            buffer = memoryMappedFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, BUFFER_SIZE * 8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void writeInBuffer(String message) {
        buffer.position(offset);
        buffer.put(message.getBytes());
        buffer.put(EOF);
        offset = offset + message.getBytes().length;
    }

    public String readFromBuffer() {
        byte c = 0;
        buffer.position(offset);
        StringBuilder stringBuffer = new StringBuilder();
        while (c != EOF) {
            c = buffer.get(offset);
            if (c != EOF) {
                offset++;
                stringBuffer.append((char) c);
            }
        }
        return stringBuffer.toString();
    }


    @Override
    public void subscribe(Player player, Consumer<Message> callBack) {
        handlers.put(player, callBack);
    }

    @Override
    public void send(Player sender, Player reciver, Message message) {
        trace(reciver, message, message.getEmitter(), OUTGOING);
        stillWaitingForTheLastMessgae = message.isNotExpired();
        writeInBuffer(message.print());
    }

    public void listen(Player player) {

        while (stillWaitingForTheLastMessgae) {
            String messge = readFromBuffer();
            if (!messge.isEmpty()) {
                Message message = Message.parseFrom(messge);
                if (message.isNotExpired()) {
                    trace(message.getEmitter(), message, player, INCOMING);
                    handlers.get(player).accept(message);
                } else {
                    break;
                }
            }
            sleep();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void trace(Player reciver, Message message, Player emitter, String action) {
        System.out.println("[" + emitter.getName() + "] " + action + " " + reciver.getName()
                + " (" + message.getMessage() + "|" + message.getTimeToLive() + ")");
    }

    public void initFilesystem() {
        buffer.position(0);
        buffer.put(EOF);
    }
}
