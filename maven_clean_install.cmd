@REM init env
CALL init_env.cmd

@REM RUN MAVEN CLEAN INSTALL 
%MVN_HOME%\bin\mvn clean install -s %MVN_SETTING%
pause