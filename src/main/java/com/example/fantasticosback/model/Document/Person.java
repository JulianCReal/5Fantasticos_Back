package com.example.fantasticosback.model.Document;

public abstract class Person {

    protected String name;
    protected String lastName;
    protected String document;

    public Person() {
    }

    public Person(String name, String lastName, String document) {
        this.name = name;
        this.lastName = lastName;
        this.document = document;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", document=" + document +
                '}';
    }

    public abstract void showInformation();
}
