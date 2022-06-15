@echo off

:: change CHCP to UTF-8
CHCP 65001

set /p version="Give a Minecraft version to run! "
if not exist %version%\spigot-%version%.jar (
	echo This version of server not found. Exiting.
	pause
	goto _exit
)

cd %version%

:_start

cls

title Server (version %version%)

java -Xms256M -Xmx2G -DIReallyKnowWhatIAmDoingISwear=true -XX:+UseG1GC -XX:+ParallelRefProcEnabled -XX:MaxGCPauseMillis=200 -XX:+UnlockExperimentalVMOptions -XX:+DisableExplicitGC -XX:+AlwaysPreTouch -XX:G1NewSizePercent=30 -XX:G1MaxNewSizePercent=40 -XX:G1HeapRegionSize=8M -XX:G1ReservePercent=20 -XX:G1HeapWastePercent=5 -XX:G1MixedGCCountTarget=4 -XX:InitiatingHeapOccupancyPercent=15 -XX:G1MixedGCLiveThresholdPercent=90 -XX:G1RSetUpdatingPauseTimePercent=5 -XX:SurvivorRatio=32 -XX:+PerfDisableSharedMem -XX:MaxTenuringThreshold=1 -jar spigot-%version%.jar nogui

set choice=""
set /p choice="Do you want to restart? Press 'y' or 'Y' and 'Enter' to restart! "
if not "%choice%" == "" (
	if "%choice:~0,1%" == "y" goto _start
	if "%choice:~0,1%" == "Y" goto _start
)

:_exit
exit 0