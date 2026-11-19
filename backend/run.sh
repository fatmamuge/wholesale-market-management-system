#!/bin/bash

./mvnw spring-boot:run \
	-Dspring-boot.run.arguments="--spring.datasource.url=${DATA_SOURCE_URL} --spring.datasource.username=${DATA_SOURCE_NAME} --spring.datasource.password=${DATA_SOURCE_PASSWORD}"
