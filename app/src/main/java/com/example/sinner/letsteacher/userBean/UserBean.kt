package com.example.sinner.letsteacher.userBean

import com.example.sinner.letsteacher.utils.Logs
import kotlin.reflect.KClass

/**用户类保存信息
 * Created by sinner on 2017-09-07.
 * mail: wo5553435@163.com
 * github: https://github.com/wo5553435
 */
class UserBean(val map2:MutableMap<String,Any?>) {
    var name: String by map2
    var age: Int     by map2

    fun test(){
        var test :Test=Test("",1)
        test say ("")
        var we:Int=1;

        we.inc()
        var  a:String ="abc"
        var l=a.length
        var int :Int?=0
        int=null as? Int

        var b:String?="abc"
        b=null

        fun isOdd(x:Int) =x%2!=0
        fun conver(X:Int) =X-2!=0
        val nums= listOf<Int>(1,2,3)
        Logs.e("--",""+nums.filter (::conver))

        val c:String ?=try {
            if(true){
                ""
            }
            ""
        }catch (e:NullPointerException){
            null
        }finally {

        }

        val  x=b?.length
    }

    inner  class Test( var name:String? ,var age:Int?){

        infix fun say(word :String){
            test()
        }

         operator fun component1(): String? {
             return name;
         }
        operator fun component2():Int?{
            return age;
        }

    }
}