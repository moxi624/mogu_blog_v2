#!/usr/bin/env bash

echo "############当前操作系统版本##############"
if ! type yum >/dev/null 2>&1; then
        echo "【ERROR】目前脚本仅支持CentOS7.X系统"
        exit 8
else
        osVersion=$(echo `cat /etc/redhat-release | sed -r 's/.* ([0-9]+)\..*/\1/'`)
        if [[ "$osVersion" != "7" ]]; then
             echo "【ERROR】目前脚本仅支持CentOS7.X系统"
             exit 8
        else
             echo '版本校验成功' 
        fi
fi


echo "############检查常用端口号是否被占用##############"
mysqlPID=`/usr/sbin/lsof -i :3306|grep -v "PID" | awk '{print $2}'`
if [ "$mysqlPID" != "" ];
then
   echo "【ERROR】MySQL的3306端口号已被占用"
   exit 8
fi

redisPID=`/usr/sbin/lsof -i :6379|grep -v "PID" | awk '{print $2}'`
if [ "$redisPID" != "" ];
then
   echo "【ERROR】Redis的6379端口号已被占用"
   exit 8
fi
echo '端口号冲突校验成功' 


echo "############判断是否安装了docker##############"
if ! type docker >/dev/null 2>&1; then

	cat >/etc/yum.repos.d/docker.repo<<EOF
[docker-ce-edge]
name=Docker CE Edge - \$basearch
baseurl=https://mirrors.aliyun.com/docker-ce/linux/centos/7/\$basearch/edge
enabled=1
gpgcheck=1
gpgkey=https://mirrors.aliyun.com/docker-ce/linux/centos/gpg
EOF

    echo 'docker 未安装';
	echo '开始安装Docker....';	
	yum -y install docker-ce
	
	echo '配置Docker开启启动';
	systemctl enable docker
	systemctl start docker	

cat >> /etc/docker/daemon.json << EOF
{
  "registry-mirrors": ["https://b9pmyelo.mirror.aliyuncs.com"]
}
EOF

	systemctl restart docker
	
else
    echo 'docker 已安装';
fi

echo "############判断是否安装了wget##############"
if ! type wget >/dev/null 2>&1; then
    echo 'wget 未安装';
	echo '开始安装wget....';	
	yum -y install wget
	
else
    echo 'wget 已安装';
fi

echo "############判断是否安装了dos2unix##############"
if ! type dos2unix >/dev/null 2>&1; then
    echo 'dos2unix 未安装';
	echo '开始安装dos2unix....';	
	yum -y install dos2unix*
	
else
    echo 'dos2unix 已安装';
fi

echo "############判断是否安装了docker-compose##############"
if ! type docker-compose >/dev/null 2>&1; then
    echo 'docker-compose 未安装';
	echo '开始安装docker-compose....';		
	wget http://oss.moguit.cn/script//docker-compose-Linux-x86_64
	mv docker-compose-Linux-x86_64  docker-compose
	chmod +x docker-compose
	mv docker-compose /usr/local/bin/
	docker-compose -v
	
else
    echo 'docker-compose 已安装';
fi


echo "############判断是否安装了unzip解压##############"
if ! type unzip >/dev/null 2>&1; then
    echo 'unzip 未安装';
	echo '开始安装unzip....';	
	yum -y install unzip
	
else
    echo 'unzip 已安装';
fi


echo '创建docker网络';
docker network create mogu

echo '正在拉取一键部署脚本';
wget http://oss.moguit.cn/script//docker-compose.zip


unzip docker-compose.zip
rm -rf docker-compose.zip

# 进入目录
cd docker-compose
# 添加执行权限
chmod +x bin/kernStartup.sh
chmod +x bin/kernShutdown.sh
chmod +x bin/mouGoStartup.sh
chmod +x bin/mouGoShutdown.sh
chmod +x bin/update.sh
chmod +x bin/wait-for-it.sh

# 进入到bin目录
cd bin

# 修改编码
echo "修改编码...."
dos2unix kernStartup.sh
dos2unix kernShutdown.sh
dos2unix moguGoStartup.sh
dos2unix moguGoShutdown.sh
dos2unix update.sh
dos2unix wait-for-it.sh

# 执行脚本
python2 replaceIp.py

sh kernStartup.sh
