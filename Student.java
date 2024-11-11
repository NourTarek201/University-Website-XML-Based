public class Student {
    private String id;
    private String firstName;
    private String lastName;
    private String gender;
    private double gpa;
    private int level;
    private String address;

    public Student(String ID, String firstName, String lastName, String gender, double GPA, int level, String address) {
        this.id = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.gpa = GPA;
        this.level = level;
        this.address = address;
    }
}
