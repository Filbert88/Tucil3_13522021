#!/bin/bash
if [ -z "$DISPLAY" ]; then
    echo "Setting DISPLAY to :0.0"
    export DISPLAY=:0.0
fi

if [ ! -d "bin" ]; then
  mkdir bin
fi

javac -d bin src/*.java

clear

echo "Running..."

java -cp bin Main 

read -p "Press enter to continue"