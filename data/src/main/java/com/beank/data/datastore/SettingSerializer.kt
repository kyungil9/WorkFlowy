package com.beank.data.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.beank.workFlowy.Setting
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object SettingSerializer : Serializer<Setting>{
    override val defaultValue: Setting
        get() = Setting.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Setting {
        try {
            return Setting.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: Setting, output: OutputStream) {
        t.writeTo(output)
    }
}