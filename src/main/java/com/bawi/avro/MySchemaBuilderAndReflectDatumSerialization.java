package com.bawi.avro;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.reflect.ReflectDatumWriter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class MySchemaBuilderAndReflectDatumSerialization {
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

    File file = new File("my-file2.avro");

    ReflectDatumWriter<MyRecord> writer = new ReflectDatumWriter<>(schema);
    DataFileWriter<MyRecord> dataFileWriter = new DataFileWriter<>(writer);
    dataFileWriter.create(schema, file);
    MyRecord myRecord = new MyRecord();
    myRecord.setMyRequiredInt(1);
    myRecord.setMyOptionalDouble(2.0d);
    myRecord.setMyRequiredArrayLongs(Arrays.asList(1L, 2L, 3L));
    MyRecord.MyRequiredSubRecord myRequiredSubRecord = new MyRecord.MyRequiredSubRecord();
    myRequiredSubRecord.setMyRequiredFloat(3.0f);
    myRecord.setMyRequiredSubRecord(myRequiredSubRecord);
    MyRecord.MyOptionalArraySubRecordType myOptionalArraySubRecordType = new MyRecord.MyOptionalArraySubRecordType();
    myOptionalArraySubRecordType.setMyRequiredBoolean(true);
    myRecord.setMyOptionalArraySubRecords(Arrays.asList(myOptionalArraySubRecordType, myOptionalArraySubRecordType));
    dataFileWriter.append(myRecord);
    dataFileWriter.close();

    GenericDatumReader<GenericRecord> datumReader = new GenericDatumReader<>(schema);
    DataFileReader<GenericRecord> dataFileReader = new DataFileReader<>(file, datumReader);
    GenericRecord recordRead = null;
    while (dataFileReader.hasNext()) {
      // Reuse user object by passing it to next(). This saves us from allocating and garbage collecting
      // many objects for files with many items.
      recordRead = dataFileReader.next();
      System.out.println(recordRead);
    }
  }
}
