FROM alpine:latest

RUN set -x && \
    apk add --update --no-cache curl ca-certificates python py-pip jq
	
RUN set -x && \
    curl -o /usr/local/bin/kubectl https://amazon-eks.s3.us-west-2.amazonaws.com/1.15.10/2020-02-22/bin/linux/amd64/kubectl && \
	curl -o /usr/local/bin/aws-iam-authenticator https://amazon-eks.s3.us-west-2.amazonaws.com/1.15.10/2020-02-22/bin/linux/amd64/aws-iam-authenticator	

COPY kubectl-wrapper.sh /usr/local/bin/kubectl-wrapper.sh

RUN set -x && \
    \    
    chmod +x /usr/local/bin/kubectl && \
    chmod +x /usr/local/bin/kubectl-wrapper.sh && \
    chmod +x /usr/local/bin/aws-iam-authenticator && \
    \
    # Create non-root user (with a randomly chosen UID/GUI).
    adduser kubectl -Du 2342 && \
    \
    # Install AWS CLI
    pip install --upgrade awscli && \
    # Basic check it works.
    aws --version && \ 
	kubectl version --short --client

##USER kubectl
ENTRYPOINT [ "/usr/local/bin/kubectl-wrapper.sh" ]