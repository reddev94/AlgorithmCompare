# Use the desired KIND base image
FROM kindest/node:v1.21.14

# Install curl
RUN apt-get update && apt-get install -y curl
RUN apt-get install wget

# Install kind
RUN curl -s https://api.github.com/repos/kubernetes-sigs/kind/releases/latest| grep browser_download_url | grep kind-linux-amd64 | cut -d '"' -f 4  | wget -qi -
RUN chmod a+x kind-linux-amd64
RUN mv kind-linux-amd64 /usr/local/bin/kind

# Install kubectl
RUN curl -LO https://storage.googleapis.com/kubernetes-release/release/`curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt`/bin/linux/amd64/kubectl
RUN chmod +x ./kubectl
RUN mv ./kubectl /usr/local/bin/kubectl

# Copy the KIND cluster configuration file
COPY kind-config.yaml /kind-config.yaml

# Set the work directory for the container
WORKDIR /
