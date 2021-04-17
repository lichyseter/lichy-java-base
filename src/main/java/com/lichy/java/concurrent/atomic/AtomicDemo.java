package com.lichy.java.concurrent.atomic;

/**
 * 关于Atomic的demo，试试不太常用的api
 */
public class AtomicDemo {

    public static void main(String[] args) {
        // 不写了。参照java11的中文文档。
        // https://www.apiref.com/java11-zh/java.base/java/lang/invoke/VarHandle.html#getVolatile(java.lang.Object...)
        // todo 这块有些需要理解的问题。类似getOpaque， getVolatile, get, getAcquire的区别。还有他带有loadload屏障，storestore 屏障，这块需要研究。
    }
}
