# SIPV API

Backend service for SIPV project, running at http://sipv.gorecan.com:11111/api/v1/

## API Authorization

API accepts 2 types of requests. Requests that need to be authorized and public requests. Authorization is handled via a Barer token (JWT). For the request to be successfully authorized, it must contain a header with key value of "Authorization", and value of "Barer " + JWT. 

Header example:

```
"Authorization": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJz ... i2RiScp_z9yn78i0jkPZJt2DSxQ"
```

## How to obtain JWT token 

To obtain JWT token, you must be a registered user. See bellow how to register. To obtain a new JWT token, authentication with username and password is required.

Request example:
```
POST: http://sipv.gorecan.com:11111/api/v1/authenticate
BODY:
{
    "username": "<your-username>",
    "password": "<your-password>" 
}
```
Response:
```json
{
    "username": "<your-username>",
    "name": "<your-name>",
    "id": "e8d68143-d59b-41b6-8e76-eb8e6499ffff",
    "permissions": "{permissions:[PROFILE_PERMISSION]}",
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ ... 7m8__VAi9X7CoNPvfbm25UaRE_rh0frwdHGQ"
}
```
Once you obtain your JWT, it will be valid for 7 days.


## How to register

To register, follow the example bellow:

```
POST: http://sipv.gorecan.com:11111/api/v1/register
BODY:
{
    "firstName": "<your-name>",
    "lastName": "<your-last-name>",
    "username": "<your-username>",
    "password": "<your-password>"
}
```
Response:
```json
Successfully registered new user
```
Once successfully registered, you can obtain a JWT toke, to access API endpoints.


## Test your JWT

For now, only one API endpoint is supported. You can use it, to verify if your app is working propperly.
Example call for endpoint:
```
GET: http://sipv.gorecan.com:11111/api/v1/profile
```
Response:
```json
{
    "id": "e8d68143-d59b-41b6-8e76-eb8e6499ffff",
    "firstName": "Bine",
    "lastName": "Gorečan",
    "username": "bine",
    "passwordHash": "fe5f810b174e14898a74c322c6fc767eb997afaaca4de007ee4248d5a7066778",
    "permissions": "{permissions:[PROFILE_PERMISSION]}"
}
```


## Other

### Sessions
Server keeps sessions for each generated JWT token, therefore it is good practice to destroy sessions when a logout event occurs in client app.
Session destroy example:
```
DELETE: http://sipv.gorecan.com:11111/api/v1/session/destroy
```
Response:
```
Session destroyed.
```


### Activity
Server keeps a log of user activity.


# FOODS MANIPULATION
Foods are separated in two categories. Public ones and Personal ones. Personal are only accessable if user has PERSONAL_FOODS_PERMISSION. Public foods are accesable for viewing without permission, but for editinga, adding and deleting, user must have PUBLIC_FOODS_PERMISSION.

There are separate endpoints for public and personal foods:
```
http://sipv.gorecan.com:11111/api/v1/foods/personal
```
```
http://sipv.gorecan.com:11111/api/v1/foods/public
```

### CRUD for foods:

#### Adding:
```
POST: http://sipv.gorecan.com:11111/api/v1/foods/personal
BODY:
{
    "name": "Kinder jajčka",
    "kcal": 123,
    "foodType": "SWEETS"   <-- OPTIONAL PARAMETER
}
```

#### Updating:
```
PUT: http://sipv.gorecan.com:11111/api/v1/foods/personal
BODY:
{
    "id": "591633a0-1ac0-43ab-9064-15a3767fbb6f",
    "owner": "e8d68143-d59b-41b6-8e76-eb8e6499ffff",
    "name": "Kinder jajčka",
    "kcal": 123.0,
    "foodType": "SWEETS"
}
```

#### Deleting:
```
DELETE: http://sipv.gorecan.com:11111/api/v1/foods/personal/<id-of-food-here>
```

For public foods, just use the public foods endpoint. Note, that owner is allways null in public foods.

### Food type ENUM
Food type is optional, when adding a new food.
Supported types:
```
    NOT_SPECIFIED,
    VEGETABLES,
    FRUITS,
    NUTS,
    MEATS,
    SWEETS,
    PASTRY,
    FISH,
    CARBS
```