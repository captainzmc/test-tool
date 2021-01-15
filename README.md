# test-tool

A testing tool capable of supporting serial or parallel tasks

bulid:
mvn clean package

usage:
Users can write the shell under test to a file and execute commands like:
java -cp odfs-test-1.0.0-jar-with-dependencies.jar test.TestTool -model concurrent -file /data/test.conf

You can use -help to see details.
