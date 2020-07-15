package com.example.hlsiidb.dao;

//import com.example.hlsiidb.entity.Role;
//import com.example.hlsiidb.entity.User;
//import com.example.hlsiidb.entity.UserRole;

import com.commonuser.entity.Register;
import com.commonuser.entity.User;
import com.commonuser.entity.UserRole;

import com.example.hlsiidb.entity.UserQuery;
import com.example.hlsiidb.vo.ReturnCode;
//import com.mysql.cj.result.Row;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sun.nio.cs.US_ASCII;

import javax.annotation.Resource;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Component
public class UserDao {


    @Resource(name = "jdbcTemplateTwo")
    JdbcTemplate jdbcTemplate;


    @Autowired
    private RegisterDao registerDao;

//    用户信息查询方法

    /**
     * 通过id查询用户所有信息
     *
     * @param id
     * @return
     */
    public User findByUserId(int id) {
        String sql = "select * from user where id = '" + id + "'";
        try {
            return jdbcTemplate.queryForObject(sql, new userRowMapper());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据用户名得到用户id
     */
    public int getIdByUserName(String userName) {
        String sql = "select id from user where user_name='" + userName + "'";
        int id = -1;
        try {
            id = jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    /**
     * 根据用户名获取获取信息
     */
    public User findByUsername(String userName) {
        String sql = "select * from user where user_name='" + userName + "'";
        User user = null;
        try {
            user = jdbcTemplate.queryForObject(sql, new userRowMapper());
            return user;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return user;
    }


    /**
     * 通过用户id查询用户所有信息
     *
     * @param id
     * @return
     */
//    public User findById(int id) {
//        String sql = "select * from user where id = " + id;
//        return jdbcTemplate.queryForObject(sql, new userRowMapper());
//    }

    /**
     * 通过账号密码查询用户所有信息
     *
     * @param userName
     * @param pwd
     * @return
     */
    public User findByUsernameAndPwd(String userName, String pwd) {
        String sql = "select * from user where user_name = '" + userName + "' and password = '" + pwd + "'";
        return jdbcTemplate.queryForObject(sql, new userRowMapper());
    }

    /**
     * 查询所有用户信息
     *
     * @return
     */
    public List<User> findAllUser() {
        String sql = "select * from user";
        return jdbcTemplate.query(sql, new userRowMapper());
    }

//    用户信息管理方法

    /**
     * 插入新的用户信息:username作为salt
     *
     * @param username
     * @param password
     * @param createTime
     * @return
     */
//    public boolean insertUser(String username, String password, String createTime) {
//        String sql = "insert into user(username, password, salt, create_time) values(?,?,?,?)";
//        try {
//            String pwd = encrypt(password, username);
//            jdbcTemplate.update(sql, username, pwd, username, createTime);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

    /**
     * 通过username更新存在的用户信息
     *
     * @param userName
     * @param
     * @param email
     * @return
     */
    public boolean updateUser(String userName,String organization, String department, String email) {
        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String createTime = sp.format(new Date());
        String sql = "update user set organization=?,department=?,email=?,create_time = ? where user_name = ?";
        try {
            int flag = jdbcTemplate.update(sql, organization, department, email, createTime, userName);
            if (flag > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 通过id删除用户信息
     *
     * @param id
     * @return
     */
    public boolean deleteUser(int id) {
        String sql = "delete from user where id = ?";
        try {
            jdbcTemplate.update(sql, id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


//    用户角色关联方法

    /**
     * 获取所有用户角色关系
     * @return
     */
/*    public List<UserRole> getAllUR(){
        String sql = "select * from user_role";
        return jdbcTemplate.query(sql, new urRowMapper());
    }*/

    /**
     * 通过用户名获取该用户角色关系
     * @return
     */
/*    public List<UserRole> getUserUR(int user_id){
        String sql = "select * from user_role where user_id = " + user_id;
        return jdbcTemplate.query(sql, new urRowMapper());
    }*/

    /**
     * 插入用户角色关联内容
     *
     * @param user_id
     * @param role_id
     * @return
     */
    public boolean addUR(int user_id, int role_id) {
        String sql = "insert into user_role(user_id, role_id) values(?,?)";
        try {
            jdbcTemplate.update(sql, user_id, role_id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 通过userid和roleid修改用户角色关联表
     *
     * @param user_id
     * @param role_id_old
     * @param role_id_new
     * @return
     */
    public boolean updateUR(int user_id, int role_id_old, int role_id_new) {
        String sql = "update user_role set role_id = ? where user_id = ? and role_id = ?";
        try {
            jdbcTemplate.update(sql, role_id_new, user_id, role_id_old);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 通过userid和roleid删除用户角色关联表
     *
     * @param user_id
     * @param role_id
     * @return
     */
    public boolean deleteUR(int user_id, int role_id) {
        String sql = "delete from user_role where user_id = ? and role_id = ?";
        try {
            int flag = jdbcTemplate.update(sql, user_id, role_id);
            if (flag > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 通过userid删除用户角色关联表
     *
     * @param userid
     * @return
     */
    public boolean deleteUR(int userid) {
        String sql = "delete from user_role where user_id = ?";
        try {
            int flag = jdbcTemplate.update(sql, userid);
            if (flag > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

// 加密方法

    /**
     * 加密函数:采用MD5，加盐，迭代2次
     *
     * @return
     */
    public String encrypt(String pwd, String salt) {
        Object s = ByteSource.Util.bytes(salt);
        SimpleHash simpleHash = new SimpleHash("MD5", pwd, s, 2);
        return simpleHash.toString();
    }


    /**
     * add register table information into user
     */
    public boolean addRegisterToUser(int id, String userType) {

        Register register = registerDao.getRegisterByUserName(id);
        String userName = register.getUserName();
        String password = register.getPassword();
        String organization = register.getOrganization();
        String department = register.getDepartment();
        String email = register.getEmail();
        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String createTime = sp.format(new Date());
        String sql = "insert into user(user_name,password,organization,department,email,salt,create_time) " +
                "values('" + userName + "','" + password + "','" + organization + "','" + department + "','" + email + "'," +
                "'" + userName + "','" + createTime + "')";
        int type = 0;
        if (userType.equals("admin")) {
            type = 1;
        } else {
            type = 2;
        }

        try {
            jdbcTemplate.update(sql);
            int userId = getIdByUserName(userName);
            String sql2 = "insert into user_role(user_id,role_id) values('" + userId + "','" + type + "')";
            jdbcTemplate.update(sql2);
            registerDao.deleteRegister(userName);
            registerDao.sendEmail(email);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean changeUserType(int userId, String type) {
        int roleType = 0;
        if (type.equals("admin")) {
            roleType = 1;
        }
        if (type.equals("user")) {
            roleType = 2;
        }
        String sql = "update user_role set role_id='" + roleType + "' where user_id='" + userId + "'";
        try {
            jdbcTemplate.update(sql);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     *
     */
    public String findUserAndPwd(String email) {
        String sql = "select user_name from user where email='" + email + "'";
        try {
            String userName = jdbcTemplate.queryForObject(sql, String.class);
            return userName;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

//   用来封装的类


    class userRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("user_name"));
            user.setPassword(rs.getString("password"));
            user.setOrganization(rs.getString("organization"));
            user.setDepartment(rs.getString("department"));
            user.setEmail(rs.getString("email"));
            user.setSalt(rs.getString("salt"));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            try {
                user.setCreateTime(simpleDateFormat.parse(rs.getString("create_time")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return user;
        }
    }

    public List<String> getAllUserRegisteredUserName() {
        List<String> userNameList = null;
        String sql = "select user_name from register UNION select user_name from user";
        try {
            userNameList = jdbcTemplate.queryForList(sql, String.class);
            return userNameList;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return userNameList;
    }

    public List<String> getAllUserRegisteredEmail() {
        List<String> emailList = null;
        String sql = "select email from register UNION select email from user";
        try {
            emailList = jdbcTemplate.queryForList(sql, String.class);
            return emailList;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return emailList;
    }


    public boolean resetPwd(String userName, String pwd) {
        String encryptPwd = encrypt(pwd, userName);
        String sql = "update user set password='" + encryptPwd + "' where user_name='" + userName + "'";
        try {
            jdbcTemplate.execute(sql);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }


    public ReturnCode changePwd(String userName, String pwd) {
        String encryptPwd = encrypt(pwd, userName);
        String sql = "update user set password='" + encryptPwd + "' where user_name='" + userName + "'";
        try {
            jdbcTemplate.execute(sql);
            return new ReturnCode(true, "密码修改成功!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ReturnCode(true, "密码修改失败!");
    }


    public String forgetPwd(String userName) {
        int radomInt = new Random().nextInt(999999);
        String randomPwd = radomInt + "";
        String encryptPwd = encrypt(randomPwd, userName);

        String email = null;
        String sql1 = "select email from user where user_name='" + userName + "'";
        try {
            email = jdbcTemplate.queryForObject(sql1, String.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String sql2 = "update user set password='" + encryptPwd + "' where user_name='" + userName + "'";
        try {
            boolean success = registerDao.sendRandomPwd(email, "您的HLS-II账户密码重置为：" + randomPwd + "，请登录系统后修改密码");
            if (success) {
                jdbcTemplate.update(sql2);
                return "已将重置密码发送到你的注册邮箱:" + email;
            } else {
                return "邮件发送请求失败";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "修改密码请求失败!";
    }

    /**
     * 将数据库查询结果封装成实体类userRole对象的方法
     */
    class urRowMapper implements RowMapper<UserRole> {
        @Override
        public UserRole mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserRole userRole = new UserRole();
            userRole.setId(rs.getInt("id"));
            userRole.setUserId(rs.getInt("user_id"));
            userRole.setRoleId(rs.getInt("role_id"));
            userRole.setRemark(rs.getString("remarks"));
            return userRole;
        }
    }
}
