#!/bin/bash

echo "Starting shell script ..."

if [ $# -eq 0 ]; then
  echo "Starting conversation in single process"
  ./mvnw compile exec:java -Dexec.mainClass='com.game.Application'
elif [ "$1" == "S" ]; then
  echo "Starting socket server with Talker B for inter-process communication"
  ./mvnw compile exec:java -Dexec.mainClass='com.game.Application' -Dexec.args="$1"
elif [ "$1" == "C" ]; then
  echo "Starting socket client with Talker A for inter-process communication"
  ./mvnw compile exec:java -Dexec.mainClass='com.game.Application' -Dexec.args="$1"
else
    echo "Invalid argument. Please use 'S' (Server) or 'C' (Client) for inter-process communication"
    echo "or provide no arguments for single process communication"
fi