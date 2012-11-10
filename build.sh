#!/bin/sh

BUILD_DIRECTORY=$1
USER_NAME=$2
DEPLOY=$3
BRANCH=$4
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
        echo " 2) The Git user name used to checkout the code. Required."
        echo ""
        echo " 3) Whether the assemblies should be deployed or not. Optional. Default value: false"
        echo ""
        echo " 4) The branch from where check out the code. Optional. Default value: master"
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

if [ -z "$USER_NAME" ]
then
	echo "[ERROR] The USER_NAME parameter is required"
        echo "Run the script with '-h' for help"
        exit 1
fi

if [ -z "$DEPLOY" ]
then
	DEPLOY="false"
fi

if [ -z "$BRANCH" ]
then
	BRANCH=master
fi

SOURCE_DIRECTORY=$BUILD_DIRECTORY/$PROJECT_NAME/source
ASSEMBLIES_DIRECTORY=$BUILD_DIRECTORY/$PROJECT_NAME/assemblies

# Checking out
# ************************

# Clean the directories
rm -r -f $SOURCE_DIRECTORY
mkdir -p $SOURCE_DIRECTORY

rm -r -f $ASSEMBLIES_DIRECTORY
mkdir -p $ASSEMBLIES_DIRECTORY

# Checkout the project
cd $SOURCE_DIRECTORY
echo Cloning git@github.com:maxirosson/jdroid.git
git clone git@github.com:maxirosson/jdroid.git $PROJECT_NAME

cd $SOURCE_DIRECTORY/$PROJECT_NAME
if [ "$BRANCH" != 'master' ] 
then
	git checkout -b $BRANCH origin/$BRANCH --track
fi

# Assemblies Generation
# ************************
cd $SOURCE_DIRECTORY/$PROJECT_NAME

mvn dependency:resolve clean install -Dmaven.test.skip=true

if [ "$DEPLOY" = "true" ]
then
    mvn javadoc:aggregate
	mvn deploy assembly:single -Dmaven.test.skip=true
	cp ./target/*.zip $ASSEMBLIES_DIRECTORY/
fi


