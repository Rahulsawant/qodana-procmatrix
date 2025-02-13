#!/bin/bash

# Install Cassandra using Helm with the custom values file
helm install my-cassandra bitnami/cassandra --namespace default -f custom-values.yaml

# Wait for the pod to be running
echo "Waiting for Cassandra pod to be in Running state..."
kubectl wait --for=condition=ready pod -l app.kubernetes.io/name=cassandra --namespace default --timeout=300s

# Set up port forwarding
nohup kubectl port-forward -n default svc/my-cassandra 9042:9042 &
echo "Port forwarding set up. You can connect to Cassandra at localhost:9042."
