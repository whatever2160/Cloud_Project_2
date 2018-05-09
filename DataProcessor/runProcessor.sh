#!/bin/bash

export MPJ_HOME=/home/ubuntu/nick/mpj-v0_44/
export PATH=$MPJ_HOME/bin:$PATH

mpjrun.sh -np 4 /home/ubuntu/Cloud_Project_2-master/out/artifacts/cloudProject2_jar/cloudProject2.jar
