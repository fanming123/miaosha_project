package com.example.miaosha1.Controller;

import com.example.miaosha1.vo.LoginVo;
import com.example.miaosha1.redis.RedisService;
import com.example.miaosha1.result.Result;
import com.example.miaosha1.service.MiaoshaUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Controller
@RequestMapping("/login")
public class LoginController {
      @Autowired
      MiaoshaUserService miaoShaUserService;
      @Autowired
      RedisService redisService;
      private static Logger logger= LoggerFactory.getLogger(LoginController.class);

     @RequestMapping("/to_login")
      /*登录*/
      public String toLogin(){
            return "login";
      }

      @RequestMapping("/do_login")
      @ResponseBody
      public Result<String> doLogin(HttpServletResponse response, @Valid LoginVo loginVo){
            logger.info(loginVo.toString());
          String token = miaoShaUserService.login(response, loginVo);
          return Result.success(token);
      }



}
