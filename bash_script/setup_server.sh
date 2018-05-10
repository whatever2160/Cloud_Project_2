#-----install Apache--------
#sudo apt-get update
#sudo apt-get install apache2 -y
#sudo enable ufw -y
#sudo ufw allow 'Apache Full'
#sudo ufw allow 22

#-----install php-----------
#sudo apt-get install php -y
#sudo apt-get install libapache2-mod-php

#-----setup directory-------
sudo cp -R /home/ubuntu/Cloud_Project_2-master/web_server /var/www/
sudo rm -R /var/www/html
sudo cp -R /var/www/web_server /var/www/html
