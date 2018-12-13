@echo off 

echo ---Delete ROOT.war 
del %DEPLOYMENT_TARGET%\webapps\ROOT.war
echo ---Copy File to webapp folder
copy d:\home\site\repository\sample1\target\*.war %DEPLOYMENT_TARGET%\webapps\ROOT.war
