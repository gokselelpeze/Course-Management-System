package classes;

import java.util.ArrayList;

public class Student extends User {
    private static int _id = 2020510000;
    private int id;
    private ArrayList<String> courseNames;

    public Student(String name, String username, String password) {
        super(name, username, password);
        this.id = _id++;
        courseNames = new ArrayList<>();
    }

    public void addCourse(String courseName) {
        this.courseNames.add(courseName);
    }

    public void deleteCourse(String courseName) {
        this.courseNames.remove(courseName);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<String> getCourseNames() {
        return courseNames;
    }

    public void setCourseNames(ArrayList<String> courseNames) {
        this.courseNames = courseNames;
    }
}
