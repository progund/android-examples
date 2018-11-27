package se.juneday.memberimages;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import se.juneday.memberimages.domain.Member;

public class Utils {

    private static final String LOG_TAG = Utils.class.getCanonicalName();

    private Utils() {};

    public static File createImageFile(Context c, Member m, Bitmap bitmap) throws IOException {
        // Directory
        String dirName = fileDir(c);
        File dir = new File(dirName);
        if(!dir.exists())dir.mkdirs();

        // Fileame
        String fileName = dirName + "/" + fileName(m);

        // Write to file
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream (fileName);
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, outputStream);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Log.d(LOG_TAG, "Created file: " + fileName);

        return new File(fileName);
    }

    private static String fileDir(Context c) {
        return c.getFilesDir().getAbsoluteFile()+
                "/logos/";
    }

    private static String fileName(Member member) {
        return member.name().replace(" ", "_") +
                ".png";
    }

    private static String completeFileName(Context c, Member member) {
        return fileDir(c) + "/" + fileName(member);
    }

    private static File completeFile(Context c, Member member) {
        return new File(fileDir(c) + "/" + fileName(member));
    }

    public static boolean avatarExists(Context c, Member member) {
        return completeFile(c, member).exists();
    }

    public static Bitmap avatarBitmap(Context c, Member member) {
        if (! avatarExists(c, member)) {
            return null;
        }

        return BitmapFactory.decodeFile(completeFileName(c,member));
    }

}
