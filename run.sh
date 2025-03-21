#!/bin/bash

WSL_PATH=$(pwd)
WIN_PATH=$(wslpath -w "$WSL_PATH")
JAR_PATH="$WIN_PATH/target/java-casting-0.1.jar"

mvn clean package
# Ejecutar el JAR usando Java de Windows con PowerShell
powershell.exe -Command "java -jar '$JAR_PATH'"
