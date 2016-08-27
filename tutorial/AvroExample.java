package com.example.tutorial;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

public class AvroExample {

	public static void main(String[] argv) {
		PhoneNumber phoneNumber1 = PhoneNumber.newBuilder().setNumber("123456")
				.setType(0).build();
		PhoneNumber phoneNumber2 = PhoneNumber.newBuilder().setNumber("123457")
				.setType(2).build();
		List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();
		phoneNumbers.add(phoneNumber1);
		phoneNumbers.add(phoneNumber2);

		Person person = Person.newBuilder().setName("Dongxicheng")
				.setEmail("dongxicheng@yahoo.com").setId(11111)
				.setPhone(phoneNumbers).build();
		File file = new File("person.txt");
		try {
			DatumWriter<Person> personDatuWriter = new SpecificDatumWriter<Person>(
					Person.class);
			DataFileWriter<Person> dataFileWriter = new DataFileWriter<Person>(
					personDatuWriter);
			dataFileWriter.create(person.getSchema(), file);
			dataFileWriter.append(person);
			dataFileWriter.close();
		} catch (Exception e) {
			System.out.println("Write Error:" + e);
		}
		try {
			DatumReader<Person> userDatumReader = new SpecificDatumReader<Person>(
					Person.class);
			DataFileReader<Person> dataFileReader = new DataFileReader<Person>(
					file, userDatumReader);
			person = null;
			while (dataFileReader.hasNext()) {
				person = dataFileReader.next(person);
				System.out.println(person);
			}
		} catch (Exception e) {
			System.out.println("Read Error:" + e);
		}
	}
}
