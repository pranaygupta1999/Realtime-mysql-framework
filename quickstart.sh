#!/usr/bin/bash
./compile.sh
echo -e "\e[33mStarting the Mysql server. Select the data directory\e[0m"
datadir=`zenity --file-selection --title="Select data directory" --directory`
filename=`zenity --entry --text="Enter the file name for general log" --title="General log file name"`
mysqld --datadir="$datadir" --general-log --general-log-file="$filename" &
file="$datadir/$filename"
echo "Waiting for Mysql server to start"
while [ ! -f "$file" ]; do
    :
done
sleep 2
clear
echo -e "\e[32mMysql Server Started\e[0m"
cd ./generated
java Server "$file" || kill -term `pidof mysqld`