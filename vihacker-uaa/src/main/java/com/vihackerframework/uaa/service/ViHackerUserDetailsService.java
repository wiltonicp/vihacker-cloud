package com.vihackerframework.uaa.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 用户登录详细
 *
 * @author Ranger
 * @since 2021/6/17
 * @email wilton.icp@gmail.com
 */
public interface ViHackerUserDetailsService extends UserDetailsService {

    /**
     * 根据手机号登录
     * @param mobile
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    UserDetails loadUserByMobile(String mobile) throws UsernameNotFoundException;

    /**
     * 根据社交账号登录
     * @param openId 第三方的绑定的openId
     * @return
     * @throws UsernameNotFoundException
     */
    UserDetails loadUserBySocial(String openId) throws UsernameNotFoundException;
}
