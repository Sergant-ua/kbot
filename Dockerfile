FROM quay.io/projectquay/golang:1.20 AS builder

WORKDIR /go/src/app
COPY . .
ARG TARGETOS=${TARGETOS}
RUN make build ${TARGETOS}

FROM scratch
WORKDIR /
COPY --from=builder /go/src/app/kbot .
COPY --from=alpine:latest /etc/ssl/certs/ca-certificates.crt /etc/ssl/certs/
ENTRYPOINT ["./kbot"]
