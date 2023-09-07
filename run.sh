#!/bin/bash

DOCKER_COMPOSE_FILE="docker-compose.yml"
IMAGE_TAG="latest"

ORIGINAL_ADMIN_MONITOR_IMAGE_NAME="algorithmcompare_admin-monitor"
ORIGINAL_ALGORITHM_COMPARE_BE_IMAGE_NAME="algorithmcompare_algorithm-compare-be"
ORIGINAL_ALGORITHM_COMPARE_FE_IMAGE_NAME="algorithmcompare_algorithm-compare-fe"
ORIGINAL_EUREKA_IMAGE_NAME="algorithmcompare_eureka"
ORIGINAL_GATEWAY_IMAGE_NAME="algorithmcompare_gateway"

MINIKUBE_ADMIN_MONITOR_IMAGE_NAME="minikube/algorithmcompare_admin-monitor"
MINIKUBE_ALGORITHM_COMPARE_BE_IMAGE_NAME="minikube/algorithmcompare_algorithm-compare-be"
MINIKUBE_ALGORITHM_COMPARE_FE_IMAGE_NAME="minikube/algorithmcompare_algorithm-compare-fe"
MINIKUBE_EUREKA_IMAGE_NAME="minikube/algorithmcompare_eureka"
MINIKUBE_GATEWAY_IMAGE_NAME="minikube/algorithmcompare_gateway"

# Step 1: Build the Docker image from the docker-compose file
echo "Building Docker image from $DOCKER_COMPOSE_FILE..."
docker-compose -f "$DOCKER_COMPOSE_FILE" build

# Step 2: Tag the image for Minikube
echo "Tagging image for Minikube..."
docker tag "$ORIGINAL_ADMIN_MONITOR_IMAGE_NAME:$IMAGE_TAG" "$MINIKUBE_ADMIN_MONITOR_IMAGE_NAME:$IMAGE_TAG"
docker tag "$ORIGINAL_ALGORITHM_COMPARE_BE_IMAGE_NAME:$IMAGE_TAG" "$MINIKUBE_ALGORITHM_COMPARE_BE_IMAGE_NAME:$IMAGE_TAG"
docker tag "$ORIGINAL_ALGORITHM_COMPARE_FE_IMAGE_NAME:$IMAGE_TAG" "$MINIKUBE_ALGORITHM_COMPARE_FE_IMAGE_NAME:$IMAGE_TAG"
docker tag "$ORIGINAL_EUREKA_IMAGE_NAME:$IMAGE_TAG" "$MINIKUBE_EUREKA_IMAGE_NAME:$IMAGE_TAG"
docker tag "$ORIGINAL_GATEWAY_IMAGE_NAME:$IMAGE_TAG" "$MINIKUBE_GATEWAY_IMAGE_NAME:$IMAGE_TAG"

# Step 3: Verify the new image
echo "Printing all the images"
docker images

echo "Done!"
