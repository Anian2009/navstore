# ** Getting started **
***

- ### Changing data directory

By default, the directory of data for your application is located at the root of the server "%Tomcat%\webapps\". You may change this directory.

 To change the data directory, follow these steps:
 - create a directory in the path that you want;
 - in the file "navstore.properties" at the root of the application you should write new directory. For example:


    boot.file.path=c:\\My\\new\\directory

 - create file "items.csv" in your directory and fill it data;
 - restart your application;

- ### Exeption: "No properties file."

 Сreate a "config.properties" file in the root application with the following content
    
    boot.file.path=/data

 Instead of '/ data' you can specify the directory where you want to place the data


- ### initializing "data/items.csv" file

 After first launch and visiting page "navstore/shop" there are created a file "data/items.csv" which initializing default values
    Sample:


    Anorak,A23,50.5
    Apron,A85,5.3
    Baseball cap,B12,5.0
    Belt,B15,10.2
    Blouse,B45,12.9
    Boots,B49,45.3
    Cardigan,C78,20.4
    Coat,C98,16.7
    Dress,D85,65.2

 After that, every 5 minutes the collection of items will be updated from this file.
 If the file "data / items.csv" is deleted or its contents will be empty collection of goods and this file will be initialized dafault values.


# ** APIdoc for Navstore **
***

- ### Go to the store page to create an order

GET `/navstore/shop`

Response 200 - html page

    <!DOCTYPE html>
    <html lang="en" xmlns:data-bind="http://www.w3.org/1999/xhtml">
    <head>
        <meta charset="UTF-8">
    
        <script language="JavaScript" type='text/javascript' src='../navstore/js/jquery-3.4.1.js'></script>
        <script language="JavaScript" type='text/javascript' src='../navstore/js/knockout-3.5.0.js'></script>
        <script language="JavaScript" type='text/javascript' src='../navstore/js/shop.js'></script>
    
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
        <link href="../navstore/css/some.css" type="text/css" rel="stylesheet">
    
        <title>Shop</title>
    </head>
    <body>
    <header class="w3-container w3-teal">
        <div><h2 class="w3-btn" onclick="location.href='/navstore'">NAVSTORE</h2></div>
        <div><h4 class="w3-btn" onclick="location.href='/shop'">Go shopping</h4></div>
    </header>
    <div id="cont" class="w3-container" style="display: flex; flex-direction: column; align-items: center">
        <h2>Product List</h2>
    
        <table class="w3-table-all w3-hoverable">
            <thead>
            <tr class="w3-light-grey">
                <th>Name</th>
                <th>Price</th>
                <th>Buy</th>
            </tr>
            </thead>
            <tbody data-bind="foreach: items, wrongGods">
            <tr>
                <td data-bind="text:name">Jill</td>
                <td data-bind="text:price">Smith</td>
                <td><input class="w3-check" type="checkbox"
                           data-bind="attr:{value: password}, checked: $parent.desiredProduct">
                    <label>select</label></td>
            </tr>
            </tbody>
            <tbody data-bind="foreach: wrongGoods">
            <tr id="badGoods" >
                <td data-bind="text:name">Jill</td>
                <td data-bind="text:price">Smith</td>
                <td>Product missing</td>
            </tr>
            </tbody>
        </table>
    </div>
    <div class="btnBlock">
        <button class="w3-btn w3-block w3-teal"
                data-bind="visible: visibleFlagForBuyButton()&desiredProduct().length > 0, click: buy">Buy
        </button>
    
        <button class="w3-btn w3-block w3-teal" data-bind="visible: desiredProduct().length > 0, click: toBasket">To Basket</button>
    </div>
    
    </body>
    </html>

- ### Get a list of all products available at this moment

GET `/navstore/shop/items`

                
Response 200 - example of available product list

    [
       {
          "name":"Anorak",
          "password":"A23",
          "price":50.5
       }, {
          "name":"Apron",
          "password":"A85",
          "price":5.3
       }, {
          "name":"Baseball cap",
          "password":"B12",
          "price":5.0
       }
    ]
                
Response 503 - The product list is currently unavailable.
    
***

- ### Validation of goods selected by the customer with a list of goods on the server

GET `/navstore/shop/basket?desiredProduct=goodsArticle_1,goodsArticle_2,...,goodsArticle_N`

                
Response 200 - product codes that are not on the server list

    [goodsArticle_1,goodsArticle_5]
                
***

- ### Creates an order (re-validates selected products)

POST `/navstore/buyservice`

    desiredProduct: goodsArticle_1,goodsArticle_2,goodsArticle_3

                
Response 201 - The order was accepted successfully

Response 400 - The product list contains non-existent goods
                
Response 500 - Order not accepted. Error creating order.

***

- ### Returns the html page with information about the successful creation of the order

GET `/navstore/shop/success`

Response 200 - html page

    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Success</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
        <link href="../css/some.css" type="text/css" rel="stylesheet">
    </head>
    <body>
    <header class="w3-container w3-teal" >
        <div><h2 class="w3-btn" onclick="location.href='/navstore'">NAVSTORE</h2></div>
        <div><h4 class="w3-btn" onclick="location.href='/navstore/shop'">Go shopping</h4></div>
    </header>
    
    <img src="../img/seccess.jpg" alt="Shopping">
    
    <article class="w3-container">
        <p>Our congratulations. Your order has been successfully accepted and will be processed as soon as possible. Thank
            you for being with us.</p>
    </article>
    
    <footer class="w3-container w3-teal">
        <h5>Navstore@some.com</h5>
    </footer>
    </body>
    </html>

***

- ### Returns the html page with information about the unsuccessful creation of the order

GET `/navstore/shop/failure`

Response 200 - html page

    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Failure</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
        <link href="../css/some.css" type="text/css" rel="stylesheet">
    </head>
    <body>
    
    <header class="w3-container w3-teal">
        <div><h2 class="w3-btn" onclick="location.href='/navstore'">NAVSTORE</h2></div>
        <div><h4 class="w3-btn" onclick="location.href='/navstore/shop'">Go shopping</h4></div>
    </header>
    
    <img src="../img/failure.jpg" alt="Shopping">
    
    <article class="w3-container">
        <p>Sorry, but something went wrong. Please try again later.</p>
    </article>
    
    <footer class="w3-container w3-teal">
        <h5>Navstore@some.com</h5>
    </footer>
    
    </body>
    </html>


