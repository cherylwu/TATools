# 助教小工具

### 从博客爬取学生信息(已完成)

#### 作业要求

将个人技术博客和Git账号通过评论的形式发布（在本帖下发布评论即可）。
格式：Github账号 学号（后5位） 博客地址，三者以空格分隔，
示例：https://github.com/SivilTaram 02101 http://www.cnblogs.com/easteast/

[抓取学生信息地址](http://www.cnblogs.com/cherylwu/p/8495419.html)


#### 思路
1. Chrome调试模式下，查看获取评论的API
2. 查看API需要的参数，通过抓取当前页面来找到API需要的参数


第一个问题：
![](https://images2018.cnblogs.com/blog/682120/201803/682120-20180319210756789-1271431330.png)

由上图可知，请求API为：

http://www.cnblogs.com/mvc/blog/GetComments.aspx?postId=8495419&blogApp=cherylwu&pageIndex=0&anchorCommentId=0&_=1521464785265

需要以下参数

1. postId: 8495419
2. blogApp: cherylwu
3. pageIndex: 0
4. anchorCommentId: 0
5. _: 1521463915572



第1个参数postId
![](https://images2018.cnblogs.com/blog/682120/201803/682120-20180319211001886-1501693017.png)
第2个参数blogApp
![](https://images2018.cnblogs.com/blog/682120/201803/682120-20180319210913182-620364239.png)
在这里获取

第3个参数pageIndex为页码，因为博客园评论为50条一页，学生人数大概三十人，所以一页数据足够，后续考虑分页逻辑。

第4个参数anchorCommentId没有搞清楚啥意思，所以就按原先的设置为0

最后一个参数 _ 很显然是时间戳，比较好获取
```java
new Date().getTime()
```

抓取学生的信息结果如下：
```
https://github.com/fqd332 17085 http://www.cnblogs.com/RuanjianFqd/
https://github.com/lzwk 17079 http://www.cnblogs.com/MrZhang145689/
https://github.com/FangStars 17090 http://www.cnblogs.com/FangStar/
https://github.com/BillCYJ 17093 http://www.cnblogs.com/billcyj/
https://github.com/HastingsX 17087 http://www.cnblogs.com/zhiwei97/
https://github.com/chaseMengdi 17070 https://www.cnblogs.com/BoqianLiu/
https://github.com/liuqiang666 17080 http://www.cnblogs.com/liuqsw/
https://github.com/JarrySmith 17086 http://www.cnblogs.com/Jarry-smith/
https://github.com/yangwj001 17073 http://www.cnblogs.com/RuanjianYwj/
https://github.com/792450735 17088 http://www.cnblogs.com/Nathon1Blog/
https://github.com/yuyuyu960818 17092 http://www.cnblogs.com/yuyuyu96/
https://github.com/jiaxuansun 17077 http://www.cnblogs.com/ss07/
https://github.com/zhaozhiyu/zhaoshidaye 17095 http://www.cnblogs.com/zhaoshidaye/
https://github.com/handsomesnail 17071 http://www.cnblogs.com/handsomesnail
https://github.com/HYYYYYZ/- 17068 http://www.cnblogs.com/HYYYYYZ/
https://github.com/jianjake 17091 http://www.cnblogs.com/jakejian/
https://github.com/husterSyy/ 17067 http://www.cnblogs.com/husterSyy/
https://github.com/MasonWater 17072 http://www.cnblogs.com/hayitutu/
https://github.com/Middamn 17074 http://www.cnblogs.com/middamn/
https://github.com/Hoyifei 17078 http://www.cnblogs.com/Komeiji-Koishi/
https://github.com/hddddd 17083 http://www.cnblogs.com/hddddd/
https://github.com/wwwwu 16081 http://www.cnblogs.com/mengue/
https://github.com/CG0317 17075 http://www.cnblogs.com/kkdzz/
https://github.com/Aabon00 17082 http://www.cnblogs.com/dilidiligy/
https://github.com/LantyrLYL 90035 http://www.cnblogs.com/Lantyr/
https://github.com/mrlandiao 17081 http://www.cnblogs.com/mrlandiao/
https://github.com/skz12345 17069 http://www.cnblogs.com/fleshbone/
https://github.com/245553473 17076 http://www.cnblogs.com/carroll/
https://github.com/aaliku 17084 http://www.cnblogs.com/17084aaliku/
https://github.com/mostannno 17089 http://www.cnblogs.com/miaoTer/
https://github.com/hcy6668 17094 http://www.cnblogs.com/Mitchell977/
```


### 自动执行exe文件(进行中)
1. 创建下载exe文件的根目录、
    - test
        - apps
            - 学号后五位_github用户名
            - 学号后五位_github用户名
            - 学号后五位_github用户名
        - answer
    
2. 将学生信息存放到本地目录c:/students下，学生信息的文件名为studentInfo.txt：
      
      17176	https://github.com/Daweihao
      
      16081	https://github.com/wwwwu
      
      17067	https://github.com/husterSyy
      
      17068	https://github.com/HYYYYYZ
      
      ...

3. 创建学生个人作业文件夹，命名格式：学号后五位_github用户名.如：900035_LantyrLYL

4. 拼接命令，从github将项目克隆到学生对应文件夹。
- 自动获取学生个人作业文件夹下的项目目录
- 从测试用例文档中读取命令
- 执行读取的命令
- 获取结果输出文件，并移到指定文件夹中，并删除源目录的结果输出文件

以上，循环执行

5. 手动建立测试数据，放在c:/test/testCase目录下。

6. 拼接命令，执行同学的exe文件，测试字符数统计的方法。

7. 执行测试用例集

### update 2018-03-31
- 多线程下载学生作业
- 验证结果(字符串全匹配)


### 源码
[TATools](https://github.com/cherylwu/TATools)