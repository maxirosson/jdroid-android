#!/bin/sh

BUILD_DIRECTORY=$1
PROJECT_NAME=jdroid

# Help
# ****
if [ $# -eq 1 ] && [ $1 = -h ]
then
        echo "Help"
        echo "****"
        echo ""
        echo "This script will build the application."
        echo "Available parameters"
        echo ""
        echo " 1) The path to a directory where the code will be checked out and the assemblies would be generated. For example: /home/user/build. Required."
        echo ""
        exit 0
fi

# Parameters validation
# ************************
if [ -z "$BUILD_DIRECTORY" ]
then
	echo "[ERROR] The BUILD_DIRECTORY parameter is required"
	echo "Run the script with '-h' for help"
	exit 1;
fi

if [ ! -d "$BUILD_DIRECTORY" ]
then
	echo "[ERROR] - The BUILD_DIRECTORY directory does not exist."
	echo "Run the script with '-h' for help"
	exit 1;
fi

SOURCE_DIRECTORY=$BUILD_DIRECTORY

# Assemblies Generation
# ************************

# Install the jdroid java jar
cd $SOURCE_DIRECTORY/$PROJECT_NAME/jdroid-java
mvn dependency:resolve clean install -Dmaven.test.skip=true

# Install the jdroid javaweb jar
cd $SOURCE_DIRECTORY/$PROJECT_NAME/jdroid-javaweb
mvn dependency:resolve clean install -Dmaven.test.skip=true

# Install the jdroid android apk lib
cd $SOURCE_DIRECTORY/$PROJECT_NAME/jdroid-android
mvn dependency:resolve clean install

# Install the jdroid android leftnavbar apk lib
cd $SOURCE_DIRECTORY/$PROJECT_NAME/jdroid-android-leftnavbar
mvn dependency:resolve clean install

