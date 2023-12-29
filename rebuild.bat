@echo off

set command="mvn clean package"

if "%1"=="--no-thicc" set command="%command% -Dassembly.skipAssembly=true"

%command%