package com.tangent.tokens.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.Data;

import java.io.IOException;

@Data
public class Address {

    private static final String PREFIX = "0x";
    private static final int ADDRESS_LENGTH = 40 + PREFIX.length();

    private String value;

    private Address(String value) {
        this.value = value.toLowerCase();
    }

    public static Address fromString(String value) {
        if (value == null || !value.startsWith(PREFIX) || value.length() != ADDRESS_LENGTH) {
            throw new IllegalArgumentException("Invalid Ethereum address format");
        }
        return new Address(value);
    }

    @Override
    public String toString() {
        return value;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Address other)) {
            return false;
        }
        return this.value.equals(other.value);
    }

    public int hashCode() {
        return value.hashCode();
    }

    public static class AddressSerializer extends JsonSerializer<Address> {

        @Override
        public void serialize(Address address, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(address.getValue());
        }
    }

    @Converter(autoApply = true)
    public static class AddressConverter implements AttributeConverter<Address, String> {

        @Override
        public String convertToDatabaseColumn(Address address) {
            return address != null ? address.toString() : null;
        }

        @Override
        public Address convertToEntityAttribute(String dbData) {
            return dbData != null ? Address.fromString(dbData) : null;
        }
    }

}
