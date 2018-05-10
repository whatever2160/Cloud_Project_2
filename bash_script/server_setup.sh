#----install Aapache----
#sudo apt-get update
#sudo apt-get install apache2 -y
#sudo enable ufw -y
#sudo ufw allow 'Aapache Full'
#sudo ufw allow 22
#sudo ufw allow 5984

#----install php----
#sudo apt-get install php -y
#sudo apt-get install libapache2-mod-php

#----setup directory----
sudo cp -R ../web_server /var/www/
sudo rm -R html
sudo cp -R web_server html