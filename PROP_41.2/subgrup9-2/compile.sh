#!/bin/bash

TARGET_PATH="out/production/practicaProp/compresor/"

sh clean.sh $TARGET_PATH
find -name "*.java" > sources.txt
javac @sources.txt -d $TARGET_PATH

# Queda revisar esta parte del jar
cd $TARGET_PATH/compresor
jar cvf mi_compresor.jar *