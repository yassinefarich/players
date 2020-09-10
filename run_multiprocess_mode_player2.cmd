@REM init env
CALL init_env.cmd

@REM Player2
%JAVA_HOME%\bin\java -cp "target/*" io.yassinefarich.palyersgame.Main --multi-process Player2
pause