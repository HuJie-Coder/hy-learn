#!/bin/bash
set -e
docker pull apache/airflow:latest
docker run -d -p 8080:8080 apache/airflow:latest
docker exec -it airflow /bin/bash