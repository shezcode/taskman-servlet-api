package com.shezcode.taskMan;

import com.shezcode.taskMan.dao.Database;
import com.shezcode.taskMan.dao.UserDao;
import com.shezcode.taskMan.domain.User;

import java.sql.SQLException;

public class Main {
    public static void main(String[]args){
        try {
            Database.connect();
            //User user = Database.jdbi.withExtension(UserDao.class, UserDao::getAdrianUser);
            //System.out.println(user);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
