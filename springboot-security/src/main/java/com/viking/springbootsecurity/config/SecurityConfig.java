package com.viking.springbootsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {



    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }


    /**
     * 用户名密码配置
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth.inMemoryAuthentication().withUser("viking").password("admin@viking.123").roles("admin")
                .and().withUser("yan").password("admin@yan.123").roles("user");
    }

    /**
     * 登录处理相关的配置
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
//        http.addFilterBefore()
        http.authorizeRequests()// 开启登录配置
                .antMatchers("/start").hasAnyRole("admin")// 表示访问 /hello 这个接口，需要具备 admin 这个角色
                .anyRequest().authenticated()// 表示剩余的其他接口，登录之后就能访问
                .and().formLogin()
//                .loginPage("/loginPage")// 定义登录页面，未登录时，访问一个需要登录之后才能访问的接口，会自动跳转到该页面
                .loginProcessingUrl("/auth/signIn")// 登录处理接口
                .usernameParameter("username")// 定义登录时，用户名的 key，默认为 username
                .passwordParameter("password")// 定义登录时，用户密码的 key，默认为 password
                // 登录成功的处理器
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//                        response.setContentType("application/json;charset=utf-8");
//                        PrintWriter printWriter = response.getWriter();
//                        printWriter.write("auth success");
//                        printWriter.flush();
                    }
                })
                // 登录失败的处理器
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                        response.setContentType("application/json;charset=utf-8");
                        PrintWriter printWriter = response.getWriter();
                        printWriter.write("auth failure" + e.getMessage());
                        printWriter.flush();
                    }
                })
                .permitAll()// 和表单登录相关的接口统统都直接通过
                .and()
                .logout()
                .logoutUrl("/logout")
                // 注销成功的处理器
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        response.setContentType("application/json;charset=utf-8");
                        PrintWriter printWriter = response.getWriter();
                        printWriter.write("logout success");
                        printWriter.flush();
                    }
                })
                .permitAll()
                .and()
                .httpBasic();
//                .and()
//                .csrf()
//                .disable();
    }

    /**
     * 登录拦截相关的配置
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web.ignoring().antMatchers("/hello");
    }
}
