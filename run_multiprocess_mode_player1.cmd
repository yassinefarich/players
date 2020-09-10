@REM init env
CALL init_env.cmd

@REM Player1
%JAVA_HOME%\bin\java -cp "target/*" io.yassinefarich.palyersgame.Main --multi-process --initiator Player1
pause