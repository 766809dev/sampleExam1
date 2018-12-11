@echo off 

echo —Delete ROOT.war 
del d:\home\site\repository\sampleExam1\target\ROOT.war
echo ---Rename app to ROOT.war 
rename d:\home\site\repository\sampleExam1\target\*.war ROOT.war
echo ---File is moved to webapp folder
copy d:\home\site\repository\sampleExam1\target\*.war %DEPLOYMENT_TARGET%\webapps\*.war
