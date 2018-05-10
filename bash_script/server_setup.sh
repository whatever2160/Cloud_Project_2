<<<<<<< HEAD:bash_script/server_setup.sh
#----install Aapache----
=======
#-----install Apache--------
>>>>>>> b23466e2ae0e5bb727143559af1c52e7bf87ba0a:bash_script/setup_server.sh
#sudo apt-get update
#sudo apt-get install apache2 -y
#sudo enable ufw -y
#sudo ufw allow 'Aapache Full'
#sudo ufw allow 22
#sudo ufw allow 5984

#----install php----
#sudo apt-get install php -y
#sudo apt-get install libapache2-mod-php

<<<<<<< HEAD:bash_script/server_setup.sh
#----setup directory----
sudo cp -R ../web_server /var/www/
sudo rm -R html
sudo cp -R web_server html
=======
#-----setup directory-------
sudo cp -R /home/ubuntu/Cloud_Project_2-master/web_server /var/www/
sudo rm -R /var/www/html
sudo cp -R /var/www/web_server /var/www/html
>>>>>>> b23466e2ae0e5bb727143559af1c52e7bf87ba0a:bash_script/setup_server.sh
