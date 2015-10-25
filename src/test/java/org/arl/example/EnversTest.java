package org.arl.example;

import java.util.Arrays;

import org.arl.example.entity.Person;
import org.arl.example.util.Crud;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.Assert;

public class EnversTest {

    public static Long entityId;

    @BeforeClass
    public static void beforeClass() {
        final Person person = new Person();
        person.setName("Antonio Lago");
        person.setAge(Integer.valueOf(29));
        entityId = Crud.salvar(person);
    }

    @Test
    public void shouldNotAuditAgeField() {

        final Person person = Crud.find(entityId, Person.class);
        person.setAge(25);
        Crud.salvar(person);

        final int count = Crud.countNative("SELECT COUNT(*) FROM Person_AUD WHERE age = ?", Arrays.asList(25));

        Assert.assertEquals(0, count);

    }

    @Test
    public void shouldAuditNameField() {

        final Person person = Crud.find(entityId, Person.class);
        person.setName("Antonio Rodrigo Lago");
        Crud.salvar(person);

        final int count = Crud.countNative("SELECT COUNT(*) FROM Person_AUD WHERE name = ?",
                Arrays.asList("Antonio Rodrigo Lago"));

        Assert.assertEquals(1, count);

    }

    @AfterClass
    public static void afterClass() {
        Crud.executeNative("DELETE FROM Person");
        Crud.executeNative("DELETE FROM Person_AUD");
        Crud.executeNative("DELETE FROM REVINFO");
    }

}
