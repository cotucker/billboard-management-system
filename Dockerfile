FROM ubuntu:latest
LABEL authors="klimi"

ENTRYPOINT ["top", "-b"]