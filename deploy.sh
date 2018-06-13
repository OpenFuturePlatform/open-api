#!/bin/bash


# ENVIRONMENT VARIABLES
GOOGLE_CLIENT_ID=480339541980-c9ehp1guamcuvfk7e7e2n40qlenkncjn.apps.googleusercontent.com
GOOGLE_CLIENT_SECRET=wcfa3u46cymJ-IzTC20xL1OI

POSTGRES_HOST=postgres
POSTGRES_DB=open
POSTGRES_USER=open
POSTGRES_PASSWORD=open

INFURA_URL=https://rinkeby.infura.io/DUsjLle6DbxbPVvJFDhL
ETHEREUM_PRIVATE_KEY=9bb8b9f512bfe6f2d5cd392ba9a99321c4ace0612c0cbbaf871bf208a3a3543f
OPEN_TOKEN_ADDRESS=0xD57B27f6ebA186D56ec2AaaF9BbB438678DFd4f1


# CREATE IMAGE APP
docker build -t open-platform:latest -f docker/Dockerfile .

# CREATE NETWORK
docker network create open-net

# RUN POSTGRES
docker run \
    -d \
    --name postgres \
    --restart=always \
    -e POSTGRES_DB=${POSTGRES_DB} \
    -e POSTGRES_USER=${POSTGRES_USER} \
    -e POSTGRES_PASSWORD=${POSTGRES_PASSWORD} \
    -p 5432:5432 \
    postgres

# CREATE ALIAS FOR HOSTNAME
docker network connect --alias ${POSTGRES_HOST} open-net postgres

# RUN APP
docker run \
    -i \
    --name open-platform \
    --network open-net \
    -e GOOGLE_CLIENT_ID=${GOOGLE_CLIENT_ID} \
    -e GOOGLE_CLIENT_SECRET=${GOOGLE_CLIENT_SECRET} \
    -e POSTGRES_HOST=${POSTGRES_HOST} \
    -e POSTGRES_DB=${POSTGRES_DB} \
    -e POSTGRES_USER=${POSTGRES_USER} \
    -e POSTGRES_PASSWORD=${POSTGRES_PASSWORD} \
    -e INFURA_URL=${INFURA_URL} \
    -e ETHEREUM_PRIVATE_KEY=${ETHEREUM_PRIVATE_KEY} \
    -e OPEN_TOKEN_ADDRESS=${OPEN_TOKEN_ADDRESS} \
    -p 8080:8080 \
    open-platform:latest