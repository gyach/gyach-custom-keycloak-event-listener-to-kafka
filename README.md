# Gyach's Keycloak to Kafka event listener
Custom event listener to publish all Keycloak events to Kafka

## Dockerfile
```
FROM keycloak/keycloak:22.0.1
ENV KC_PROXY edge
COPY ./gyach-keycloak-event-listener.jar /opt/keycloak/providers/
RUN /opt/keycloak/bin/kc.sh build
```
copy jar to /opt/keycloak/providers/ and build custom docker image


