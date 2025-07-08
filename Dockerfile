FROM ubuntu:latest
LABEL authors="doyle"

ENTRYPOINT ["top", "-b"]