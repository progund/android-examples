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

    // String tag for logging
    private static final String LOG_TAG = Utils.class.getCanonicalName();

    // String constants for files and dirs
    private static final String FILE_SEP = "/" ;
    private static final String FIELD_SEP = "_" ;
    private static final Object AVATAR_DIR = "logos";
    private static final Object AVATAR_SUFFIX = ".png";

    // no objects needed, so keep the constructor private
    private Utils() {};

    /**
     * Creates a file for a Member given a bitmap
     * @param context used to find the app's directory
     * @param member
     * @param bitmap
     * @return
     * @throws IOException
     */
    public static File createImageFile(Context context, Member member, Bitmap bitmap) throws IOException {
        // Directory
        String dirName = fileDir(context);
        File dir = new File(dirName);
        if(!dir.exists()) {
            dir.mkdirs();
        }

        // Fileame
        String fileName = completeFileName(context, member);

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

    // returns the directory name for an app using the Context
    private static String fileDir(Context c) {
        return c.getFilesDir().getAbsoluteFile()+
            FILE_SEP + AVATAR_DIR + FILE_SEP;
    }

    // returns the file name from a Member
    private static String fileName(Member member) {
        return (member.name() + FIELD_SEP + member.email()).replace(" ", FIELD_SEP) +
                AVATAR_SUFFIX;
    }

    // returns the complete file name from a Member and Context
    private static String completeFileName(Context c, Member member) {
        return fileDir(c) + FILE_SEP + fileName(member);
    }

    // returns the File from a Member and Context
    private static File completeFile(Context c, Member member) {
        return new File(completeFileName(c, member));
    }

    /**
     * Checks if an avatar file for a member exists
     * @param c context used to get the app's directory
     * @param member - member which file to check for presence
     * @return true if the file exists, false otherwise
     */
    public static boolean avatarExists(Context c, Member member) {
        return completeFile(c, member).exists();
    }

    /**
     * Reads the content of the Members file and creates a bitmap from that.
     * @param c context used to get the app's directory
     * @param member member which file to read from
     * @return bitmap or null if something failed
     */
    public static Bitmap avatarBitmap(Context c, Member member) {
        if (! avatarExists(c, member)) {
            return null;
        }
        return BitmapFactory.decodeFile(completeFileName(c,member));
    }

}
