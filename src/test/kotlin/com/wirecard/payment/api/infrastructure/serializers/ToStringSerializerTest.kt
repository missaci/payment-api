package com.wirecard.payment.api.infrastructure.serializers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ToStringSerializerTest {

    @Test
    fun `should serialize an object using the toString function`() {
        val data = WrapperObject(ClassToBeSerialized("teste"))

        val result = ObjectMapper().writeValueAsString(data);

        assertEquals("{\"internalValue\":\"teste\"}", result)

    }

}

data class WrapperObject(val internalValue:ClassToBeSerialized)

@JsonSerialize(using = ToStringSerializer::class)
class ClassToBeSerialized(val value:String) {

    override fun toString(): String {
        return value
    }
}