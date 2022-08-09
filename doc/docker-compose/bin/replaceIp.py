# -*- coding:utf-8 -*-
from urllib2 import urlopen
import io
import re
# 获取ip地址


import requests
import re
 
myIp = requests.get('https://checkip.amazonaws.com').text.strip()
print "外网IP：", myIp

# 替换ip地址
def replace(file, newStr):
    fileData = ""
    with io.open(file, 'r', encoding='utf-8') as f:
        lines = f.readlines()
        # 查找到ip地址
        for line in lines:
            ipList = re.findall(r"\b(?:[0-9]{1,3}\.){3}[0-9]{1,3}\b", line)
            if not ipList == []:
                oldStr = ipList[0]
                print file, "替换ip地址:", oldStr, "->", newStr
                break
        # 替换ip地址
        for line in lines:
            line = line.replace(oldStr, newStr)
            fileData += line

    with io.open(file,"w",encoding="utf-8") as f:
        f.write(fileData)

replace("../config/vue_mogu_admin.env", myIp)
replace("../config/vue_mogu_web.env", myIp)

