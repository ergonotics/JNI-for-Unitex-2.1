@echo off
echo try run : startLibraryAndJniDemo.bat .\demojnires . 1
javac unitexLibraryAndJniDemo.java
rem java -classpath .; unitexLibraryAndJniDemo
"%JAVA_HOME%\bin\java.exe" -classpath .; unitexLibraryAndJniDemo %1 %2 %3
