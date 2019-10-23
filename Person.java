public abstract class Person {

    protected String surname;

    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Person(String surname) {
        this.surname = surname;
    }

    public abstract int qualification();

    @Override
    public String toString() {
        return "Person{" +
                "surname='" + surname + '\'' +
                '}';
    }
}
