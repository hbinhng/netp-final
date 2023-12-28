#!/bin/sh

classpath=applications/org.uet.int3304.gateway/target/org.uet.int3304.gateway-1.0.0-SNAPSHOT.jar
classpath=$classpath:applications/org.uet.int3304.gateway/target/deps/*
echo $classpath

java \
  --class-path $classpath \
  org.uet.int3304.gateway.Program