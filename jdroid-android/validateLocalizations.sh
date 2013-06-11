#!/bin/sh

ANDROID_APP_DIR=$1
PREFIX=$2
FILE_1=$3
FILE_2=$4

cd $ANDROID_APP_DIR

cat "$FILE_1" | grep "$PREFIX" | sed s/\>.*//g > temp_FILE_1
cat "$FILE_2" | grep "$PREFIX" | sed s/\>.*//g > temp_FILE_2

diff_result=`diff -a "temp_FILE_1" "temp_FILE_2"`

rm temp_FILE_1
rm temp_FILE_2

if [ -z "$diff_result" ]
then
	echo "**************************************************************"
	echo "* Status: OK. The following i19n files match:"
    	echo "*  $FILE_1"
	echo "*  $FILE_2"
	echo "**************************************************************"
	exit 0
else
	echo "**************************************************************"
	echo "* Status: ERROR. The following i19n files match:"
    	echo "*  $FILE_1"
	echo "*  $FILE_2"
	echo "*"
    	echo "* Remember that the i19n files should have the same keys "
	echo "* on the same lines."
    	echo "* If you don't have the translation for any language, please "
	echo "* add the key on all the files, and 'TODO' as value"
    	echo "*"
    	echo "Diff:"
    	echo $diff_result
    	echo "**************************************************************"
	exit 1
fi

