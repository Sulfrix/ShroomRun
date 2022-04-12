#!/bin/bash

# remove old library
rm -rf ../processing

# clear temp directory
#rm -rf ./temp
mkdir -p temp
cd temp

rm -rf ./processing-4.0b7

# fixed download URL
#wget -O temp.tgz https://github.com/processing/processing4/releases/download/processing-1282-4.0b7/processing-4.0b7-linux-x64.tgz

# extract
tar -xf temp.tgz

# move
mkdir -p ../../processing
mv ./processing-4.0b7/* ../../processing

rm -rf processing-4.0b7

