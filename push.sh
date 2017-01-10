git add .
echo -e "Please enter the commit message."
read msg
git commit -m "$msg"
git push -u origin master

read pause