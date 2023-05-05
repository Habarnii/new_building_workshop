# new_building_workshop
web application for residents of a new building for the sale and publication of leftover materials


Up postgres in docker new_building_workshop/project/postgres.sh  
run application  

endpoints  
  "/api/"   
  "/api/admin"   
  "/api/wallet"  

tests  
  curl -X PUT -H "login: admin" localhost:8080/api/admin/register -d '{"login":"test","first_name":"Test","last_name":"Test","apartment_number":10,"is_active":false}' -v<  
  curl -X PUT -H "login: admin" localhost:8080/api/admin/renew_subscription -d '{"login":"test","is_active":true}' -v  

  curl -X PUT -H "login: test" localhost:8080/api/create/new_lot -d '{"name":"lightbulb","description":"200V","count":10.0,"price":10.0}' -v  
  curl -X PUT -H "login: alex02" localhost:8080/api/create/new_lot -d '{"name":"lightbulb","description":"200V","count":10.0,"price":10.0}' -v  

  curl -X GET -H "login: admin"  localhost:8080/api/wallet/balance -d {'"login":"admin"'} -v  
  curl -X PUT -H "login: admin"  localhost:8080/api/wallet/balance -d {'"login":"admin","balance":1000 '} -v  

selects in postgres for check  
