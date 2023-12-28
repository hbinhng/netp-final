#!/bin/sh

cd applications/org.uet.int3304.node

classpath=target/org.uet.int3304.node-1.0.0-SNAPSHOT.jar
classpath=$classpath:target/deps/*

echo $classpath

java \
  --class-path $classpath \
  org.uet.int3304.node.Program