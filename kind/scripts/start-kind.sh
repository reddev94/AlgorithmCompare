#!/bin/bash

kind create cluster --config /kind/kind-config.yaml
kind export kubeconfig
sleep 10  # Wait for a short time to allow nodes to start

while [[ $(kubectl get nodes --no-headers | awk '{print $2}') != 'Ready' ]]; do
  sleep 5
done

sleep infinity