#!/bin/bash

PROJECT_VERSION=`mvn -q -Dexec.executable=echo -Dexec.args='${project.version}' --non-recursive exec:exec`
export PROJECT_VERSION

if [ ! -e dreamScreenTime.env ]
then
  echo "env vars should be populated in dreamScreenTime.env file"
  exit 1
fi

docker compose up -d

