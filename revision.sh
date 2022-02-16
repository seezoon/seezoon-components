#!/bin/sh
# revision project
# ./revision.sh 1.0.0-SNAPSHOT
set -e
version=$1
if [ -z "$version" ]; then
  echo -n "Enter New Version："
  read -r version
fi
#
mvn versions:set -DnewVersion="${version}"
# update bom version
cd seezoon-dependencies-bom
mvn versions:set -DnewVersion="${version}"
echo "Current Version： ${version}"
