1. 是参考 https://juejin.cn/post/6844904095942180878 做的。
2. 需要主要的是 base64(clientId:clientSecret) 来进行认证。否则会被CsrfFilter拦截掉。
3. 最初的一个spring security oauth2 学习样例。慢慢理解。
4. 获取token 的postman配置
```
http://localhost:6001/oauth/token?grant_type=password&username=admin&password=123456&scope=all
header:   Authorization: Basic dXNlci1jbGllbnQ6dXNlci1jbGllbnQ=

然后根据access_token 访问数据
http://localhost:6101/client-user/get
header:    Authorization: bearer 8c4cd7ad-e433-4599-9306-4ff34daa12f1
```