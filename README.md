# Simple Money Transfer RESTFULL API
A simple Java RESTful API for money transfers between accounts

## Technologies and Frameworks

- Spark Java
- MyBatis
- Spock (groovy based testing framework)
- H2 database
- Liquibase(Version control for Database)
- Jetty

## Installing And Running

install

>  mvn clean package 

Run

> mvn exec:java

## Rest Endpoints

 Endpoint Path | HTTP Method | Description
---|---|---
/account  |POST| create new account
/account?{status}  |GET| list all account. may be filtered with status parameter
/account/{id}  |GET| get accountby id
/account/{id}  |PUT| Update existing account. 404 if not found
/account/{id}/operations  |GET| get all operation of the account by account id
/account/transfer  |POST| transfer between account
/operation/{id}  |GET| get  operation by id
||
/user  |POST| create new user
/user  |PUT| Update existing user. 404 if not found
/user  |GET| list all user
/user/{id}  |GET| get user by id
/user/{id}/account/  |GET| get all account of the user by user id

## Sample Input and Output

### post(/account) #insert account

Input:
```json
 {
        "balance": 100,
        "owner": {"id"	:1},
        "currency": "TRY",
        "status": "ACTIVE"
    }
```
Output:

```json
{
    "createdDate": 1547503200000,
    "accountId": 9,
    "balance": 100,
    "owner": {
        "createdDate": 1547503200000,
        "id": 1,
        "name": "mfatihercik"
    },
    "currency": "TRY",
    "status": "ACTIVE"
}
```

### post(/account/transfer) #transfer between accounts

Input:
```json
{
"sourceAccountId": 1,
"destinationAccountId": 2,
"amount": 123.12
}
```
Output:

```json
[
    {
        "id": 11,
        "transactionId": "ce0365a4-d083-41bf-b1ec-b67b595669b3",
        "account": {
            "createdDate": 1547503200000,
            "accountId": 2,
            "balance": 223.12,
            "owner": {
                "createdDate": 1547503200000,
                "id": 1,
                "name": "mfatihercik"
            },
            "currency": "USD",
            "status": "ACTIVE"
        },
        "amount": -123.12,
        "type": "CREDIT"
    },
    {
        "id": 12,
        "transactionId": "ce0365a4-d083-41bf-b1ec-b67b595669b3",
        "account": {
            "createdDate": 1547503200000,
            "accountId": 1,
            "balance": -23.12,
            "owner": {
                "createdDate": 1547503200000,
                "id": 1,
                "name": "mfatihercik"
            },
            "currency": "TRY",
            "status": "ACTIVE"
        },
        "amount": 123.12,
        "type": "DEBIT"
    }
]
```

### get(/account?status=ACTIVE,PENDING) #list all ACTIVE and PENDING accounts

Output:

```json
[
    {
        "createdDate": 1547503200000,
        "accountId": 1,
        "balance": -23.12,
        "currency": "TRY",
        "status": "ACTIVE"
    },
    {
        "createdDate": 1547503200000,
        "accountId": 2,
        "balance": 223.12,
        "currency": "USD",
        "status": "ACTIVE"
    },
    {
        "createdDate": 1547503200000,
        "accountId": 3,
        "balance": 100,
        "currency": "TRY",
        "status": "PENDING"
    },   
]
```

### get(/account/1) #get account by id

Output:

```json
{
    "createdDate": 1547503200000,
    "accountId": 1,
    "balance": -23.12,
    "owner": {
        "createdDate": 1547503200000,
        "id": 1,
        "name": "mfatihercik"
    },
    "currency": "TRY",
    "status": "ACTIVE"
}
```

### get(/account/1/operations) #get account by id

Output:

```json
[
    
    
    {
        "createdDate": 1547503200000,
        "id": 8,
        "transactionId": "7",
        "amount": 150,
        "type": "DEBIT",
        "description": "debit fooo"
    },
    {
        "createdDate": 1547503200000,
        "id": 12,
        "transactionId": "ce0365a4-d083-41bf-b1ec-b67b595669b3",
        "amount": 123.12,
        "type": "DEBIT"
    }
]
```

### get(/user/1) #get user by id
```json
{
    "createdDate": 1547503200000,
    "id": 1,
    "name": "mfatihercik"
}
```


