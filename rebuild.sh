#!/bin/bash

command="mvn clean package"

if [[ "$*" == *"--no-thicc"* ]]
then
  command+=" -Dassembly.skipAssembly=true"
fi

$command