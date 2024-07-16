1. docker镜像

使用说明：

- 若接收的是源代码：
首次运行：docker-compose up --build
后续运行：docker-compose up
停止服务：docker-compose down
> 不用别忘记停止，不然如果更新可能会占用

完全重置（包括数据）：docker-compose down -v
若想保存镜像：docker save -o myapp.tar "你的镜像名字" mysql:8.0
设置镜像名字：docker-compose里的image配置，不写会自动生成个别的
查看镜像名字: docker images

- 若接收的是镜像myapp.tar：
docker load -i myapp.tar
docker-compose up

项目跑起来后：
后端端口：localhost:8080/api/
对应启动的Swagger文档：localhost:8080/api/swagger-ui/index.html

数据库端口号3306，需保证这两个端口号未被占用

> !!!本地测试前端需要配置解决跨域问题，不然访问不到后端接口，后续如果需要部署服务器再用nginx代理

