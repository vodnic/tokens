package com.tangent.tokens.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AddressTest {

    private static final String VALID_ADDRESS = "0x7d90d74b3501440f95fb8ee55af2ed2933c18fdd";

    @Test
    public void testFromString() {
        Address address = Address.fromString(VALID_ADDRESS);
        assertEquals(VALID_ADDRESS, address.toString());
    }

    @Test
    public void testFromString_tooLong() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Address.fromString(VALID_ADDRESS+ "00"));
    }

    @Test
    public void testFromString_tooShort() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Address.fromString("0x123"));
    }
    
    @Test
    public void testFromString_null() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Address.fromString(null));
    }

}
