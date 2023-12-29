@echo off

cd applications\org.uet.int3304.node

set classpath=target\org.uet.int3304.node-1.0.0-SNAPSHOT.jar
set classpath=%classpath%;target\deps\*

echo %classpath%

java^
  --class-path %classpath%^
  org.uet.int3304.node.Program
