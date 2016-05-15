#/system/bin/sh


######转发代理脚本#####
###!全自动无需修改!####
###QQ全自动半放行无需修改###
####支持[xxx]变量调用#######
###监听端口:[localport]####
###软件UID: [myuid]######
###QQUid:[qquid]########
###轻聊版QQuid:[qqluid]###

######变量使用例子#####
#echo "\t\t\tQQlite:[qqluid]\n"

####!无需修改!####
####监听端口#####
l_port="[localport]"
####QQ的UID#####
u_qq=[qquid]
####轻聊版QUID####
u_qql=[qqluid]
####HaP软件UID####
u_my=[myuid]

#开启全代理防跳
qft="on"

#wifi放行
wifi="on"

#开启共享防跳
gxft="on"

#wifi网卡名
wk="wlan0"

#####自定义全放行######
###默认全局UID:3004###
###必须加上否则无法上网##
######空格隔开UID######
qfx="3004 "

####自定义放行应用的端口###
##放的应用的UID 空格隔开####
bfx=""

##放行的端口 空格隔开####
bfx_port=""



echo "防跳规则开始应用"
echo "#############"
iptables -t nat -F

iptables -t nat -I OUTPUT -p tcp --dport 80 -j REDIRECT --to-ports $l_port
iptables -t nat -I OUTPUT -p tcp --dport 8080 -j REDIRECT --to-ports $l_port
iptables -t nat -I OUTPUT -p tcp --dport 443 -j REDIRECT --to-ports $l_port
iptables -t nat -I OUTPUT -p tcp -d 127.0.0.1 -j ACCEPT


if [[ $qft == "on" ]]
then
iptables -t nat -A OUTPUT -p tcp -j DNAT --to-destination 127.0.0.1:$l_port
echo "全代理防跳: <font color='#ff0000'> √</font> "
else 
echo "全代理防跳: <font color='#ff0000'>×</font>"
fi

#####开始执行全放行UID#####
   for u in $qfx
    do
     #应用放行规则
     iptables -t nat -I OUTPUT -p tcp -m owner --uid-owner $u -j ACCEPT      
   done

echo "全放行UID: <font color='#ff0000'> $qfx √</font>"

if [[ "$bfx" != "" ]]
then
#####开始执行半放行UID#####
   for u in $bfx
    do
       for p in $bfx_port
        do
        iptables -t nat -I OUTPUT -p tcp --dport $p -m owner --uid-owner $u -j ACCEPT
       done
   done

   echo "半放行UID: <font color='#ff0000'> $bfx</font>"
   echo "半放行端口: <font color='#ff0000'> $bfx_port</font>"
   echo "半放行执行: <font color='#ff0000'> √</font>"
fi 

if [[ $u_qq -ne -1 ]]
then
iptables -t nat -I OUTPUT -p tcp --dport 14000 -m owner --uid-owner $u_qq -j ACCEPT
iptables -t nat -I OUTPUT -p tcp --dport 8080 -m owner --uid-owner $u_qq -j ACCEPT
iptables -t nat -I OUTPUT -p tcp --dport 443 -m owner --uid-owner $u_qq -j ACCEPT
iptables -t nat -I OUTPUT -p udp --dport 8000 -m owner --uid-owner $u_qq -j ACCEPT
iptables -t nat -I OUTPUT -p udp --dport 10061 -m owner --uid-owner $u_qq -j ACCEPT
echo "半放行QQ UID:$u_qq <font color='#ff0000'> √</font> "

fi

if [[ $u_qql -ne -1 ]]
then 
iptables -t nat -I OUTPUT -p tcp --dport 14000 -m owner --uid-owner $u_qql -j ACCEPT
iptables -t nat -I OUTPUT -p tcp --dport 8080 -m owner --uid-owner $u_qql -j ACCEPT
iptables -t nat -I OUTPUT -p tcp --dport 443 -m owner --uid-owner $u_qql -j ACCEPT
iptables -t nat -I OUTPUT -p udp --dport 8000 -m owner --uid-owner $u_qql -j ACCEPT
iptables -t nat -I OUTPUT -p udp --dport 10061 -m owner --uid-owner $u_qql -j ACCEPT
echo "半放行轻聊版QQ UID:$u_qql <font color='#ff0000'> √</font> "

fi

if [[ $u_my -ne -1 ]]
then
echo "识别HaP软件UID为:$u_my <font color='#ff0000'> √</font> "
fi

#共享规则#
if [[ $gxft == "on" ]]  
then
##开启ipv4转发
echo "1"  > /proc/sys/net/ipv4/ip_forward
iptables -t nat -A PREROUTING -s 192.168.0.0/16 -p sctp -j REDIRECT --to-ports $l_port
iptables -t nat -A PREROUTING -s 192.168.0.0/16 -p icmp -j REDIRECT --to-ports $l_port
iptables -t nat -A PREROUTING -s 192.168.0.0/16 -p udp -j REDIRECT --to-ports $l_port

iptables -t nat -A PREROUTING -s 192.168.0.0/16 -p tcp --dport 80 -j REDIRECT --to-ports $l_port

#iptables -t nat -A PREROUTING -s 192.168.0.0/16 -p tcp --dport 8080 -j REDIRECT --to-ports $l_port

#iptables -t nat -A PREROUTING -s 192.168.0.0/16 -p tcp --dport 443 -j REDIRECT --to-ports $l_port


iptables -t nat -A POSTROUTING -s 192.168.0.0/16 -j MASQUERADE

echo "wifi共享防跳: <font color='#ff0000'> √</font> "
else 
echo "wifi共享防跳: <font color='#ff0000'> ×</font>"
fi

#wifi放行#
if [[ $wifi == "on"  ]]
then
iptables -t mangle -I OUTPUT -o $wk -j ACCEPT
iptables -t nat -I OUTPUT -o $wk -j ACCEPT
echo "放行wifi : <font color='#ff0000'> $wk √</font> "
else 
echo "放行wifi : <font color='#ff0000'> $wk ×</font>"
fi


echo "<font color='#ff0000'>防跳命令执行完成....</font>"


