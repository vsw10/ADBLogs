package com.gesl.logs.adblogs;

/**
 * Created by vinod on 27/11/17.
 */

public class Utility {

    private static Utility mUtility;

    public String getTextFile() {
        return textFile;
    }

    public void setTextFile(String textFile) {
        this.textFile = textFile;
    }

    public String textFile;
    public static Utility getInstance() {
        if(mUtility == null) {
            mUtility = new Utility();
        }
        return mUtility;
    }
}
