Recursion - the act of using a method to call itself over and over.

Methods: Any user defined functions taking in parameters and giving a return value in a class.

Ex:

```java
static int fact(int a) {
	if (a == 1) {
		return 1; // "base case" breaks out of the recursion
	}
	return a * fact (a - 1);
}
```

The above method defines a recursive way to get the factorial of a positive integer. Below is another example for calculating the fibonacci series up to n:

```java
static int fibs(int n) {
	if (a == 1 || a == 2) {
		return 1;
	} else {
		return fibs(a - 2) + fibs(a - 1);
	}
}
```

Recursion is only useful when there is a set pattern that can be solved using repetition.
