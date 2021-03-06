package com.example.design_patterns.proxy;

import com.example.design_patterns.proxy.dynamic.DynamicFactory;
import com.example.design_patterns.proxy.dynamic.DynamicJavaFactory;

public class ProxyMain {
    public static void main(String[] args) {
        final JavaDeveloper jack = new JavaDeveloper("Jack");

        DynamicJavaFactory factory = new DynamicJavaFactory();
        factory.setFactory(jack);
        Developer instance = (Developer) factory.getProxyInstance();
        instance.print("fafaofaf");
    }
}
