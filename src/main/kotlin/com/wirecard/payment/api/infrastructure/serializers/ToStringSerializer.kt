package com.wirecard.payment.api.infrastructure.serializers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

class ToStringSerializer<T> : JsonSerializer<T>() {

    override fun serialize(parent: T, jgen: JsonGenerator, provider: SerializerProvider) {
        jgen.writeStartObject()
        jgen.writeString(parent.toString())
        jgen.writeEndObject()
    }
}