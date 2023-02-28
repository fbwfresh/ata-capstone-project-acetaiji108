#!/usr/bin/env bash
docker stop redis-stack
docker rm redis-stack
docker network create kenzie-local
docker run -d --network kenzie-local --name redis-stack -p 6379:6379 -p 8001:8001 redis/redis-stack:latest
