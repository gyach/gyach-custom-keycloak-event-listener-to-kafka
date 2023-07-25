package ru.gyach.keycloak;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.OperationType;
import org.keycloak.events.admin.ResourceType;
import ru.gyach.kafka.Producer;


public class KeycloakEventListenerProvider implements EventListenerProvider {

    private final ObjectMapper objectMapper;
    public KeycloakEventListenerProvider() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void onEvent(Event event) {
        final EventType eventType = event.getType();
        final String topic = "keycloak.events." + eventType.toString().toLowerCase();
        try {
            String value = objectMapper.writeValueAsString(event);
            Producer.publishEvent(topic, event.getUserId(), value);
        } catch (JsonProcessingException e) {
            System.out.println("Cannot serialize event:" + e);
        }

    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {
        final ResourceType resourceType = adminEvent.getResourceType();
        final OperationType operationType = adminEvent.getOperationType();
        final String topic = "keycloak.admin.events." + resourceType.toString().toLowerCase() + "." +
                operationType.toString().toLowerCase();
        try {
            String value = objectMapper.writeValueAsString(adminEvent);
            Producer.publishEvent(topic, adminEvent.getAuthDetails().getUserId(), value);
        } catch (JsonProcessingException e) {
            System.out.println("Cannot serialize admin event:" + e);
        }
    }

    @Override
    public void close() {

    }
}
