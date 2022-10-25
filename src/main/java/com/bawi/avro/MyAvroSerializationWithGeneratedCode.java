package com.bawi.avro;


import com.bawi.avro.model.Friend;
import com.bawi.avro.model.Parent;
import com.bawi.avro.model.User;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public class MyAvroSerializationWithGeneratedCode {


    public static void main(String[] args) throws IOException {
        // mvn generates java classes from schema/user.avsc

        User user1 = new User();
        //user1.setName("Alyssa");
        user1.setFavoriteNumber(256);
        // Leave favorite color null

        // serialization walks through avro User schema elements checking the types - if a corresponding Parent field is not set (null) then
        // and the avro type for that is not an union of "null" then the serializer attempts to serialize each User class field
        // by calling a get(int fieldIndex) on that User instance causing NPE
        // - solution if Parent field is not set in java: make parent avro type union with null
//        user1.setParent(new Parent("DaddyFirstName")); // serialization to avro iterates over the schema and try to write every schema defined field

        // Alternate constructor
        User user2 = new User("Ben",
                7,
                "red",
                new Parent("Daddy"),
                Arrays.asList("vw", "toyota"),
                Collections.singletonList(new Friend(10, false)));

        // Construct via builder
        User user3 = User.newBuilder()
                .setName("Charlie")
                .setFavoriteColor("blue")
                .setFavoriteNumber(null)
                .setParent(null)
                //.setCarsColors(null)
                .setFriends(null)
                .build();  // builder pattern expects either using setter method in java
        // or specifying default value in avro schema - otherwise AvroRuntimeException

    /*
    either:

    {
      "name": "cars_colors",
      "type": [
        {
          "type": "array",
          "items": "string"
        }, "null"
      ], "default": "null"
    },

    -> "cars_colors": []
    or:

    {
      "name": "cars_colors",
      "type": [
        "null",
        {
          "type": "array",
          "items": "string"
        }
      ],
      "default": null
    },

    -> "cars_colors": null

         */


        // Serialize user1, user2 and user3 to disk
        DatumWriter<User> userDatumWriter = new SpecificDatumWriter<>(User.class);
        DataFileWriter<User> dataFileWriter = new DataFileWriter<>(userDatumWriter);
        File avroFile = new File("users.avro");
        dataFileWriter.create(user1.getSchema(), avroFile);
        dataFileWriter.append(user1);
        dataFileWriter.append(user2);
        dataFileWriter.append(user3);
        dataFileWriter.close();


        // Deserialize Users from disk
        DatumReader<User> userDatumReader = new SpecificDatumReader<>(User.class);
        DataFileReader<User> dataFileReader = new DataFileReader<>(avroFile, userDatumReader);
        User user = null;
        while (dataFileReader.hasNext()) {
            // Reuse user object by passing it to next(). This saves us from
            // allocating and garbage collecting many objects for files with
            // many items.
            user = dataFileReader.next(user);
            System.out.println(user);
        }
    }
}