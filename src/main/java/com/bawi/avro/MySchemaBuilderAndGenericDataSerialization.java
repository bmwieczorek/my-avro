package com.bawi.avro;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class MySchemaBuilderAndGenericDataSerialization {
  public static void main(String[] args) throws IOException {
    Schema schema = SchemaBuilder.record("myRecordName").fields()
      .requiredInt("myRequiredInt")
      //.name("myRequiredInt2").type().intType().noDefault()

      .optionalDouble("myOptionalDouble")
      //.name("myOptionalDouble2").type().optional().doubleType()

      .nullableString("myNullableString", "myNullableStringDefaultValue")
      //.name("myNullableString2").type().nullable().stringType().stringDefault("myNullableStringDefaultValue2")

      .name("myRequiredArrayLongs").type().array().items().longType().noDefault()

      .name("myRequiredSubRecord")
        .type(
          SchemaBuilder.record("myRequiredSubRecordType").fields().requiredFloat("myRequiredFloat").endRecord()
        ).noDefault()

      .name("myOptionalArraySubRecords").type().nullable().array()
        .items(
          SchemaBuilder.record("myOptionalArraySubRecordType").fields().requiredBoolean("myRequiredBoolean").endRecord()
        ).noDefault()
    .endRecord();

    System.out.println(schema);

    File file = new File("my-file.avro");
    GenericData.Record record = new GenericData.Record(schema);

    record.put("myRequiredInt", 1);

    record.put("myOptionalDouble", 2.2d); // this line can be commented since optional long

//    record.put("myNullableString", "abc"); // this line can be commented since optional string

    record.put("myRequiredArrayLongs", Arrays.asList(1L, 2L, 3L));  // required otherwise java.lang.NullPointerException: null of array in field myRequiredArrayLongs of myRecordName

    GenericData.Record myRequiredSubRecord = new GenericData.Record(schema.getField("myRequiredSubRecord").schema());
    myRequiredSubRecord.put("myRequiredFloat", 1.0f); // required otherwise null of int in field myRequiredFloat of myRequiredSubRecordType in field myRequiredSubRecord of myRecordName
    record.put("myRequiredSubRecord", myRequiredSubRecord); // required otherwise java.lang.NullPointerException: null of myRequiredSubRecordType in field myRequiredSubRecord of myRecordName

    GenericData.Record myOptionalArraySubRecord = new GenericData.Record(schema.getField("myOptionalArraySubRecords").schema().getTypes().get(0).getElementType());
    myOptionalArraySubRecord.put("myRequiredBoolean", true);
    record.put("myOptionalArraySubRecords", Arrays.asList(myOptionalArraySubRecord, myOptionalArraySubRecord));

    GenericDatumWriter<GenericRecord> genericDatumWriter = new GenericDatumWriter<>(schema);
    DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<>(genericDatumWriter);
    dataFileWriter.create(schema, file);
    dataFileWriter.append(record);
    dataFileWriter.close();

    DatumReader<GenericRecord> datumReader = new GenericDatumReader<>(schema);
    DataFileReader<GenericRecord> dataFileReader = new DataFileReader<>(file, datumReader);
    GenericRecord recordRead = null;
    while (dataFileReader.hasNext()) {
      // Reuse user object by passing it to next(). This saves us from allocating and garbage collecting
      // many objects for files with many items.
      recordRead = dataFileReader.next(recordRead);
      System.out.println(recordRead);
    }
  }
}
