Mocking is the act of removing external dependencies from a unit test in order to create a controlled environment around it. Typically, we mock all other classes that interact with the class that we want to test. Common targets for mocking are:

1. Database connections,
2. Web services,
3. Classes that are slow,
4. Classes with side effects, and
5. Classes with non-deterministic behavior

For example, think of a Java class that communicates with an external payment provider, e.g. Paypal. There is no need to actually connect to the payment provider each time the unit test runs. It would be dangerous to test code that charges credit cards using a live payment environment. It would also make the unit test non-deterministic, e.g. if the payment provider is down for some reason.

Mocks and stubs are fake Java classes that replace these external dependencies. These fake classes are then instructed before the test starts to behave as you expect. More specifically:

A mock is a fake class that can be examined after the test is finished for its interactions with the class under test. For example, you can ask it whether a method was called or how many times it was called. Typical mocks are classes with side effects that need to be examined, e.g. a class that sends emails or sends data to another external service.