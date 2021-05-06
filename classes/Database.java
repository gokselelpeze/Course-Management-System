package classes;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Database {
    // K : CourseName, V : Course
    private HashMap<String,Course> courseHashMap;
    // K : Username, V : Student
    private HashMap<String,Student> studentHashMap;
    // K : Username, V : Teacher
    private HashMap<String,Teacher> teacherHashMap;
    // K : Username, V : User
    private HashMap<String,User> userHashMap;
    // K : CourseName, V : Posts
    private HashMap<String,ArrayList<Post>> postHashMap;
    // K : Sender, V : (K:Receiver V: Chat)
    private HashMap<String,HashMap<String,Chat>> messageHashMap;

    private ArrayList<Course> courseArrayList;
    private ArrayList<Student> studentArrayList;
    private ArrayList<Teacher> teacherArrayList;
    private ArrayList<User> userArrayList;
    private ArrayList<ArrayList<Post>> postArrayList;
    private ArrayList<ArrayList<Chat>> messageArrayList;

    public Database() throws IOException {
        courseHashMap = new HashMap<>();
        studentHashMap = new HashMap<>();
        teacherHashMap = new HashMap<>();
        userHashMap = new HashMap<>();
        postHashMap = new HashMap<>();
        courseArrayList = new ArrayList<>();
        studentArrayList = new ArrayList<>();
        teacherArrayList = new ArrayList<>();
        userArrayList = new ArrayList<>();
        postArrayList = new ArrayList<>();
        messageArrayList = new ArrayList<>();
        messageHashMap = new HashMap<>();
        readData();

    }

    public void readData() throws IOException {
        readTeachers();
        readCourses();
        readStudents();
        readPosts();
        readMessage();
    }

    public void saveData() throws IOException {
        saveStudents();
        saveCourses();
        saveTeachers();
        savePosts();
        saveMessage();
    }

    public void readStudents() throws IOException {
        File file = new File("src/database/student.txt");

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        while ((st = br.readLine()) != null) {
            String[] temp = st.split("-");
            String[] info = temp[0].split(",");
            String[] courses = temp[1].split(",");
            System.out.println("------------------------------------------");
            System.out.println("Name: " + info[0]);
            System.out.println("UserName: " + info[1]);
            System.out.println("Password: " + info[2]);

            Student student = new Student(info[0], info[1], info[2]);
            addStudent(student);

            System.out.println("Student " + info[0] + " added to system");

            for (String course : courses) {
                addStudentToCourse(student, course);
                System.out.println("Student " + info[0] + " added to " + course);
            }
            System.out.println("------------------------------------------");
        }
    }

    public void saveStudents() throws IOException {
        FileWriter file = new FileWriter("src/database/student.txt");
        String data = "";
        String courses = "";
        for (Student student : studentArrayList) {
            courses = "";
            for (String courseName : student.getCourseNames()) {
                courses += courseName + ",";
            }
            courses = courses.substring(0, courses.length() - 1);
            data += student.getName() + "," + student.getUsername() + "," + student.getPassword() + "-" + courses + "\n";

        }

        file.write(data);
        file.close();
        System.out.println("----------STUDENTS ARE SAVED------------");
        System.out.println("------------------------------------------");
    }

    public void readCourses() throws IOException {
        File file = new File("src/database/course.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        while ((st = br.readLine()) != null) {
            String[] info = st.split(",");
            System.out.println("------------------------------------------");
            System.out.println("ID: " + info[0]);
            System.out.println("Course Name: " + info[1]);
            System.out.println("Teacher: " + info[2]);
            addCourse(info[0], info[1], teacherHashMap.get(info[2]));

            System.out.println(info[0] + " - " + info[1] + " added to Courses");
            System.out.println("------------------------------------------");
        }
    }

    public void saveCourses() throws IOException {
        FileWriter file = new FileWriter("src/database/course.txt");
        String data = "";
        for (Course course : courseArrayList) {
            data += course.getId() + "," + course.getName() + "," + course.getTeacher().getUsername() + "\n";
        }
        file.write(data);
        file.close();
        System.out.println("----------COURSES ARE SAVED------------");
        System.out.println("------------------------------------------");
    }

    public void readTeachers() throws IOException {
        File file = new File("src/database/teacher.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        while ((st = br.readLine()) != null) {
            String[] info = st.split(",");
            System.out.println("------------------------------------------");
            System.out.println("Name: " + info[0]);
            System.out.println("UserName: " + info[1]);
            System.out.println("Password: " + info[2]);

            Teacher teacher = new Teacher(info[0], info[1], info[2]);
            addTeacher(teacher);

            System.out.println("Teacher " + info[0] + " added to system");
            System.out.println("------------------------------------------");
        }
    }

    public void saveTeachers() throws IOException {
        FileWriter file = new FileWriter("src/database/teacher.txt");
        String data = "";
        for (Teacher teacher : teacherArrayList) {
            data += teacher.getName() + "," + teacher.getUsername() + "," + teacher.getPassword() + "\n";

        }

        file.write(data);
        file.close();
        System.out.println("----------TEACHERS ARE SAVED------------");
        System.out.println("------------------------------------------");
    }

    public void readPosts() throws IOException {
        File file = new File("src/database/post.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        while ((st = br.readLine()) != null) {
            String[] temp = st.split("-");
            String courseName = temp[0];
            System.out.println("Reading" + temp[0]);
            String[] posts = temp[1].split(";");
            ArrayList<Post> postList = new ArrayList<>();
            for (String post : posts) {
                System.out.println(post);
                String[] tempPost = post.split("\\|");

                String[] data = tempPost[0].split(",");
                System.out.println(tempPost[0]);
                String author = data[0];
                String content = data[1];
                Post newPost = new Post(author, content);
                if (tempPost.length > 1) {
                    String[] comments = tempPost[1].split("~");

                    for (String comment : comments) {
                        String[] tempComment = comment.split(",");
                        Comment c = new Comment(tempComment[0], tempComment[1], tempComment[2]);
                        newPost.getComments().add(c);
                    }
                }

                postList.add(newPost);
            }
            postHashMap.put(courseName, postList);
            System.out.println("------------------------------------------");

            System.out.println("------------------------------------------");

        }
    }

    public void savePosts() throws IOException {
        FileWriter file = new FileWriter("src/database/post.txt");
        String data = "";
        for (Course course : courseArrayList) {
            if (postHashMap.get(course.getName()) != null) {
                ArrayList<Post> posts = postHashMap.get(course.getName());
                data += course.getName() + "-";
                for (Post post : posts) {
                    data += post.getAuthor() + "," + post.getContent() + "|";
                    for (Comment comment : post.getComments()) {
                        data += comment.getAuthor() + "," + comment.getComment() + "," + comment.getDate() + "~";
                    }
                    data += ";";
                }
                data += "\n";
            }

        }

        file.write(data);
        file.close();
        System.out.println("----------POSTS ARE SAVED------------");
        System.out.println("------------------------------------------");
    }

    public void readMessage() throws IOException {
        File file = new File("src/database/message.txt");

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        HashMap<String,Chat> tempChats = new HashMap<>();
        ArrayList<Chat> tempChats2 = new ArrayList<>();

        while ((st = br.readLine()) != null) {
            String[] temp = st.split("-");
            String[] info = temp[0].split(",");
            String[] chatList = temp[1].split("\\|");
            System.out.println("------------------------------------------");
            System.out.println("Sender: " + info[0]);
            System.out.println("Receiver: " + info[1]);
            ArrayList<Message> tempMessages = new ArrayList<>();
            for (String chats : chatList) {
                String[] messageList = chats.split(";");
                for (String mess : messageList) {
                    String[] data = mess.split(",");
                    // Message created.
                    Message message = new Message(data[0], info[1], data[1], data[2]);
                    tempMessages.add(message);
                }
                Chat chat = new Chat(info[0], info[1], tempMessages);
                tempChats.put(info[1], chat);
                tempChats2.add(chat);
            }
            messageHashMap.put(info[0], tempChats);
            messageArrayList.add(tempChats2);


            System.out.println("------------------------------------------");
        }
    }

    public void saveMessage() throws IOException {
        FileWriter file = new FileWriter("src/database/message.txt");
        String data = "";
        for (ArrayList<Chat> chats : messageArrayList) {
            for (Chat chat : chats) {
                data += chat.getSender() + "," + chat.getReceiver() + "-";
                for (Message message : chat.getChat()) {
                    data += message.getSender() + "," + message.getText() + "," + message.getDate() + ";";
                }
                data += "|";
            }
            data += "\n";
        }

        file.write(data);
        file.close();
        System.out.println("----------POSTS ARE SAVED------------");
        System.out.println("------------------------------------------");
    }


    public void addStudent(Student student) {
        userHashMap.put(student.getUsername(), student);
        studentHashMap.put(student.getUsername(), student);
        studentArrayList.add(student);
        userArrayList.add(student);
    }

    public void deleteStudent(Student student) {
        userHashMap.remove(student.getUsername(), student);
        studentHashMap.remove(student.getUsername(), student);
        studentArrayList.remove(student);

    }

    public void addStudentToCourse(Student student, String courseName) {
        student.addCourse(courseName);
        courseHashMap.get(courseName).addStudent(student);
    }

    public void addTeacher(Teacher teacher) {
        userHashMap.put(teacher.getUsername(), teacher);
        teacherHashMap.put(teacher.getUsername(), teacher);
        teacherArrayList.add(teacher);
        userArrayList.add(teacher);

    }

    // Editing course teacher
    public void editTeacher(String courseName, Teacher newTeacher) {
        courseHashMap.get(courseName).setTeacher(newTeacher);

    }

    public void deleteTeacher(Teacher teacher) {
        userHashMap.remove(teacher.getUsername(), teacher);
        teacherHashMap.remove(teacher.getUsername(), teacher);
        teacherArrayList.remove(teacher);

    }

    // Adding new course
    public void addCourse(String id, String courseName, Teacher teacher) {
        Course course = new Course(id, courseName, teacher);
        courseHashMap.put(courseName, course);
        courseArrayList.add(course);
        teacher.addCourse(courseName);
    }

    // Editing course name
    public void editCourseName(String oldCourseName, String id, String newCourseName) {
        Course course = courseHashMap.get(oldCourseName);
        courseArrayList.remove(course);
        course.setName(newCourseName);
        courseArrayList.add(course);
    }


    // Deleting course
    public void deleteCourse(String courseName) {
        System.out.println(courseName);
        ArrayList<Student> students = courseHashMap.get(courseName).getStudents();
        Teacher teacher = courseHashMap.get(courseName).getTeacher();
        System.out.println(courseHashMap.containsKey(courseName));
        System.out.println(teacher.getCourseNames().toString());
        for (Student student : students) {
            if (!student.getCourseNames().isEmpty()) {
                student.getCourseNames().removeIf(course -> course.equals(courseName));
            }
        }
        teacher.getCourseNames().removeIf(course -> course.equals(courseName));
        courseArrayList.remove(courseHashMap.get(courseName));
        courseHashMap.remove(courseName);
        System.out.println(courseHashMap.containsKey(courseName));
        System.out.println(teacher.getCourseNames().toString());
    }

    public void addMessage(String sender, String receiver, Message message) {
        if (messageHashMap.containsKey(sender) && messageHashMap.get(sender).get(receiver) != null) {
            HashMap<String,Chat> chats = messageHashMap.get(sender);
            Chat chat = chats.get(receiver);
            chat.getChat().add(message);
            HashMap<String,Chat> chats2 = messageHashMap.get(receiver);
            Chat chat2 = chats2.get(sender);
            chat2.getChat().add(message);
        } else {
            //Sender
            HashMap<String,Chat> chatsHashMap = new HashMap<>();
            ArrayList<Chat> chats = new ArrayList<>();
            ArrayList<Message> messages = new ArrayList<>();
            messages.add(message);
            Chat chat = new Chat(sender, receiver, messages);

            chats.add(chat);
            chatsHashMap.put(receiver, chat);
            messageHashMap.put(sender, chatsHashMap);
            messageArrayList.add(chats);
            //Receiver
            HashMap<String,Chat> chatsHashMap2 = new HashMap<>();
            ArrayList<Chat> chats2 = new ArrayList<>();
            ArrayList<Message> messages2 = new ArrayList<>();
            messages.add(message);
            Chat chat2 = new Chat(receiver, sender, messages);

            chats2.add(chat2);
            chatsHashMap2.put(sender, chat2);
            messageHashMap.put(receiver, chatsHashMap2);
            messageArrayList.add(chats2);
        }

    }

    public HashMap<String,Course> getCourseHashMap() {
        return courseHashMap;
    }

    public void setCourseHashMap(HashMap<String,Course> courseHashMap) {
        this.courseHashMap = courseHashMap;
    }

    public HashMap<String,Student> getStudentHashMap() {
        return studentHashMap;
    }

    public void setStudentHashMap(HashMap<String,Student> studentHashMap) {
        this.studentHashMap = studentHashMap;
    }

    public HashMap<String,Teacher> getTeacherHashMap() {
        return teacherHashMap;
    }

    public void setTeacherHashMap(HashMap<String,Teacher> teacherHashMap) {
        this.teacherHashMap = teacherHashMap;
    }

    public HashMap<String,User> getUserHashMap() {
        return userHashMap;
    }

    public void setUserHashMap(HashMap<String,User> userHashMap) {
        this.userHashMap = userHashMap;
    }

    public User getUser(String username) {
        return userHashMap.get(username);
    }

    public HashMap<String,ArrayList<Post>> getPostHashMap() {
        return postHashMap;
    }

    public void setPostHashMap(HashMap<String,ArrayList<Post>> postHashMap) {
        this.postHashMap = postHashMap;
    }


    public ArrayList<Course> getCourseArrayList() {
        return courseArrayList;
    }

    public void setCourseArrayList(ArrayList<Course> courseArrayList) {
        this.courseArrayList = courseArrayList;
    }

    public ArrayList<Student> getStudentArrayList() {
        return studentArrayList;
    }

    public void setStudentArrayList(ArrayList<Student> studentArrayList) {
        this.studentArrayList = studentArrayList;
    }

    public ArrayList<Teacher> getTeacherArrayList() {
        return teacherArrayList;
    }

    public void setTeacherArrayList(ArrayList<Teacher> teacherArrayList) {
        this.teacherArrayList = teacherArrayList;
    }

    public ArrayList<User> getUserArrayList() {
        return userArrayList;
    }

    public void setUserArrayList(ArrayList<User> userArrayList) {
        this.userArrayList = userArrayList;
    }

    public ArrayList<ArrayList<Post>> getPostArrayList() {
        return postArrayList;
    }

    public void setPostArrayList(ArrayList<ArrayList<Post>> postArrayList) {
        this.postArrayList = postArrayList;
    }


    public HashMap<String,HashMap<String,Chat>> getMessageHashMap() {
        return messageHashMap;
    }

    public void setMessageHashMap(HashMap<String,HashMap<String,Chat>> messageHashMap) {
        this.messageHashMap = messageHashMap;
    }

    public ArrayList<ArrayList<Chat>> getMessageArrayList() {
        return messageArrayList;
    }

    public void setMessageArrayList(ArrayList<ArrayList<Chat>> messageArrayList) {
        this.messageArrayList = messageArrayList;
    }
}