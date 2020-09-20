package com.moxi.mogublog.utils;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;

/**
 * 图片操作工具类
 *
 * @author 陌溪
 * @date 2020年9月20日11:24:52
 */
public class ImageUtils {

    public static void main(String[] args) throws Exception {

        System.out.println(System.currentTimeMillis());
        //输出目录
        String rootPath = "d:/";

        //这里文字的size，建议设置大一点，其实就是像素会高一点，然后缩放后，效果会好点，最好是你实际输出的倍数，然后缩放的时候，直接按倍数缩放即可。
        Font font = new Font("微软雅黑", Font.PLAIN, 130);

        createImage("蘑菇博客", font, Paths.get(rootPath, "sojson-image.png").toFile());
    }

    private static int[] getWidthAndHeight(String text, Font font) {
        Rectangle2D r = font.getStringBounds(text, new FontRenderContext(
                AffineTransform.getScaleInstance(1, 1), false, false));
        int unitHeight = (int) Math.floor(r.getHeight());
        // 获取整个str用了font样式的宽度这里用四舍五入后+1保证宽度绝对能容纳这个字符串作为图片的宽度
        int width = (int) Math.round(r.getWidth()) + 1;
        // 把单个字符的高度+3保证高度绝对能容纳字符串作为图片的高度
        int height = unitHeight + 3;
        return new int[]{width, height};
    }

    /**
     * 根据str,font的样式以及输出文件目录 创建图片
     *
     * @param text
     * @param font
     * @param outFile
     * @throws Exception
     */
    public static void createImage(String text, Font font, File outFile)
            throws Exception {
        // 获取font的样式应用在输出内容上整个的宽高
        int[] arr = getWidthAndHeight(text, font);
        int width = arr[0];
        int height = arr[1];
        // 创建图片画布
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_BGR);
        //透明背景  the begin
        Graphics2D g = image.createGraphics();
        image = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        g = image.createGraphics();
        //透明背景  the end


        /**
         如果你需要白色背景或者其他颜色背景可以直接这么设置，其实就是满屏输出的颜色
         我这里上面设置了透明颜色，这里就不用了
         */
        g.setColor(Color.BLACK);

        //画出矩形区域，以便于在矩形区域内写入文字
        g.fillRect(0, 0, width, height);

        /**
         * 文字颜色，这里支持RGB。new Color("red", "green", "blue", "alpha");
         * alpha 我没用好，有用好的同学可以在下面留言，我开始想用这个直接输出透明背景色，
         * 然后输出文字，达到透明背景效果，最后选择了，createCompatibleImage Transparency.TRANSLUCENT来创建。
         * android 用户有直接的背景色设置，Color.TRANSPARENT 可以看下源码参数。对alpha的设置
         */
        g.setColor(Color.white);

        // 设置画笔字体
        g.setFont(font);
        // 画出一行字符串
        g.drawString(text, 0, font.getSize());
        // 画出第二行字符串，注意y轴坐标需要变动
        g.drawString(text, 0, 2 * font.getSize());
        //执行处理
        g.dispose();
        // 输出png图片，formatName 对应图片的格式
        ImageIO.write(image, "png", outFile);
    }
}