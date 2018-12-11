@echo off 

echo —Delete ROOT.war 
del %DEPLOYMENT_TARGET%\webapps\*.war
echo ---Copy File to webapp folder
copy d:\home\site\repository\sample1\target\*.war %DEPLOYMENT_TARGET%\webapps\*.war
echo ---Rename File to ROOT.war 
rename %DEPLOYMENT_TARGET%\webapps\*.war ROOT.war
