package com.example.data;

/**
 * 枚举类型单例
 */
enum EnumSingleton {
    INSTANCE;

    public void doSomeThing() {
        System.out.println("你好，我是枚举类");
    }
}
