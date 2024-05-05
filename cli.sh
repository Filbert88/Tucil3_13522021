#!/bin/bash
if [ ! -d "bin" ]; then
  mkdir bin
fi

javac -d bin src/*.java

clear

echo "Running..."

java -cp bin Main 

read -p "Press enter to continue"