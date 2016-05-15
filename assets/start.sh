#/system/bin/sh
cd /data/data/com.HaP.Byml/files
#chmod 7777 ./haproxy.pid >/dev/null 
echo "\t正在执行启动命令...."
./HaP -f run.cfg >/dev/null 
if [ -s ./haproxy.pid ]
then
while read line
do
    if [ -f /proc/$line/comm ]
    then
      echo "<font color='#ff0000'> ########PID: $line #######</font>\n\t程序HaP:\t<font color='#ff0000'> \n\t\t正在运行....</font>"
    else
      echo "<font color='#ff0000'> ########PID: $line #######</font>\n\t程序HaP:\t<font color='#ff0000'> \n\t\t!没有运行....</font>"
    fi
    break
done < ./haproxy.pid
else
    echo "<font color='#ff0000'> ########PID: 没有PID文件 #######</font>\n\t程序HaP:\t<font color='#ff0000'> \n\t\t启动失败!....</font>"
fi   
  echo "\n八云论坛,你的选择: <font color='#ff0000'> http://byml.net</font>"      
