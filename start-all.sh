#!/bin/bash

# Create external network if it doesn't exist
docker network inspect monitoring_net >/dev/null 2>&1 || \
  docker network create monitoring_net

cd ./user-management
docker-compose --env-file .env -p usermanagement up -d

cd ../monitoring
docker-compose --env-file .env -p monitoring up -d
