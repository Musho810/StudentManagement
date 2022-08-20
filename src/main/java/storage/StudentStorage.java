package storage;


import model.Student;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class StudentStorage {
    private Student[] array = new Student[10];
    private int size = 0;

    public void add(Student student) {
        if (size == array.length) {
            increaseArray();
        }
        array[size++] = student;
    }


    public void print() {
        for (int i = 0; i < size; i++) {
            System.out.println(i + ". " + array[i]);
        }
    }

    private void increaseArray() {
        Student[] temp = new Student[array.length + 10];
        System.arraycopy(array, 0, temp, 0, size);
        array = temp;
    }

    public void deleteByIndex(int index) {
        if (index < 0 || index >= size) {
            System.out.println("invalid index");
        } else {
            System.arraycopy(array, index + 1, array, index, size - index);
            size--;
        }
    }

    public void printStudentsByLessonName(String lessonName) {
        for (int i = 0; i < size; i++) {
            if (array[i].getLesson().getName().equals(lessonName)) {
                System.out.println(array[i]);
            }
        }
    }

    public int getSize() {
        return size;
    }

    public Student getStudentByIndex(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return array[index];
    }


    public void writeStudentsToExcel(String fileDir) throws IOException , InvalidFormatException {
        File directory = new File(fileDir);
        if (directory.isFile()) {
            throw new RuntimeException("file Dir must be a Directory!");
        }
        File excelFile = new File(directory, "students  " + System.currentTimeMillis() + ".xlsx");
        excelFile.createNewFile();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("students");
        Row headerRow = sheet.createRow(0);
        Cell nameCell = headerRow.createCell(0);
        nameCell.setCellValue("name");

        Cell surName = headerRow.createCell(1);
        surName.setCellValue("surname");

        Cell ageCell = headerRow.createCell(2);
        ageCell.setCellValue("age");

        Cell phoneNumberCell = headerRow.createCell(3);
        phoneNumberCell.setCellValue("phone");

        Cell cityCell = headerRow.createCell(4);
        cityCell.setCellValue("city");

        for (int i = 0; i < size; i++) {
            Student student = array[i];
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(student.getName());
            row.createCell(1).setCellValue(student.getSurname());
            row.createCell(2).setCellValue(student.getAge());
            row.createCell(3).setCellValue(student.getPhoneNumber());
            row.createCell(4).setCellValue(student.getCity());

        }
        workbook.write(new FileOutputStream(excelFile));
        System.out.println("Excell was created successfuly");

    }
}

