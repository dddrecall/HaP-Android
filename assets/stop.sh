#/system/bin/sh
cd /data/data/com.HaP.Byml/files
#chmod 7777 ./haproxy.pid >/dev/null 
echo "\t正在执行停止命令...."
if [ -s haproxy.pid ]
then
  while read line
  do  
   kill $line
done < ./haproxy.pid 
sleep 0.5
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
  echo "<font color='#ff0000'> ########PID: $line #######</font>\n\t程序HaP:\t<font color='#ff0000'> \n\t\t!没有运行....</font>"
fi
    
