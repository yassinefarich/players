## Inter process communication using Memory mapped file example 

```
Having a Player class - an instance of this class with that can communicate with other Player(s) (other instances of this class)

The use case for is as bellow:

1. create 2 players

2. one of the players should send a message to second player (let's call this player "initiator")

3. when a player receives a message should send back a new message that contains the received message concatenated with the message counter that this player sent.

4. finalize the program (gracefully) after the initiator sent 10 messages and received back 10 messages (stop condition)

5. Every player should run in a separate JAVA process (different PID).

```

## How to run 
1. setup environment variables in demo.sh script

```bash
echo "### Init ENV VARIABLES ###"  
JAVA_HOME="/usr/lib/jvm/java-11-openjdk"  
MVN_HOME="/mnt/sda4/opt/apache-maven-3.6.3"
```
2. Execute demo.sh script 

## Solution explanation 
* A `Player` is a simple object that can send messages (Object `Message`) to other players  through a broker message `MessageBroke`.
* Every message broker should implement `MessageBroker` interface.
* `SimpleMessageBroker` use observer like pattern to allow communication between players.
* `InterprocessMessageBroker` use a  [memory mapped file](https://en.wikipedia.org/wiki/Memory-mapped_file) to allow communication between different process.

## Improvements
* This solution can be easily extended to allow communication between multiple player and multiple process.
* A multiple improvement can be done to make code more readable and extensible.
* Sorry for not adding a windows demo launcher it's due to the fact that I don't have a windows os machine
