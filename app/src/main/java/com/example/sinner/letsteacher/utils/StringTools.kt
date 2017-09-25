package com.example.sinner.letsteacher.utils

import java.lang.reflect.Array
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * String操作工具类
 *
 */
object StringTools {

    // 随机种子
    private val CHAR_RANDOMS = charArrayOf('1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z')

    /**
     * 统计字符串长度,一个双字节字符长度计2，ASCII字符计1
     *
     * @param str 字符串
     */
    fun getLength(str: String): Int {
        return str.replace("[^\\x00-\\xff]".toRegex(), "aa").length
    }

    fun getStringByBytes(bs: ByteArray): String {
        return String(bs)
    }

    /**
     * 判断字符串是否为空，即为null或""
     */
    fun isEmpty(str: String?): Boolean {
        return str == null || str.length == 0
    }

    /*public static String IsEmpty(String str){
		return ((str == null) || (str.length() == 0));
	}
	*/

    /**
     * 判断字符串是否不为空，即不为null且不为""
     */
    fun isNotEmpty(str: String): Boolean {
        return !isEmpty(str)
    }

    /**
     * 判断字符串是否为空白，即为null或为" "
     */
    fun isBlank(str: String?): Boolean {
        return str == null || str.trim { it <= ' ' }.length == 0
    }

    /**
     * 生成长度为5到10的随机字符串. 随机字符串的内容包含[1-9 A-Z a-z]的字符.
     *
     * @return 随机字符串.
     */
    fun buildRandomString(): String? {
        return buildRandomString(5, 10)
    }

    /**
     * 生成随机字符串. 随机字符串的内容包含[1-9 A-Z a-z]的字符.
     *
     * @param length 必须为正整数 随机字符串的长度
     * @return 随机字符串.
     */
    @Synchronized
    fun buildRandomString(length: Int): String? {
        if (length <= 0) {
            throw IllegalArgumentException("Length只能是正整数!")
        }
        var random: SecureRandom? = null
        try {
            random = SecureRandom.getInstance("SHA1PRNG")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        if (random == null) {
            return null
        }
        val ret = StringBuffer()
        for (i in 0 until length) {
            ret.append(CHAR_RANDOMS[random.nextInt(CHAR_RANDOMS.size)])
        }
        random = null
        return ret.toString()
    }

    /**
     * 生成随机字符串. 随机字符串的内容包含[1-9 A-Z a-z]的字符.
     *
     * @param min 必须为正整数 随机字符串的最小长度
     * @param max 必须为正整数 随机字符串的最大长度
     * @return 随机字符串.
     */
    @Synchronized
    fun buildRandomString(min: Int, max: Int): String? {
        if (min <= 0) {
            throw IllegalArgumentException("Min 只能是正整数!")
        } else if (max <= 0) {
            throw IllegalArgumentException("Max 只能是正整数!")
        } else if (min > max) {
            throw IllegalArgumentException("Min 必须小于或等于 Max!")
        }
        var random: SecureRandom? = null
        try {
            random = SecureRandom.getInstance("SHA1PRNG")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        if (random == null) {
            return null
        }
        val length = random.nextInt(max - min + 1) + min
        val ret = StringBuffer()
        for (i in 0 until length) {
            ret.append(CHAR_RANDOMS[random.nextInt(CHAR_RANDOMS.size)])
        }
        random = null
        return ret.toString()
    }

    /**
     * 截取固定超出长度,以"..."结尾
     *
     * @param str 需要截图的string
     * @param length 长度
     */
    fun subStringLength(str: String, length: Int): String? {
        var str = str
        if (!isEmpty(str) && str.length > length) {
            str = str.substring(0, length) + "..."
        }
        return str
    }

    fun getNotNullStr(str: String?): String {
        return if (str == null || str === "null") "" else str
    }

    fun getNotNullStr(str: String?, back: String): String {
        return if (str == null || str === "null") back else str
    }

    fun isPhoneNumber(num: String?): Boolean {
        if (num != null && num.length != 0) {
            val p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$")

            val m = p.matcher(num)

            println(m.matches().toString() + "---")

            return m.matches()
        }
        return false
    }

    /**
     * 将html占位符转化为正常字符
     * @param str
     * @return
     */
    fun replaceHtmlCharacters(str: String): String {
        var inputstr: String? = str
        var temptext = ""
        if (null != inputstr && "" != inputstr) {
            if (inputstr.indexOf("&amp;") > -1) {
                inputstr = inputstr.replace("&amp;".toRegex(), "&")
            }
            if (inputstr.indexOf("&lt;") > -1) {
                inputstr = inputstr.replace("&lt;".toRegex(), "<")
            }
            if (inputstr.indexOf("&gt;") > -1) {
                inputstr = inputstr.replace("&gt;".toRegex(), ">")
            }
            if (inputstr.indexOf("&apos;") > -1) {
                inputstr = inputstr.replace("&apos;".toRegex(), "'")
            }
            if (inputstr.indexOf("&quot;") > -1) {
                inputstr = inputstr.replace("&quot;".toRegex(), "\"")
            }
            if (inputstr.indexOf("&#034;") > -1) {
                inputstr = inputstr.replace("&#034;".toRegex(), "\"")
            }
            temptext = inputstr
        }
        return temptext
    }

    fun <T : Comparable<T>?> List<T>.sort():List<T>{
        return this.apply { Collections.sort(this) }
    }

    fun <T:Comparable<T>?> List<T>.reverse():List<T>{
        return this.apply { Collections.reverse(this) }
    }
}