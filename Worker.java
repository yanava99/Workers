import java.util.Comparator;

public class Worker extends Person implements Comparable<Worker>{

    private static final int DIVIDER = 330;

    private String position;
    private int salary;

    public Worker(String surname, String position, int salary) {
        super(surname);
        this.position = position;
        this.salary = salary;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    @Override
    public int qualification() {
        return salary / DIVIDER;
    }

    @Override
    public int compareTo(Worker o) {
        return ((-1)*((Integer)this.getSalary()).compareTo(o.getSalary()));
    }

    public static Comparator<Worker> COMPARATOR = new Comparator<Worker>() {
        public int compare(Worker one, Worker other) {
            if(one.position.compareTo(other.position) != 0)
                return one.position.compareTo(other.position);
            else
                return one.surname.compareTo(other.surname);
        }
    };

    @Override
    public String toString() {
        return "Worker{" +
                super.toString() +
                ", position='" + position + '\'' +
                ", salary=" + salary +
                ", qualification=" + qualification() +
                '}';
    }
}
