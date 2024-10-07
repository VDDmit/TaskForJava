FROM ubuntu:latest
LABEL authors="danvo"

ENTRYPOINT ["top", "-b"]