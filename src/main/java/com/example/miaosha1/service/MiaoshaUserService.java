package com.example.miaosha1.service;



import com.example.miaosha1.dao.MiaoshaUserDao;
import com.example.miaosha1.exception.GlobalException;
import com.example.miaosha1.redis.MiaoshaUserKey;
import com.example.miaosha1.vo.LoginVo;
import com.example.miaosha1.pojo.MiaoshaUser;
import com.example.miaosha1.redis.RedisService;
import com.example.miaosha1.result.CodeMsg;
import com.example.miaosha1.utils.MD5Util;
import com.example.miaosha1.utils.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoshaUserService {
    public static final String COOKIE_NAME_TOKEN ="token";

    @Autowired
    MiaoshaUserDao miaoShaUserDao;
    @Autowired
    RedisService redisService;
    //取缓存
    public MiaoshaUser getById(long id){
        MiaoshaUser user = redisService.get(MiaoshaUserKey.getById,""+id,MiaoshaUser.class);
        if (user != null){
            return user;
        }
        //取数据库
        user = miaoShaUserDao.getById(id);
        if(user != null){
            redisService.set(MiaoshaUserKey.getById,""+id,user);
        }
        return user;
    }
    public boolean updatePass(String token,long id,String passNew){
        MiaoshaUser user = getById(id);
        if (user == null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        MiaoshaUser toBeUpdate = new MiaoshaUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDbPass(passNew,user.getSalt()));
        miaoShaUserDao.update(toBeUpdate);
        //处理缓存
        redisService.delete(MiaoshaUserKey.getById,""+id);
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(MiaoshaUserKey.token,token,user);
        return true;
    }
    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
        if(StringUtils.isEmpty(token)) {
            return null;
        }
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        //延长有效期
        if(user != null) {
            addCookie(response, token, user);
        }
        return user;
    }
    public String login(HttpServletResponse response,LoginVo loginVo){
        if(loginVo==null){
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        long mobile=loginVo.getMobile();
        String formPass=loginVo.getPassword();
        //判断手机号是否存在
        MiaoshaUser user = getById(mobile);
        if (user==null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbPass=user.getPassword();
        String salt=user.getSalt();
        String calcPass= MD5Util.formPassToDbPass(formPass,salt);
        if(!calcPass.equals(dbPass)){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        String token = UUIDUtil.uuid();
        addCookie(response, token, user);
        return token;
    }
    private void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {
        redisService.set(MiaoshaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
