@REM init env
CALL init_env.cmd

@REM RUN MAVEN CLEAN INSTALL 
%JAVA_HOME%\bin\java -cp "target/*" io.yassinefarich.palyersgame.Main
pause