echo #######################
echo ##### begin copy ######
echo #######################

echo ###### copy mogu_admin ########
copy .\conf\prod\mogu_admin\application.yml ..\..\mogu_admin\src\main\resources\
copy .\conf\prod\mogu_admin\bootstrap.yml ..\..\mogu_admin\src\main\resources\


echo ###### copy mogu_gateway ########
copy .\conf\prod\mogu_gateway\application.yml ..\..\mogu_gateway\src\main\resources\
copy .\conf\prod\mogu_gateway\bootstrap.yml ..\..\mogu_gateway\src\main\resources\


echo ###### copy mogu_monitor ########
copy .\conf\prod\mogu_monitor\application.yml ..\..\mogu_monitor\src\main\resources\
copy .\conf\prod\mogu_monitor\bootstrap.yml ..\..\mogu_monitor\src\main\resources\


echo ###### copy mogu_picture ########
copy .\conf\prod\mogu_picture\application.yml ..\..\mogu_picture\src\main\resources\
copy .\conf\prod\mogu_picture\bootstrap.yml ..\..\mogu_picture\src\main\resources\


echo ###### copy mogu_search ########
copy .\conf\prod\mogu_search\application.yml ..\..\mogu_search\src\main\resources\
copy .\conf\prod\mogu_search\bootstrap.yml ..\..\mogu_search\src\main\resources\


echo ###### copy mogu_sms ########
copy .\conf\prod\mogu_sms\application.yml ..\..\mogu_sms\src\main\resources\
copy .\conf\prod\mogu_sms\bootstrap.yml ..\..\mogu_sms\src\main\resources\


echo ###### copy mogu_spider ########
copy .\conf\prod\mogu_spider\application.yml ..\..\mogu_spider\src\main\resources\
copy .\conf\prod\mogu_spider\bootstrap.yml ..\..\mogu_spider\src\main\resources\


echo ###### copy mogu_web ########
copy .\conf\prod\mogu_web\application.yml ..\..\mogu_web\src\main\resources\
copy .\conf\prod\mogu_web\bootstrap.yml ..\..\mogu_web\src\main\resources\
