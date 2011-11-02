package cmg.org.monitor.ext.util;





import cmg.org.monitor.common.Constant;

import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;

import java.util.Collection;

/**
 * Please enter a short description for this class.
 *
 * <p>Optionally, enter a longer description.</p>
 *
 * @author   Binh Nguyen
 * @version  1.01 May 02, 2008
 */
public class StringUtils {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   source  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  RuntimeException  DOCUMENT ME!
     */
    public static String encodeUrl(String source) {
        if (!needsUrlEncoding(source)) {
            return source;
        }
        try {
            return URLEncoder.encode(source, Constant.ENCODING_ISO_8859_1);
        } catch (UnsupportedEncodingException e) {
            // this should never happen, ISO-8859-1 is a standard encoding
            throw new RuntimeException(e);
        }
    }

    private static boolean needsUrlEncoding(String source) {
        if (null == source) {
            return false;
        }

        // check if the string needs encoding first since
        // the URLEncoder always allocates a StringBuffer, even when the
        // string is returned as-is
        boolean encode = false;
        char ch;
        for (int i = 0; i < source.length(); i++) {
            ch = source.charAt(i);
            if (((ch >= 'a') && (ch <= 'z')) || ((ch >= 'A') && (ch <= 'Z'))
                    || ((ch >= '0') && (ch <= '9'))
                    || (ch == '-') || (ch == '_') || (ch == '.')
                    || (ch == '*')) {
                continue;
            }
            encode = true;

            break;
        }

        return encode;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   collection  the collection
     * @param   seperator   the seperator
     *
     * @return  a string
     */
    public static String join(Collection collection, String seperator) {
        if (null == collection) {
            return null;
        }
        if (null == seperator) {
            seperator = "";
        }
        if (0 == collection.size()) {
            return "";
        } else {
            StringBuilder result = new StringBuilder();
            for (Object element : collection) {
                result.append(String.valueOf(element));
                result.append(seperator);
            }
            result.setLength(result.length() - seperator.length());

            return result.toString();
        }
    }

    /**
     * this method gets a plain text between html tags.
     *
     * @param   inputStr  the input string
     *
     * @return  a string after remove html tags
     */
    public static String getValueInTDTag(String inputStr) {
        try {
            int lastIndex = inputStr.lastIndexOf("</td>");
            String temp = inputStr.substring(4, lastIndex);

            // return the result
            return temp;
        } catch (Exception ex) {
            ex.printStackTrace();

            return null;
        }
    }

    /**
     * Returns the value between pair code.
     *
     * <p>
     * </p>
     *
     * <p>tags</p>
     *
     * @param   inputStr  the input string
     *
     * @return  new string without tags
     */
    public static String getValueInPTag(String inputStr) {
        try {
            if (inputStr == null) {
                return null;
            }

            return inputStr.replaceAll("(<p>|</p>)", "");
        } catch (Exception ex) {
            ex.printStackTrace();

            return null;
        }
    }
}
