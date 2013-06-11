#!/bin/sh

ANDROID_APP_DIR=$1
FILE=$2

cd $ANDROID_APP_DIR
todo=`cat "$FILE" | grep "TODO"`

exitCode=0
if [ -n "$todo" ]
then
	
	echo "**************************************************************"
	echo "* Status: ERROR. Missing translations on $FILE"
	echo "**************************************************************"
	cat "$FILE" | grep "TODO"
	
	exit 1
fi

echo "**************************************************************"
echo "* Status: OK. Not Missing translations on $FILE"
echo "**************************************************************"
exit 0
	
