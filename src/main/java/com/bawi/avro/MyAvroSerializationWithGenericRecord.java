package com.bawi.avro;


import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;

import java.io.File;
import java.io.IOException;

public class MyAvroSerializationWithGenericRecord {

    public static void main(String[] args) throws IOException {
        Schema schema = new Schema.Parser().parse(new File("schema/user.avsc"));

        GenericRecord user1 = new GenericData.Record(schema);
        user1.put("name", "Alyssa");
        //user1.put("favorite_number", 256);
        // Leave favorite color null

        // Serialize user1 and user2 to disk
        File avroFile = new File("users.avro");
        DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(schema);
        DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<>(datumWriter);
        dataFileWriter.create(schema, avroFile);
        dataFileWriter.append(user1);
        dataFileWriter.close();


        // Deserialize users from disk
        DatumReader<GenericRecord> datumReader = new GenericDatumReader<>(schema);
        DataFileReader<GenericRecord> dataFileReader = new DataFileReader<>(avroFile, datumReader);
        GenericRecord user = null;
        while (dataFileReader.hasNext()) {
            // Reuse user object by passing it to next(). This saves us from allocating and garbage
            // collecting many objects for files with many items.
            user = dataFileReader.next(user);
            System.out.println(user);
        }
    }
}