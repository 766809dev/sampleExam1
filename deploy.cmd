@echo off 

echo —Delete ROOT.war 
del d:\home\site\wwwroot\webapps\ROOT.war
echo ---Copy File to webapp folder
copy d:\home\site\repository\sample1\target\*.war %DEPLOYMENT_TARGET%\webapps\ROOT.war
;echo ---Rename File to ROOT.war 
;rename %DEPLOYMENT_TARGET%\webapps\*.war ROOT.war
