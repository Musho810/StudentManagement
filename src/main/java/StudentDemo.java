import command.Commands;
import exception.LessonNotFoundException;
import model.Lesson;
import model.Student;
import model.User;
import model.UserType;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import storage.LessonStorage;
import storage.StudentStorage;
import storage.UserStorage;

import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

import static dateUtil.DateUtil.stringToDate;


public class StudentDemo implements Commands {

    private static Scanner scanner = new Scanner(System.in);
    private static StudentStorage studentStorage = new StudentStorage();
    private static LessonStorage lessonStorage = new LessonStorage();
    private static UserStorage userStorage = new UserStorage();
    private static User currentUser = null;

    public static void main(String[] args) {
        initData();

        boolean run = true;
        while (run) {
            Commands.printLoginCommands();
            int command;
            try {
                command = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                command = -1;
            }
            switch (command) {
                case EXIT -> run = false;
                case LOGIN -> login();
                case REGISTER -> register();
                default -> System.out.println("Invalid command");
            }

        }
    }

    private static void login() {
        System.out.println("Please input email,password");
        String emailPasswordStr = scanner.nextLine();
        String[] emailPassword = emailPasswordStr.split(",");
        User user = userStorage.getUserByEmail(emailPassword[0]);
        if (user == null) {
            System.out.println("User with " + emailPassword[0] + " does not exists!");
        } else {
            if (user.getPassword().equals(emailPassword[1])) {
                currentUser = user;
                if (user.getUserType() == UserType.ADMIN) {
                    loginAdmin();
                } else if (user.getUserType() == UserType.USER) {
                    loginUser();
                }
            } else {
                System.out.println("Password is wrong!");
            }
        }
    }

    private static void loginUser() {
        System.out.println("Welcome " + currentUser.getName());

        boolean run = true;
        while (run) {
            Commands.printUserCommands();
            int command;
            try {
                command = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                command = -1;
            }

            switch (command) {
                case EXIT -> run = false;
                case ADD_STUDENT -> addStudent();
                case PRINT_ALL_STUDENTS -> studentStorage.print();
                case PRINT_STUDENTS_BY_LESSON -> printStudentsByLessonName();
                case PRINT_STUDENT_COUNT -> System.out.println("students count:" + studentStorage.getSize());
                case PRINT_ALL_LESSONS -> lessonStorage.print();
                case DOWNLOAD_STUDENTS_EXCEL -> downloadStudentsExcel();
                default -> System.out.println("Invalid command");
            }

        }
    }

