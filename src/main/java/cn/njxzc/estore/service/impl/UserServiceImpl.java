package cn.njxzc.estore.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.njxzc.estore.dao.IRoleDao;
import cn.njxzc.estore.dao.IUserDao;
import cn.njxzc.estore.dto.UserDto;
import cn.njxzc.estore.entity.Role;
import cn.njxzc.estore.entity.User;
import cn.njxzc.estore.service.IUserService;
import cn.njxzc.estore.utils.Constants;
import cn.njxzc.estore.utils.RedisDao;

@Service(value = "userService")
public class UserServiceImpl implements IUserService {
	
	@Autowired
	private IUserDao userDao;
	
	@Autowired
    private IRoleDao roleDao;
	
	@Autowired
	private RedisDao redisDao;

	@Override
	public UserDto userLogin(String username, String password) {
		// TODO Auto-generated method stub
		String message = "";
		User user = new User();
		UserDto userDto;
		user = userDao.findByUsername(username);
		String token = "";
		if (user == null) {
			message = "该用户不存在";
		} else if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {
			message = "用户名或密码错误";
		} else {
			token = UUID.randomUUID().toString();
			redisDao.setKey("session_" + token, JSON.toJSONString(user), 3600);
			message = "登录成功";
		}
		userDto = new UserDto(user, message, token, 1);
		return userDto;
	}

	@Override
	public String checkLogin(String token) {
		// TODO Auto-generated method stub
		return redisDao.get("session_" + token);
	}

	@Override
	public boolean updatePassword(Long id, String oldPassword, String newPassword) {
		// TODO Auto-generated method stub
		// 判null
		if (null == oldPassword || null == newPassword) {
			return false;
		}
		// 判空
		if ("".equals(oldPassword) || "".equals(newPassword)) {
			return false;
		}
		User realUser = userDao.findById(id);
		// 旧密码校验
		if (!realUser.getPassword().equals(DigestUtils.md5DigestAsHex(oldPassword.getBytes()))) {
			return false;
		}
		try {
			userDao.updatePassword(id, DigestUtils.md5DigestAsHex(newPassword.getBytes()));
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	@Override
	public boolean insertNew(User user) {
		// TODO Auto-generated method stub
		String password = user.getPassword();
		// MD5 加密
		user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
		// 设置默认头像
		user.setAvatar(Constants.COMMON_AVATAR);
		try {
			userDao.registerNew(user);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	@Override
	public boolean logout(String token) {
		// TODO Auto-generated method stub
		try {
			redisDao.delKey("session_" + token);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	@Override
	public Page<User> getAllByPage(int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNo, pageSize);
		return userDao.findAll();
	}

	@Override
	@Transactional
	public boolean deleteUser(long id) {
		// TODO Auto-generated method stub
		try {
			userDao.deleteUser(id);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

    @Override
    public String getRoleType(int roleId) {
        // TODO Auto-generated method stub
        Role role = roleDao.getRoleById(roleId);
        return role.getRoleType();
    }

}
