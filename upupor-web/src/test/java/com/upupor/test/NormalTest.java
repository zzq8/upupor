package com.upupor.test;

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class NormalTest {

    class User{
        private String name;
    }
    @Test
    public void test01() throws CloneNotSupportedException {
        UuIdTest uuIdTest = new UuIdTest();
        this.clone();

    }
}


@Data
class Person implements Cloneable {
    private String name;
    private List<String> hobbies;

    public Person(String name, List<String> hobbies) {
        this.name = name;
        this.hobbies = hobbies;
    }

    public void addHobby(String hobby) {
        hobbies.add(hobby);
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    @Override
    public Person clone() throws CloneNotSupportedException {
        List<String> hobbiesCopy = new ArrayList<>(hobbies); // 创建新的列表副本
        return new Person(name, hobbiesCopy); // 创建新的Person对象
    }


    public static void main(String[] args) throws CloneNotSupportedException {
        List<String> hobbies = new ArrayList<>();
        hobbies.add("Reading");
        hobbies.add("Painting");

        Person original = new Person("Alice", hobbies);
        Person deepCopy = original.clone();

        System.out.println(original.getHobbies());       // [Reading, Painting]
        System.out.println(deepCopy.getHobbies());       // [Reading, Painting]

//        original.addHobby("Cooking");
        original.setName("Alice2");

        System.out.println(original);       // [Reading, Painting, Cooking]
        System.out.println(deepCopy);       // [Reading, Painting]
    }
}
