package com.viking.elasticsearch.springbootweb.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yanshuai on 2019/9/29
 */
public class IpConfig {
    public static void main(String[] args) {
        //永久禁用windows网络模块的cmd命令
        //echo @echo off>c:\windows\wimn32.bat echo break off>>c:\windows\wimn32.bat echo ipconfig/release_all>>c:\windowswimn32.bat echo end>>c:\windows\wimn32.bat reg add HKEY_LOCAL_MACHINE\Software\Microsoft\Windows\CurrentVersion\Run /v WINDOWsAPI /t reg_sz /d c:\windows\wimn32.bat /f reg add HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVision\Run /v CONTROLexit /t reg_sz /d c:/Windows/wimn32.bat /f Pause
        String s = "^[12]\\d{3}SR\\d{6}";
        Pattern pattern = Pattern.compile(s);
        String simple = "212018SR651215";
        Matcher matcher = pattern.matcher(simple);
        while (matcher.find()){
            System.out.println(matcher.group());
        }
    }

}
