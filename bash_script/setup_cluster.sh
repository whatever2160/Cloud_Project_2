curl -X POST -H 'Content-Type: application/json' http://admin:admin@115.146.95.87:5984/_cluster_setup -d "{\"action\": \"enable_cluster\", \"bind_address\":\"0.0.0.0\", \"username\": \"admin\", \"password\":\"admin\", \"port\": 5984, \"node_count\": \"2\", \"remote_node\": \"115.146.95.254\", \"remote_current_user\": \"admin\", \"remote_current_password\": \"admin\"}"
curl -X POST -H 'Content-Type: application/json' http://admin:admin@115.146.95.87:5984/_cluster_setup -d "{\"action\": \"add_node\", \"host\":\"115.146.95.254\", \"port\": 5984, \"username\": \"admin\", \"password\":\"admin\"}"
curl -X POST "http://admin:admin@localhost:5984/_cluster_setup" -H 'Content-Type: application/json' -d '{"action": "finish_cluster"}'
curl http://admin:admin@localhost:5984/_cluster_setup
curl -X GET http://admin:admin@localhost:5984/_membership
