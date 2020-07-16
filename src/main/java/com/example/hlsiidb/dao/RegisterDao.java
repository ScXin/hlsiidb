package com.example.hlsiidb.dao;

//import com.commonuser.entity.UserRole;

import com.commonuser.entity.Register;

import com.example.hlsiidb.util.EmailUtil;
import com.example.hlsiidb.vo.ReturnCode;
//import net.bytebuddy.asm.Advice;
//import org.apache.commons.mail.Email;
//import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
//import org.crazycake.shiro.RedisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

//import javax.swing.plaf.synth.Region;
import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author ScXin
 * @date 7/5/2020 6:16 PM
 */
@Component
public class RegisterDao {


    @Resource(name = "jdbcTemplateTwo")
    private JdbcTemplate jdbcTemplate;


    public List<Register> getAllRegisters() {
        String sql = "select * from register";
        List<Register> registerList = jdbcTemplate.query(sql, new RegisterMapper());
        return registerList;
    }

    public Register getRegisterByUserName(int id) {
        Register register = null;
        String sql = "select id,user_name,password,organization,department,email,register_time from register where id='" + id + "'";
        try {
            register = jdbcTemplate.queryForObject(sql, new RegisterMapper());
            return register;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return register;
    }

    public Register getRegisterByUserName(String userName) {
        Register register = null;
        String sql = "select id,user_name,password,organization,department,email,register_time from register where user_name='" + userName + "'";
        try {
            register = jdbcTemplate.queryForObject(sql, new RegisterMapper());
            return register;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return register;
    }

    public ReturnCode insertRegister(String userName, String password, String organization, String department,
                                     String email) {
        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String registerTime = sp.format(new Date());
        String pwd = encrypt(password, userName);
        String sql = "insert into register(user_name, password,organization,department,email,register_time) " +
                "values('" + userName + "','" + pwd + "','" + organization + "','" + department + "','" + email + "','" + registerTime + "')";
        try {
            jdbcTemplate.update(sql);
            sendEmailToAdmin(EmailUtil.adminAddr);
            return new ReturnCode(true, "注册成功，等待管理员审核！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ReturnCode(false, "注册失败！");
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


    public boolean deleteRegister(String userName) {
        String sql = "delete from register where user_name='" + userName + "'";
        try {
            jdbcTemplate.execute(sql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean deleteRegister(int id) {
        String sql = "delete from register where id='" + id + "'";
        try {
            jdbcTemplate.execute(sql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    class RegisterMapper implements RowMapper<Register> {
        @Override
        public Register mapRow(ResultSet rs, int rowNum) throws SQLException {
            Register register = new Register();
            register.setId(rs.getInt("id"));
            register.setUserName(rs.getString("user_name"));
            register.setPassword(rs.getString("password"));
            register.setOrganization(rs.getString("organization"));
            register.setDepartment(rs.getString("department"));
            register.setEmail(rs.getString("email"));
            SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            try {
                register.setRegisterTime(sp.parse(rs.getString("register_time")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return register;
        }
    }

    public void sendEmail(String to) {
        HtmlEmail email = new HtmlEmail();//创建一个HtmlEmail实例对象
        email.setHostName("mail.ustc.edu.cn");//邮箱的SMTP服务器，一般123邮箱的是smtp.123.com,qq邮箱为smtp.qq.com
        email.setCharset("utf-8");//设置发送的字符类型
        try {
            email.addTo(to);//设置收件人
            email.setFrom("brave@mail.ustc.edu.cn", "HLS-II System Service");//发送人的邮箱为自己的，用户名可以随便填
            email.setAuthentication("brave@mail.ustc.edu.cn", "******");//设置发送人到的邮箱和用户名和授权码(授权码是自己设置的)
            email.setSubject("注册成功！");//设置发送主题
            email.setMsg("您好！你注册的HLS-II账号已通过管理员审核并激活，使用注册时的用户和密码即可访问HLS-II网页系统！");//设置发送内容
            //由于邮件滥发等原因阿里云服务器禁用了25端口，所以这里得使用ssl加密传输（这样使用的端口号是465）
            email.setSSLOnConnect(true);
            email.send(); //发送邮件
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }

    public boolean sendEmailForFindUser(String to, String content) {
        HtmlEmail email = new HtmlEmail();//创建一个HtmlEmail实例对象
        email.setHostName("mail.ustc.edu.cn");//邮箱的SMTP服务器，一般123邮箱的是smtp.123.com,qq邮箱为smtp.qq.com
        email.setCharset("utf-8");//设置发送的字符类型
        try {
            email.addTo(to);//设置收件人
            email.setFrom("brave@mail.ustc.edu.cn", "HLS-II System Service");//发送人的邮箱为自己的，用户名可以随便填
            email.setAuthentication("brave@mail.ustc.edu.cn", "******");//设置发送人到的邮箱和用户名和授权码(授权码是自己设置的)
            email.setSubject("用户密码找回！");//设置发送主题
            email.setMsg(content);//设置发送内容
            //由于邮件滥发等原因阿里云服务器禁用了25端口，所以这里得使用ssl加密传输（这样使用的端口号是465）
            email.setSSLOnConnect(true);
            email.send(); //发送邮件
            return true;
        } catch (EmailException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean sendRandomPwd(String to, String content) {
        HtmlEmail email = new HtmlEmail();//创建一个HtmlEmail实例对象
        email.setHostName("mail.ustc.edu.cn");//邮箱的SMTP服务器，一般123邮箱的是smtp.123.com,qq邮箱为smtp.qq.com
        email.setCharset("utf-8");//设置发送的字符类型
        try {
            email.addTo(to);//设置收件人
            email.setFrom("brave@mail.ustc.edu.cn", "HLS-II System Service");//发送人的邮箱为自己的，用户名可以随便填
            email.setAuthentication("brave@mail.ustc.edu.cn", "******");//设置发送人到的邮箱和用户名和授权码(授权码是自己设置的)
            email.setSubject("账户密码重置！");//设置发送主题
            email.setMsg(content);//设置发送内容
            //由于邮件滥发等原因阿里云服务器禁用了25端口，所以这里得使用ssl加密传输（这样使用的端口号是465）
            email.setSSLOnConnect(true);
            email.send(); //发送邮件
            return true;
        } catch (EmailException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void sendEmailToAdmin(String to) {
        HtmlEmail email = new HtmlEmail();//创建一个HtmlEmail实例对象
        email.setHostName("mail.ustc.edu.cn");//邮箱的SMTP服务器，一般123邮箱的是smtp.123.com,qq邮箱为smtp.qq.com
        email.setCharset("utf-8");//设置发送的字符类型
        try {
            email.addTo(to);//设置收件人
            email.setFrom("brave@mail.ustc.edu.cn", "HLS-II System Service");//发送人的邮箱为自己的，用户名可以随便填
            email.setAuthentication("brave@mail.ustc.edu.cn", "******");//设置发送人到的邮箱和用户名和授权码(授权码是自己设置的)
            email.setSubject("人员注册！");//设置发送主题
            email.setMsg("HLS-II有新的注册用户啦，请及时处理！");//设置发送内容
            //由于邮件滥发等原因阿里云服务器禁用了25端口，所以这里得使用ssl加密传输（这样使用的端口号是465）
            email.setSSLOnConnect(true);
            email.send(); //发送邮件
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }
}
