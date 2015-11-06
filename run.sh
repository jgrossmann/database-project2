#!/bin/sh

LIB=lib
CLASSPATH=$LIB/*:.:class

# Usage: run.sh <t_es> <t_ec> <host>
/usr/bin/java -classpath $CLASSPATH App $1 $2 $3 $4


