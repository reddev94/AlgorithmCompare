#!/bin/sh

kubectl config use-context kind-kind
kubectl apply -f /k8s-manifests