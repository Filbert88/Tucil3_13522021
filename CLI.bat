@echo off

if not exist bin mkdir bin

javac -d bin src\*.java
cls

echo Running...
java -cp bin Main