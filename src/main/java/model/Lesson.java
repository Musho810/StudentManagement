package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Lesson {

    private String name;
    private String teacherName;
    private int duration;
    private double price;
    private Date startDate;

}
