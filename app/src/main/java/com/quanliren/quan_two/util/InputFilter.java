package com.quanliren.quan_two.util;

//import org.apache.commons.lang3.StringUtils;

import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputFilter {

    /**
     * 检测是否有emoji字符
     *
     * @param source
     * @return 一旦含有就抛出
     */

        Pattern emoji = Pattern . compile (

                "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]" ,

                Pattern . UNICODE_CASE | Pattern . CASE_INSENSITIVE ) ;

        public boolean filter ( CharSequence source) {

            Matcher emojiMatcher = emoji . matcher ( source ) ;

            if ( emojiMatcher . find ( ) ) {

                return true;

            }
            return false ;

        }
}