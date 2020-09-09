package io.yassinefarich.test.broker;

import io.yassinefarich.test.beans.Message;
import io.yassinefarich.test.beans.Player;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class InterprocessMessageBroker implements MessageBroker {


    private static String BUFFER_FILE = "/tmp/memoryMappedFile";
    private static byte EOF = 0x1a;
    private int bufferSize = 10000; //Chars
    private int offset = 0;
    public Map<Player, Consumer<Message>> handlers = new HashMap<>();

    private MappedByteBuffer buffer;


    public InterprocessMessageBroker() {
        try {
            RandomAccessFile memoryMappedFile = new RandomAccessFile(BUFFER_FILE, "rw");
            buffer = memoryMappedFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, bufferSize * 8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void writeInBuffer(String message) {
        System.out.println("Write in buffer value : " + message);
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
        writeInBuffer(sender.getName() + "_" + message.toString() + "_" + message.getTimeToLive());
    }

    public void listen(Player player) {

        while (true) {
            String messge = readFromBuffer();
            if (!messge.isEmpty()) {
                Message message = Message.parseFrom(messge);
                if (message.isNotExpired()) {
                    handlers.get(player).accept(message);
                } else {
                    break;
                }
            }

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
