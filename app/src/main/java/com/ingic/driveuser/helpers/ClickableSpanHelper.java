package com.ingic.driveuser.helpers;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;


public class ClickableSpanHelper {
    public ClickableSpanHelper() {
    }

    public static SpannableStringBuilder initSpan(String text) {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(text);
        return stringBuilder;
    }

    public static void setSpan(SpannableStringBuilder StringBuilder, String text, String clickableText, ClickableSpan clickableSpan) {
        try {
            StringBuilder.setSpan(
                    clickableSpan,
                    text.indexOf(clickableText),
                    text.indexOf(clickableText) + String.valueOf(clickableText).length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            StringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#000")), text.indexOf(clickableText),
                    text.indexOf(clickableText) + String.valueOf(clickableText).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
           /* StringBuilder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC),
                    text.indexOf(clickableText),
                    text.indexOf(clickableText) + String.valueOf(clickableText).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);*/

        } catch (Exception e) {
            e.printStackTrace();

        }

    }
    public static void setColor(SpannableStringBuilder StringBuilder, String text, String clickableText,String color) {
        StringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor(color)),text.indexOf(clickableText),
                text.indexOf(clickableText) + String.valueOf(clickableText).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


    }
    public static void setClickableSpan(TextView textView, SpannableStringBuilder stringBuilder) {
        textView.setText(stringBuilder);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
