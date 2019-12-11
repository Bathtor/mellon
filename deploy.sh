#!/bin/bash
set -x;
TARGET="/srv/www/mellon/web/"
SRC_ROOT="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
SRC_TARGET="$SRC_ROOT/target/scala-2.12"
SRC_ASSEMBLY="$SRC_ROOT/assembly/"

sbt fullOptJS
mkdir -p $SRC_ASSEMBLY
cp $SRC_TARGET/mellon-opt.js $SRC_ASSEMBLY
cp $SRC_TARGET/mellon-jsdeps.js $SRC_ASSEMBLY
cp $SRC_TARGET/classes/WEB-INF/* $SRC_ASSEMBLY
rsync -vzrh --progress -e ssh $SRC_ASSEMBLY lkroll:$TARGET
