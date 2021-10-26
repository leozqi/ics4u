Class rules:

1. We must follow the 3 stages of objects: Declare, Define, Methods
2. The data we use are called "fields"
3. The functions in our class are called methods.

```java
class Person {
        String name;
        int age; // these are fields

        public void hi() {
                System.out.println("Hi I'm " + name);
        }
}
```

* What is it called? Person
* What is unique to it? name, age
* What does it do? Say hi

We must have preset values in each field. The **constructor** of a class automatically asigns values to each field when the class is created.

A class called `dog`, with an `age` and a `height` with a method called `bark`

```java
class Dog {
        int age;
        int height;

        public Dog(int age, int height) {
                this.age = age;
                this.height = height;
        }

        public void bark() {
                if ((this.age > 2) || (this.height > 5)) {
                        System.out.println("WOOF!");
                } else {
                        System.out.println("Woof!");
                }
        }
}
```

DO NOT TRUST OTHER PROGRAMMERS.

We can set our classes to either public or private, and same with methods.

* `private`: only methods in the same class can access the field (data). This is the default.
* `public`: any class can access the field

## The `tostring()` method

A method for objects to print out as a string some of their contents.
