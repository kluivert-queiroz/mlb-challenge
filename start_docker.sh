#!/usr/bin/env bash

set -e

./gradlew clean
./gradlew build -x test

docker-compose stop
docker-compose rm --force
docker-compose build --no-cache
docker-compose up