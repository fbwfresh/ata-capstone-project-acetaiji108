#!/usr/bin/env sh
set -e

DYNAMODB_CONTAINER_NAME="lbc-app-dynamodb"

STORAGE_CONTAINER=$(docker ps -a -f "name=${DYNAMODB_CONTAINER_NAME}" -q)
echo "STORAGE_CONTAINER"

if [ ! -z "$STORAGE_CONTAINER" ]; then
    echo "Removing container: ${DYNAMODB_CONTAINER_NAME}"
    docker rm -f ${STORAGE_CONTAINER}
fi

echo "Running container: ${DYNAMODB_CONTAINER_NAME}"
docker run --name ${DYNAMODB_CONTAINER_NAME} -p 8000:8000 -d amazon/dynamodb-local

echo "Waiting 10 seconds for dynamodb to boot..."
sleep 10