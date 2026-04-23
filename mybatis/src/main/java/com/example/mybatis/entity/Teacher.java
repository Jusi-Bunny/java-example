package com.example.mybatis.entity;

import lombok.Data;

import java.util.List;

@Data
public class Teacher {

    private Long teacherId;

    private String teacherName;

    private List<Student> students;
}