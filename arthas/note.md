1. 启动demo 工程 

    ```
    java -jar arthas-demo.jar
    ```

2. 启动arthas

    ```
    java -jar arthas-boot.jar
    ```

3. dashboard 
4. thread java中的进程号
5. jad 全类名/ jad demo.MathGame ----------反编译一个类
6. watch 全类名 方法名 表达式 参数/ watch demo.MathGame primeFactors returnObj ----------查看方法的返回值
7. 退出attach状态 quit/ 停止arthas stop
8. reset 重置arthas 做过的所有操作
9. jvm 查看当前环境的jvm信息。
10. logger 用户查看logger的信息和热修改。

    ```
    更新logger level
    [arthas@2062]$ logger --name ROOT --level debug
    update logger level success.
    指定classloader更新 logger level
    默认情况下，logger命令会在SystemClassloader下执行，如果应用是传统的war应用，或者spring boot fat jar启动的应用，那么需要指定classloader。
    
    可以先用 sc -d yourClassName 来查看具体的 classloader hashcode，然后在更新level时指定classloader：
    
    [arthas@2062]$ logger -c 2a139a55 --name ROOT --level debug
    ```
11. getstatic 全类名 静态变量名/ getstatic demo.MathGame random
12. ognl ----- 调用ognl表达式。这个应该功能挺强大的，没研究怎么用。
13. heapdump /tmp/dump.hprof -----生成heapdump到某个目录。
14. sc 查看类信息，包括类加载器之类的。sc -d -f demo.MathGame
15. sm 查看方法信息 ------ sm -d java.lang.String toString
16. mc 编译java为class。可以指定classloader 纯线上编译，之后配合retransform 热替换。
17. retransform 使用class替换jvm中的class
    ```
       retransform /tmp/Test.class
       retransform -l
       retransform -d 1                    # delete retransform entry
       retransform --deleteAll             # delete all retransform entries
       retransform --classPattern demo.*   # triger retransform classes
       retransform -c 327a647b /tmp/Test.class /tmp/Test\$Inner.class
       retransform --classLoaderClass 'sun.misc.Launcher$AppClassLoader' /tmp/Test.class

    ```
18. base64 技巧。往线上靠文件内容可以，先将内容base64再放到远程base64回来。用hash值比较是否一致。
19. classloader 查看jvm中的所有classloader
20. monitor 统一方法执行的次数，失败，成功数等。和watch比，watch是会显示返回值的，不一样。monitor -c 5 demo.MathGame primeFactors
21. trace 显示方法内部调用链路。trace demo.MathGame run
22. stack 显示方法被调用的路径。stack demo.MathGame primeFactors
23. tt(timetunnel) 相比watch要知道方法名。tt查看所有方法的调用时间来确定问题位置。
24. profiler 生成代码执行弱点。