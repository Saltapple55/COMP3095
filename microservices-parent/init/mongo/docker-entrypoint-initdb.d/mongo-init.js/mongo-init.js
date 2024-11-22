//creates db and assign priviledges
print('START');
db=db.getSiblingDB('product-service');
//this is mongo syntax
db.createUser(
    {
     user: 'admin',
     pwd: 'pass',
     roles:   [{role:'readWrite', db:'product-service'}]
    }
);

db.createCollection('user')

print('END');