package com.bawi.avro;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class MyTimestampAvro {
    public static void main(String[] args) throws IOException {
        Schema schema = SchemaBuilder.record("myRecordName").fields()
                .requiredLong("l")
                .requiredString("sl")
                .requiredString("sf")
                .endRecord();

        GenericRecord record = new GenericData.Record(schema);
        long time = System.currentTimeMillis();
        record.put("l", time);
        record.put("sl", String.valueOf(time));
        record.put("sf", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(time));

        File avroFile = new File("ts.avro");
        DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(schema);
        DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<>(datumWriter);
        dataFileWriter.create(schema, avroFile);
        dataFileWriter.append(record);
        dataFileWriter.close();
    }
}
