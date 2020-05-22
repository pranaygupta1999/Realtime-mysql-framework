#!/usr/bin/bash
cd generated/
logfile=`zenity --file-selection --title="Select the general log file`  
java Server "$logfile"