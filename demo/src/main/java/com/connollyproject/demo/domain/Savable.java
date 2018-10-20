package com.connollyproject.demo.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * DTO for arbitrary saved json objects.
 */
@Entity
@Table(name = "savable")
@TypeDef(name = "jsonb-node", typeClass = JsonNodeBinaryType.class)
public class Savable {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Id
    @Column(name = "uid")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID uid;

    @Type(type = "jsonb-node")
    @Column(columnDefinition = "jsonb")
    private JsonNode content;

    // hibernate requires default constructor
    public Savable() {
    }

    public Savable(@NonNull Map<String, Object> content) {
        this.setContent(content);
    }

    public UUID getId() {
        return uid;
    }

    public void setUUID(@NonNull UUID uuid) {
        this.uid = uuid;
    }

    public JsonNode getContent() {
        return content;
    }

    public void setContent(@NonNull Map<String, Object> objectMap) {
        this.content = mapper.valueToTree(objectMap);
    }

    public static Map<String, Object> convertToMap(@NonNull Savable savable) {
        Map<String, Object> obj = new HashMap<>();
        obj.putAll(mapper.convertValue(savable.getContent(), Map.class));
        obj.put("uid", savable.getId());
        return obj;
    }
}