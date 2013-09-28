#!/bin/bash
TOMCAT_HOME="$1"
WAR_PATH="$2"
PROJECT_NAME="$3"
BACKUP_DIR="$4"

# Help
# ****
if [ $# -eq 1 ] && [ $1 = -h ]
then
        echo "Help"
        echo "****"
        echo ""
        echo "This script will deploy the application."
        echo "Available parameters"
        echo ""
        echo " 1) The Apache Tomcat path. Required. For example: /home/user/apache-tomcat-6.0.29"
        echo ""
        echo " 2) The path of the war file to deploy. Required."
        echo ""
        echo " 3) The project name. Required."
        echo ""
        echo " 4) The backup directory path. Optional."
        echo ""
        exit 0
fi

# Parameters validation
# ************************
if [ -z "$TOMCAT_HOME" ]
then
	echo "[ERROR] The TOMCAT_HOME parameter is required"
	echo "Run the script with '-h' for help"
	exit 1;
fi

if [ ! -d "$TOMCAT_HOME" ]
then
	echo "[ERROR] - The TOMCAT_HOME directory does not exist."
	echo "Run the script with '-h' for help"
	exit 1;
fi

if [ -z "$WAR_PATH" ]
then
	echo "[ERROR] The WAR_PATH parameter is required"
        echo "Run the script with '-h' for help"
        exit 1
fi

filename=`echo $WAR_PATH | grep 'war$'`
if [ -z $filename ] || [ ! -e $WAR_PATH ]
then
        echo "[ERROR] The WAR_PATH parameter is not a war file"
        exit 0
fi

if [ -z "$PROJECT_NAME" ]
then
	echo "[ERROR] The PROJECT_NAME parameter is required"
	echo "Run the script with '-h' for help"
	exit 1;
fi

if [ ! -z "$BACKUP_DIR" ] && [ ! -d "$BACKUP_DIR" ]
then
	echo "[ERROR] The BACKUP_DIR directory does not exist."
	echo "Run the script with '-h' for help"
	exit 1;
fi

# Deployment
# *******************
echo "Starting deployment..."

# Killing TOMCAT

#TOMCAT_PID="$(ps -ef | grep $TOMCAT_HOME/bin | grep -v grep | sed "s/ \+/,/g" | cut -f2 -d, | tr '\n' ' ')";
#if [ ! -z "$TOMCAT_PID" ]
#then
#	echo "Killing Apache Tomcat ($TOMCAT_HOME [PID=$TOMCAT_PID])";
#	kill -9 "$TOMCAT_PID";
#	echo "Apache Tomcat killed";
#fi

echo "Shuting down tomcat..."
#$TOMCAT_HOME/bin/shutdown.sh
/etc/init.d/tomcat stop

echo "Clearing logs..."
cd $TOMCAT_HOME/logs
rm -rf *

echo "Removing earlier versions..."
cd $TOMCAT_HOME/webapps
rm -rf $PROJECT_NAME*

echo "Deploying war..."
cp -p $WAR_PATH $TOMCAT_HOME/webapps/$PROJECT_NAME.war

echo "Restarting tomcat..."
#$TOMCAT_HOME/bin/startup.sh
/etc/init.d/tomcat start

#Backups
#*******
if [ ! -z "$BACKUP_DIR" ]
then
    echo "Creating a backup copy..."
	timeStamp=`date +%F-%T`
	warName=`basename $WAR_PATH .war`
    backupFileName=$warName-[$timeStamp].war
    cp -p $WAR_PATH $BACKUP_DIR/${backupFileName}
	rm $WAR_PATH
	echo "Backup created at $BACKUP_DIR/${backupFileName}"
fi

echo "Deployment process finished successfully"

exit 0;
