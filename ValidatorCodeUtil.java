package com.pzdf.eqihua.common.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class ValidatorCodeUtil {
    public ValidatorCodeUtil() {
    }

    public static ValidatorCodeUtil.ValidatorCode getCode() {
        byte width = 80;
        byte height = 30;
        BufferedImage buffImg = new BufferedImage(width, height, 1);
        Graphics2D g = buffImg.createGraphics();
        Random random = new Random();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        Font font = new Font("微软雅黑", 2, 28);
        g.setFont(font);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, width - 1, height - 1);
        StringBuffer randomCode = new StringBuffer();
        byte length = 4;
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int size = base.length();

        for(int code = 0; code < length; ++code) {
            int start = random.nextInt(size);
            String strRand = base.substring(start, start + 1);
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(strRand, 15 * code + 6, 24);
            randomCode.append(strRand);
        }

        g.dispose();
        ValidatorCodeUtil.ValidatorCode var13 = new ValidatorCodeUtil.ValidatorCode();
        var13.image = buffImg;
        var13.code = randomCode.toString();
        return var13;
    }

    static Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if(fc > 255) {
            fc = 255;
        }

        if(bc > 255) {
            bc = 255;
        }

        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    public static class ValidatorCode {
        private BufferedImage image;
        private String code;

        public ValidatorCode() {
        }

        public BufferedImage getImage() {
            return this.image;
        }

        public String getCode() {
            return this.code;
        }
    }
}

