import kotlin.properties.Delegates
import kotlin.reflect.KProperty

/**
 * 内联函数的使用
 */
fun main(args: Array<String>) {
//    run()
//    run2()
//    apply()
//    let()
//    also()
//    with()
    demo()
    demo1()
    demo2()
    demo4()
    demo5()

    val result = getShippingCostCalculator(Delivery.COMMON)
    println("普通件的价格为：${result(Order(10))}")

    demoLambda2()

    demoLib()

    demoLambda3()

    demoAsSequence()

    lookForAlice(people)
    lookForBob(people)
}


// 功能：调用run函数块。返回值为函数块最后一行，或者指定return表达式。
fun run() {
    val a = run {
        println("run")
        return@run 10
    }
    println(a)
}

//功能：调用某对象的run函数，在函数块内可以通过 this 指代该对象。返回值为函数块的最后一行或指定return表达式。
fun run2() {
    val a = "string".run {
        println(this)
        11
    }
    println(a)
}

//功能：调用某对象的apply函数，在函数块内可以通过 this 指代该对象。返回值为该对象自己。
fun apply() {
    val apply = "apply".apply {
        println(this)
    }
    println("apply：$apply")
}

//调用某对象的let函数，则该对象为函数的参数。在函数块内可以通过 it 指代该对象。返回值为函数块的最后一行或指定return表达式。

fun let() {
    val let = "let".let {
        println(it)
        "return let"
    }
    println(let)
}

//功能：调用某对象的also函数，则该对象为函数的参数。在函数块内可以通过 it 指代该对象。返回值为该对象自己。
fun also() {
    val also = "also".also {
        println("it is $it")
    }
    println(also)
}

// with函数和前面的几个函数使用方式略有不同，因为它不是以扩展的形式存在的。它是将某对象作为函数的参数，在函数块内可以通过 this 指代该对象。返回值为函数块的最后一行或指定return表达式。
fun with() {
    val with = with("with") {
        println("inner is $this")
        1
    }
    println(with)
}


/**********************************结构式初始化***********************************/

data class Result(val result: Int, val status: String)

fun function(): Result {
    return Result(10, "xiaomei")
}


class Test {
    fun test() {
        val (result, status) = function()

        val (x, y, z) = listOf(1, 2, 3)
    }
}


fun format(userName: String, domain: String, default: String = "") {

}

/*******************异常处理********************/
fun fail(message: String): Nothing {
    throw IllegalArgumentException(message)
}

val s = "" ?: fail("id required")


/*******************多重继承/实现***************/

interface A {
    fun functionA() {}
}

interface B {
    fun functionB() {}
}

class C(val a: A, val b: B) {
    fun functionC() {
        a.functionA()
        b.functionB()
    }
}

/*******************委托模式****************/

// 类委托
interface Base {
    fun print()
}

//实现此接口的被委托的类
class BaseImpl(private val x: Int) : Base {
    override fun print() {
        print(x)
    }
}

//通过关键字by建立委托类
class Derived(b: Base) : Base by b

fun demo() {
    val b = BaseImpl(10)
    Derived(b).print()
}

// 属性委托
//定义包含属性委托的类
class MyClass {
    var p: String by Delegate()
}

// 委托的类
class Delegate {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return "$thisRef,这里委托了${property.name}属性"
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, s: String) {
        println("$thisRef 的 ${property.name} 属性赋值为$s")
    }
}

fun demo1() {
    val c = MyClass()
    println(c.p)

    c.p = "delegate"
    println(c.p)
}


//懒初始化

val lazyValue: String by lazy {
    println("Got it")
    "my lady"
}

fun demo2() {
    println(lazyValue)
    println(lazyValue)
}

/*****************Observable*************/

class Data {
    var name: String by Delegates.observable("初始值") { _, oldValue, newValue ->
        println("旧值：$oldValue ->$newValue")
    }
}

fun demo4() {
    val d = Data()
    d.name = "我是第一次赋值"
    d.name = "我是第二次赋值"

}

