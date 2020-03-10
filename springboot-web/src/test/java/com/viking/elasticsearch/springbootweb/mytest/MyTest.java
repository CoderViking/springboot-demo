package com.viking.elasticsearch.springbootweb.mytest;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yanshuai on 2019/10/8
 * 测试将一张图片放到另一张图片上
 */
public class MyTest {
    /**
     *param qrcodePath : 最后图片保存路劲
     */
    public static void overlapImage() {
        try {
            String qrcodePath = "E:\\FileTest\\createdPic.jpg";
            BufferedImage big = new BufferedImage(1080, 1920, BufferedImage.TYPE_INT_RGB);
            Graphics2D gd = big.createGraphics();
            //gd.setBackground(Color.white);
            gd.setColor(Color.white);
            gd.dispose();
            //BufferedImage big = ImageIO.read(new File(screenPath));
            //BufferedImage small = ImageIO.read(new File(qrcodePath));
            BufferedImage small = new BufferedImage(540, 540,BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = big.createGraphics();
            int x = (big.getWidth() - small.getWidth()) / 2;
            int y = 200 ;
            g.drawImage(small, x, y, small.getWidth(), small.getHeight(), null);
            g.dispose();
            ImageIO.write(big, "jpg", new File(qrcodePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        overlapImage();
        System.out.println("done");
    }
    @Test
    public void localDateTest(){
        ZonedDateTime zonedDateTime = LocalDate.now().atStartOfDay(ZoneId.systemDefault());
        Instant instant1 = zonedDateTime.toInstant();
        Date now = Date.from(instant1);
        System.out.println(now);
    }

}
