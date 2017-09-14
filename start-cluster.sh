sudo /etc/init.d/mysql stop
sudo /etc/init.d/ambari-server stop
sudo /etc/init.d/ambari-agent stop
sudo docker start mariadb
sudo docker start sandbox