/*****************从map取值*************/
class WebSite(val map: MutableMap<String, Any?>) {
    val name: String by map
    val url: String by map
}

fun demo5() {
    val map: MutableMap<String, Any?> = mutableMapOf("name" to "Google", "url" to "www.google.com")
    val site = WebSite(map)

    println(site.name)
    println(site.url)

    map["name"] = "Baidu"
    map["url"] = "www.baidu.com"


    println(site.name)
    println(site.url)

}


class Foo {
    var x: String by Delegates.notNull()
}

fun demo6() {
    val foo = Foo()
    foo.x = "bar"
}

/******************Kotlin Lambda*************/
val myFun: () -> Unit = {
}

val myGreeting: (String) -> Unit = {
    println("hello,$it")
}

val tellWang: (String, Any) -> Boolean = { s, _ ->
    "Wang" == s
}
val tellLi: (String, Any) -> Boolean = { s, _ ->
    "Li" == s
}

fun demoLambda(params: (String, Any) -> Boolean) {
    val result = params("Li", "")
    println("Lambda result is $result")
}

fun demoLambda2() {
    demoLambda(tellLi)
    demoLambda(tellWang)
}

/******************返回函数的函数*************************/
enum class Delivery { COMMON, EXPEDITED }

class Order(val flee: Int)

fun getShippingCostCalculator(delivery: Delivery): (Order) -> Double {
    if (delivery == Delivery.EXPEDITED) {
        return { order -> 6 + 2.1 * order.flee }
    }

    return { order -> 1.2 * order.flee }
}

/******************Kotlin Lambda库函数******************/

data class Person(val name: String, val age: Int) {
    fun lock() {

    }
}

val p = arrayListOf(Person("LiLi", 25), Person("LiYang", 28), Person("LiMei", 21), Person("LiXiao", 30))

fun demoLib() {
    println(p.filter { it.age >= 25 })
    println(p.map { it.name })
    println(p.map { it.name.substring(0, 1) }.reduce { acc, c -> "$acc,$c" })
    println(p.maxBy { it.age })
    println(p.groupBy { it.name.length })
    println(p.flatMap { it.name.toList() })
}

/****************高阶Lambda**********************/

fun twoAndThree(operation: (Int, Int) -> Int) {
    val result = operation(2, 3)
    println("result == $result")
}

fun demoLambda3() {
    twoAndThree { a, b -> a + b }
    twoAndThree { a, b -> a + b }
    twoAndThree(text)
}

val text: (Int, Int) -> Int = { a, b ->
    a - b
}

/****************优化lambda - 使⽤用内联****************/
//每个⾮非内联的lambda都会被编译成⼀一个匿匿名类。
//如果把lambda传给了了标记为inline的Kotlin函数， 就不不会创建任何匿匿名类。

class Lock {
    fun lock() {

    }

    fun unLock() {

    }
}

inline fun <T> synchronized(lock: Lock, action: () -> T): T {
    lock.lock()
    try {
        return action()
    } finally {
        lock.unLock()
    }
}

// 链式操作可能会⽣生成多个中间集合，导致性能问题。
// 可以调用asSequence把任何集合转为序列，调用toList把序列转换回List

fun demoAsSequence() {
    val toList = p.asSequence().map { it.name }.filter { it.startsWith("xx") }
            .toList()
    println(toList)
}


/********************从Lambda中返回********************/

val people = listOf(Person("Bob", 32), Person("Bob", 32), Person("Alice", 30), Person("Bob", 32))
fun lookForAlice(p: List<Person>) {
    p.forEach {
        if (it.name == "Alice") {
            println("find it")
            return
        }
        println("Alice is not found")
    }
}

//从lambda中返回标签
fun lookForBob(p: List<Person>) {
    p.forEach label@{
        if (it.name == "Alice")
            println(it.name)
        return@label
    }
    println("Alice might be somewhere")
}