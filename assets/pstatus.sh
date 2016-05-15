#!/system/bin/sh
echo "\n<font color='#ff0000'>####当前防跳规则链####</font>"
iptables -nL -t nat --line-numbers
