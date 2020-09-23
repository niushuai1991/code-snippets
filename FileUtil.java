package com.util;

import java.net.URL;

/**
 * 文件工具类
 */
public class FileUtil {

    public static URL getResourceURL(String fileName) {
        URL url;
        // 1、通过Class的getResource方法
        url = FileUtil.class.getResource(fileName);
        if (url != null){
            return url;
        }
        // 2、通过本类的ClassLoader的getResource方法
        url = FileUtil.class.getClassLoader().getResource(fileName);
        if (url != null){
            return url;
        }
        // 3、通过ClassLoader的getSystemResource方法
        url = ClassLoader.getSystemClassLoader().getResource(fileName);
        if (url != null){
            return url;
        }
        // 4、通过ClassLoader的getSystemResource方法
        url = ClassLoader.getSystemResource(fileName);
        if (url != null){
            return url;
        }
        // 5、通过Thread方式
        url = Thread.currentThread().getContextClassLoader().getResource(fileName);
        if (url != null){
            return url;
        }
        return null;
    }
}
