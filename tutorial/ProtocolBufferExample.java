package com.example.tutorial;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.example.tutorial.PersonProtos.Person;

public class ProtocolBufferExample {
	static public void main(String[] argv) {
		Person person1 = Person
				.newBuilder()
				.setName("Dong Xicheng")
				.setEmail("dongxicheng@yahoo.com")
				.setId(11111)
				.setPhone(
						Person.PhoneNumber.newBuilder().setNumber("1234555")
								.setType(0))
				.setPhone(
						Person.PhoneNumber.newBuilder().setNumber("1234556")
								.setType(1)).build();
		try {
			FileOutputStream output = new FileOutputStream("example.txt");
			person1.writeTo(output);
			output.close();
		} catch (Exception e) {
			System.out.println("Write Error!");
		}
		try {
			FileInputStream input = new FileInputStream("example.txt");
			Person person2 = Person.parseFrom(input);
			System.out.println("person2:" + person2);
		} catch (Exception e) {
			System.out.println("Read Error!");
		}
	}
}
