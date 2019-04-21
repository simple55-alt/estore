package cn.njxzc.estore.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.alibaba.fastjson.JSON;

import cn.njxzc.estore.dao.IUserDao;
import cn.njxzc.estore.dto.UserDto;
import cn.njxzc.estore.entity.User;
import cn.njxzc.estore.service.ILoginService;
import cn.njxzc.estore.utils.RedisDao;

@Service(value = "loginService")
public class LoginServiceImpl implements ILoginService {
	
	@Autowired
	private IUserDao userDao;
	
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
		user.setAvatar("" + user.getAvatar());
		userDto = new UserDto(user, message, token, 1);
		return userDto;
	}

	@Override
	public String checkLogin(String token) {
		// TODO Auto-generated method stub
		return redisDao.get("session_" + token);
	}

}
