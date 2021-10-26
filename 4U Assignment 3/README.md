(1) The tomogatchi question from your lesson 2.0 on classes. DO NOT OUTPUT the actual number values of the tomogatchi.

Example output:

```text

Bob: 50 Peckish ** meh ** You're not the boss of me

```

Example input:

```java

public static void main(String[] args) {
    Tamagotchi Bob = new Tamagotchi("Bob");
    System.out.println(Bob.toString());

    for(int i=0;i<8;i++) {
        Bob.ages();
        Bob.scold();

        if (i%2==0) {
            Bob.feed();
        }
    }
    System.out.println(Bob.toString());
}

```

(2) Create a class called Deck that has 2 methods. The first is called shuffle which will shuffle the order of your entire deck (all 52 cards which are strings). The other method is called toString() which takes in the deck as a parameter and displays the entire deck in its new shuffled order.

```text

Jack of Spades
4 of Diamonds
Ace of Clubs

```

(3) Create a class similar to number 2 using an arrayList, expect this class has 3 methods. It has the same shuffle and toString as the last question but it also has a method called Deal, which deals out 2 hands of 5 cards (which are given after shuffling). Reminder: you must deal each player one card at a time.

Make two smaller arrays to hold each

```text

Hand 1 has [Jack of Spades, Ace of Clubs, 5 of Diamonds]
Hand 2

```
