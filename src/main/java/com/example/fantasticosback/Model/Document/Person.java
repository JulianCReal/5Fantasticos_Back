package com.example.fantasticosback.Model.Document;

public abstract class Person {

    protected String name;
    protected String lastName;
    protected int document;

    public Person() {
    }

    public Person(String name, String lastName, int document) {
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

    public int getDocument() {
        return document;
    }

    public void setDocument(int document) {
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