    private static void downloadStudentsExcel() {
        System.out.println("Please input file location");
        String fileDir = scanner.nextLine();
        try {
            studentStorage.writeStudentsToExcel(fileDir);

        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    private static void register() {
        System.out.println("please input name,surname,email,password");
        String userDataStr = scanner.nextLine();
        String[] userData = userDataStr.split(",");
        if (userData.length < 4) {
            System.out.println("Please input correct data!");
        } else {
            if (userStorage.getUserByEmail(userData[2]) == null) {
                User user = new User();
                user.setName(userData[0]);
                user.setSurname(userData[1]);
                user.setEmail(userData[2]);
                user.setPassword(userData[3]);
                user.setUserType(UserType.USER);
                userStorage.add(user);
                System.out.println("User created!");
            } else {
                System.out.println("user with " + userData[2] + " already exists!");
            }
        }

    }

    private static void loginAdmin() {
        System.out.println("Welcome " + currentUser.getName());
        boolean run = true;
        while (run) {
            Commands.printAdminCommands();
            int command;
            try {
                command = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                command = -1;
            }

            switch (command) {
                case EXIT -> run = false;
                case ADD_STUDENT -> addStudent();
                case PRINT_ALL_STUDENTS -> studentStorage.print();
                case DELETE_STUDENT_BY_INDEX -> deleteByIndex();
                case PRINT_STUDENTS_BY_LESSON -> printStudentsByLessonName();
                case PRINT_STUDENT_COUNT -> System.out.println("students count:" + studentStorage.getSize());
                case CHANGE_STUDENT_LESSON -> changeStudentLessonName();
                case ADD_LESSON -> addLesson();
                case PRINT_ALL_LESSONS -> lessonStorage.print();
                case DOWNLOAD_STUDENTS_EXCEL -> downloadStudentsExcel();
                default -> System.out.println("Invalid command");
            }

        }
    }

    private static void initData() {
        User admin = new User("admin", "admin", "admin@mail.com", "admin", UserType.ADMIN);
        userStorage.add(admin);
        Lesson java = new Lesson("java", "teacher java", 6, 999, stringToDate("03.05.2022"));
        Lesson sql = new Lesson("sql", "teacher sql", 5, 888, stringToDate("03.04.2022"));
        Lesson php = new Lesson("php", "teacher php", 8, 777,stringToDate("02.05.2022"));
        lessonStorage.add(java);
        lessonStorage.add(sql);
        lessonStorage.add(php);
        studentStorage.add(new Student("petros", "Petrossyan", 25, "12345432", "Gyumri", java, admin, new Date()));
        studentStorage.add(new Student("poxos", "poxosyan", 33, "12345432", "Gyumri", sql, admin, new Date()));
        studentStorage.add(new Student("kirakos", "kirakosyan", 44, "12345432", "Gyumri", php, admin, new Date()));
    }

    private static void addLesson() {
        System.out.println("Please input lesson name");
        String lessonName = scanner.nextLine();
        System.out.println("Please input lesson teacherName");
        String teacherName = scanner.nextLine();
        System.out.println("Please input lesson duration by month");
        int duration = Integer.parseInt(scanner.nextLine());
        System.out.println("Please input lesson price");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.println("Please input date (dd.mm.yy)");
        String dateStr = scanner.nextLine();

        Lesson lesson = new Lesson(lessonName, teacherName, duration, price, stringToDate(dateStr));
        lessonStorage.add(lesson);
        System.out.println("lesson created!");
    }

    private static void changeStudentLessonName() {
        studentStorage.print();
        System.out.println("please choose student index");
        int index = Integer.parseInt(scanner.nextLine());
        Student student = studentStorage.getStudentByIndex(index);
        if (student == null) {
            System.out.println("Wrong Index!!!");
            changeStudentLessonName();
        } else {
            if (lessonStorage.getSize() != 0) {
                lessonStorage.print();
                System.out.println("please choose lesson index");
                try {
                    int lessonIndex = Integer.parseInt(scanner.nextLine());
                    Lesson lesson = lessonStorage.getLessonByIndex(lessonIndex);
                    student.setLesson(lesson);
                    System.out.println("lesson changed!");
                } catch (LessonNotFoundException | NumberFormatException e) {
                    System.out.println(e.getMessage());
                    changeStudentLessonName();
                }

            }

        }
    }

    private static void printStudentsByLessonName() {
        System.out.println("Please input lesson name");
        String lessonName = scanner.nextLine();
        studentStorage.printStudentsByLessonName(lessonName);
    }

    private static void deleteByIndex() {
        studentStorage.print();
        System.out.println("please choose student index");
        int index = Integer.parseInt(scanner.nextLine());
        studentStorage.deleteByIndex(index);
    }

    private static void addStudent() {
        if (lessonStorage.getSize() != 0) {
            lessonStorage.print();
            System.out.println("please choose lesson index");

            Lesson lesson = null;
            try {
                int lessonIndex = Integer.parseInt(scanner.nextLine());

                lesson = lessonStorage.getLessonByIndex(lessonIndex);
                System.out.println("Please input student's name");
                String name = scanner.nextLine();
                System.out.println("Please input student's surname");
                String surname = scanner.nextLine();
                System.out.println("Please input student's phoneNumber");
                String phoneNumber = scanner.nextLine();
                System.out.println("Please input student's city");
                String city = scanner.nextLine();
                System.out.println("please input student's age");
                int age = Integer.parseInt(scanner.nextLine());

                Student student = new Student(name, surname, age, phoneNumber, city, lesson, currentUser, new Date());
                studentStorage.add(student);
                System.out.println("Student created");
            } catch (LessonNotFoundException | NumberFormatException e) {
                System.out.println("Please choose correct number or index!!!");
                addStudent();
            }
        }


    }
}