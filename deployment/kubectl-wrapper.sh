#!/bin/sh

if [ -z "$CLUSTER" ]; then
    echo "Please provide an Amazon EKS cluster name by populating the CLUSTER environment variable"
    echo "e.g."
    echo "docker run -e CLUSTER=mycluster eks-kubectl:1.13 get pods"
    exit 1
fi

# Write a ~/.kube/config file, using the AWS CLI to fetch the necessary parameters
mkdir -p ~/.kube
OUT=~/.kube/config
SERVER=$(aws eks describe-cluster --region $REGION --name $CLUSTER | jq -r .cluster.endpoint)
CERT=$(aws eks describe-cluster --region $REGION --name $CLUSTER | jq -r .cluster.certificateAuthority.data)
echo "SERVER: $SERVER"

cat <<EOF > $OUT
apiVersion: v1
clusters:
- cluster:
    server: $SERVER
    certificate-authority-data: $CERT
  name: kubernetes
contexts:
- context:
    cluster: kubernetes
    user: aws
  name: aws
current-context: aws
kind: Config
preferences: {}
users:
- name: aws
  user:
    exec:
      apiVersion: client.authentication.k8s.io/v1alpha1
      command: aws
      args:
        - --region
        - $REGION
        - eks
        - get-token
        - --cluster-name        
        - $CLUSTER
EOF

# Pass through to kubectl
/usr/local/bin/kubectl "$@"