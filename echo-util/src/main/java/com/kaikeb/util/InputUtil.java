package com.kaikeb.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputUtil {
    public static final BufferedReader KEYBOARD_INPUT = new BufferedReader(new InputStreamReader(System.in));
    private  InputUtil(){}

    /**
     * 实现键盘输入操作
     * @param  prompot 提示信息
     * @return  输入的数据返回
     * @throws IOException
     */
    public static String getString(String prompot) throws IOException {
        boolean flag = true;//数据接收标记
        String str = null;
        while (flag){
            System.out.println(prompot);
            str = KEYBOARD_INPUT.readLine();
            if (str == null || "".equals(str)){
                System.out.println("数据输入有误，该内容不允许为空");
            }else {
                flag = false;
            }
        }
        return str;
    }
}
