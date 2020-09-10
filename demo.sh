#!/bin/bash

echo "### Init ENV VARIABLES ###"
JAVA_HOME="/usr/lib/jvm/java-11-openjdk"
MVN_HOME="/mnt/sda4/opt/apache-maven-3.6.3"

echo "### Maven clean install project ###"
"$MVN_HOME/bin/mvn" clean install

## Clear screen
clear

# Demo using single process
echo "### Single process DEMO ###"
"$JAVA_HOME/bin/java" -cp "target/*" io.yassinefarich.palyersgame.Main

echo "### Multi process DEMO ###"
echo "### - Player 1 : ###"
"$JAVA_HOME/bin/java" -cp "target/*" io.yassinefarich.palyersgame.Main --multi-process --initiator Player1 &
sleep 1
echo "### - Player 2 : ###"
"$JAVA_HOME/bin/java" -cp "target/*" io.yassinefarich.palyersgame.Main --multi-process Player2

sleep 2