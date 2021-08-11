package com.hy.java.classloader;

import java.net.URL;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.ProtectionDomain;

/**
 * HyClassLoader
 *  获取 class 所在的路径
 * @author Jie.Hu
 * @date 8/7/21 10:21 PM
 */
public class HyClassLoader {

    public static void main(String[] args) {

        Class<HyClassLoader> loaderClass = HyClassLoader.class;

        ProtectionDomain protectionDomain = loaderClass.getProtectionDomain();

        CodeSource codeSource = protectionDomain.getCodeSource();

        URL location = codeSource.getLocation();

        System.out.println(location);
    }
}
