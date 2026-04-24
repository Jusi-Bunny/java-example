package com.example.mybatis.mapper;

import com.example.mybatis.entity.Teacher;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TeacherMapper {


    /**
     * 查询所有讲师信息，并且查询每名讲师关联的学生信息
     *
     * @return 讲师列表
     */
    List<Teacher> selectAllTeacherList();
}