package com.example.mybatis.mapper;

import com.example.mybatis.entity.Teacher;

import java.util.List;

public interface TeacherMapper {


    /**
     * 查询所有讲师信息，并且查询每名讲师关联的学生信息
     *
     * @return 讲师列表
     */
    List<Teacher> selectAllTeacherList();
}