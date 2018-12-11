@echo off 

echo —Delete ROOT.war 
del %DEPLOYMENT_TARGET%\webapps\ROOT.war
echo ---Rename app to ROOT.war 
rename d:\home\site\repository\sample1\target\*.war ROOT.war
echo ---File is moved to webapp folder
copy d:\home\site\repository\sample1\target\*.war %DEPLOYMENT_TARGET%\webapps\*.war
