package com.pigcms.library.android.interfaces;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by win7 on 2017-02-09.
 */

public class NoSpecialCodeListener implements InputFilter {

    private static NoSpecialCodeListener ourInstance;

    public static NoSpecialCodeListener getInstance() {
        if (ourInstance == null)
            ourInstance = new NoSpecialCodeListener();
        return ourInstance;
    }

    InputFilter[] inputFilters = null;

    private NoSpecialCodeListener() {
        this.inputFilters = new InputFilter[]{this};
    }

    Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
            Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        Matcher emojiMatcher = emoji.matcher(source);
        if (emojiMatcher.find()) {
            return "";
        }
        return null;
    }

    public InputFilter[] Filter() {
        return inputFilters;
    }

    public InputFilter IntFilter(){
        return this;
    }
}
