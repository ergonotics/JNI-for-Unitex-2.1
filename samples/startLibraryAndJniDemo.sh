#!/bin/bash

echo try run
echo  ./startLibraryAndJniDemo.sh ./demojnires . 1
javac unitexLibraryAndJniDemo.java
# rem java -classpath .; unitexLibraryAndJniDemo

java -Djava.library.path=. unitexLibraryAndJniDemo $1 $2 $3
