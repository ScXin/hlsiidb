package com.example.hlsiidb.dao;

//import com.example.hlsiidb.entity.Role;

import com.commonuser.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class RoleDao {
    @Autowired
    PermissionDao permissionDao;

    @Resource(name = "jdbcTemplateTwo")
    JdbcTemplate jdbcTemplate;

    /**
     * 通过userid查询对应的角色信息和权限信息
     * @param userId
     * @return
     */
    public Role findRoleListByUserId(int userId){

        String sql = "select ur.role_id as id, r.name as name, r.description as description" +
                " from user_role ur" +
                " left join role r on ur.role_id = r.id" +
                " where ur.user_id =" + userId;
        return jdbcTemplate.queryForObject(sql, new roleRowMapper1());
    }


    /**
     * 通过userid查询对应的角色信息
     * @param userId
     * @return
     */
    public Role findSimpleRoleListByUserId(int userId){
        String sql = "select ur.role_id as id, r.name as name, r.description as description" +
                " from user_role ur" +
                " left join role r on ur.role_id = r.id" +
                " where ur.user_id =" + userId;
        return jdbcTemplate.queryForObject(sql, new roleRowMapper2());
    }


    /**
     * 获取所有角色信息
     * @return
     */
    public List<Role> findAllRole(){
        String sql = "select * from role";
        return jdbcTemplate.query(sql, new roleRowMapper2());
    }


    public Role findRoleByRolename(String rolename){
        String sql = "select * from role where role.name = '" + rolename + "'";
        return jdbcTemplate.queryForObject(sql, new roleRowMapper2());
    }

    /**
     * 将数据库查询结果封装成实体类的role对象(包含权限信息)
     */
    class roleRowMapper1 implements RowMapper<Role>{
        @Override
        public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
            Role role = new Role();
            role.setId(rs.getInt("id"));
            role.setName(rs.getString("name"));
            role.setDescription(rs.getString("description"));
//            role.setPermissionList(permissionDao.findPermissionListByRoleId(rs.getInt("id")));
            return role;
        }
    }


    /**
     * 将数据库查询结果封装成实体类的role对象（只含有角色信息）
     */
    class roleRowMapper2 implements RowMapper<Role>{
        @Override
        public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
            Role role = new Role();
            role.setId(rs.getInt("id"));
            role.setName(rs.getString("name"));
            role.setDescription(rs.getString("description"));
            return role;
        }
    }
}
