package com.monteCCer.projetoatitude.model;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.widget.Button;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class DroidUtils {
    public static void scaleButtonDrawables(@NotNull Button btn, double fitFactor) {
        Drawable[] drawables = btn.getCompoundDrawables();

        for (int i = 0; i < drawables.length; i++) {
            if (drawables[i] != null) {
                int imgWidth = drawables[i].getIntrinsicWidth();
                int imgHeight = drawables[i].getIntrinsicHeight();
                if ((imgHeight > 0) && (imgWidth > 0)) {    //might be -1
                    float scale;
                    if ((i == 0) || (i == 2)) { //left or right -> scale height
                        scale = (float) (btn.getHeight() * fitFactor) / imgHeight;
                    } else { //top or bottom -> scale width
                        scale = (float) (btn.getWidth() * fitFactor) / imgWidth;
                    }
                    if (scale < 1.0) {
                        Rect rect = drawables[i].getBounds();
                        int newWidth = (int) (imgWidth * scale);
                        int newHeight = (int) (imgHeight * scale);
                        rect.left = rect.left + (int) (0.5 * (imgWidth - newWidth));
                        rect.top = rect.top + (int) (0.5 * (imgHeight - newHeight));
                        rect.right = rect.left + newWidth;
                        rect.bottom = rect.top + newHeight;
                        drawables[i].setBounds(rect);
                    }
                }
            }
        }
    }
    @NotNull
    public static String toMD5(@NotNull String s) {
        MessageDigest m= null;
        try {
            m = MessageDigest.getInstance("MD5");
            m.update(s.getBytes(),0,s.length());
            return new BigInteger(1,m.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
