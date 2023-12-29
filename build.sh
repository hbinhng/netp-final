#!/bin/bash

command="mvn package"

if [[ "$*" == *"--no-thicc"* ]]
then
  command+=" -Dassembly.skipAssembly=true"
fi

$command