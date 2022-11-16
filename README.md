# Lrcmall
一个简单的书城应用
见证了大二下互联网应用开发和大三上应用体系架构的那段青葱岁月
开启后端时要记得：
- 确认mysql、WebSocket、Redis服务启动
net start -----
- 开Kafka（命令行启动）
- 开Solr（不用全文搜索功能的话可以不用开）
  在solr安装目录/bin下运行 ./solr.cmd start

各种端口对应:
- fronteend--8081（default，会根据情况自己调整）
- backend（8080--https的8843）
- bookSearch--111230
- Eureka--8040
- Gateway--8060