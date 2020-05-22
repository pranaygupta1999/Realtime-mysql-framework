#!/usr/bin/bash
javac sources/com/pranay/realtimedatabase/*.java -cp . -d ./generated/
if [ $? -ne 0 ] 
then
    echo -e "\e[031mCompilation failed\e[0m"
    exit
fi
javac sources/*.java -cp sources/ -d ./generated/
if [ $? -ne 0 ]
then
    echo -e "\e[031mCompilation failed\e[0m"
    exit
fi
