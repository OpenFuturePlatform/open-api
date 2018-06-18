#!/bin/bash

docker rm -vf open-platform
docker rm -vf postgres
docker rmi -f open-platform
docker network remove open-net