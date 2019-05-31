$(document).ready(function () {

    console.log("Hello SHOP.js");

    function AppViewModel(model) {
        this.model = model;
        var self = this;
        this.visibleFlagForBuyButton = ko.observable(false);
        this.items = ko.observableArray();
        this.desiredProduct = ko.observableArray();
        this.wrongGoods = ko.observableArray();

        this.toBasket = function(){
            this.visibleFlagForBuyButton(true);
            this.model.toBasket(this.desiredProduct());
        };

        this.buy = function(){
            this.model.buy(this.desiredProduct)
        };


        initialize = function () {
            self.desiredProduct(self.model.desiredProduct);
            self.items(self.model.items);
            self.wrongGoods(self.model.wrongGoods);
        };
    }

    var Model = function () {
        var self = this;
        this.desiredProduct = [];
        this.items = [];
        this.buyGoods = [];
        this.wrongGoods = [];

        this.toBasket = function (desiredProduct) {
            $.ajax({
                // url: '/navstore/shop/basket?desiredProduct='+desiredProduct,
                url: 'shop/basket?desiredProduct='+desiredProduct,
                method: 'GET',
                contentType: "application/json",
                error: function (xhr) {
                    alert(xhr.status+" - "+xhr.message);
                    console.log(xhr.status+" - "+xhr.message)
                },
                success: function (data) {
                    self.validGoods(data.replace(/[\s\[\]-]+/gi, '').split(","));
                }
            });
        };

        this.buy = function (desiredProduct) {
            $.ajax({
                // url: "/navstore/buyservice",
                url: "./buyservice",
                method: "post",
                data: {
                    "desiredProduct":desiredProduct
                },
                error: function(xhr) {
                    if(xhr.status===400){
                        // window.location = "/navstore/shop/failure";
                        window.location = "./shop/failure";
                    } else{
                        alert(xhr.status+" - "+xhr.message);
                    }
                },
                success: function(data, textStatus, xhr) {
                    if(xhr.status ===201){
                        window.location = "./shop/success"
                    }
                }
            });
        };

        this.validGoods = function (wrongGoodsPass) {
            self.buyGoods = [];
            self.wrongGoods = [];
            var wrong = [];
            for (var i = 0; i < self.desiredProduct.length;i++){
                for (var j=0; j<self.items.length;j++){
                    if (self.desiredProduct[i]===self.items[j].password){
                        var a=wrong.length;
                        for(var k=0; k<wrongGoodsPass.length; k++){
                            if(wrongGoodsPass[k]===self.items[j].password){
                                wrong.push(self.items[j]);
                                delete self.desiredProduct[i];
                            }
                        }
                        if (a === wrong.length){
                            self.buyGoods.push(self.items[j]);
                        }
                    }
                }
            }
            self.items = self.buyGoods;
            self.wrongGoods = wrong;
            self.desiredProduct = self.desiredProduct.filter(function (el) {
                return el != null;
            });
            initialize();
        };
    };

    var model = new Model();
    ko.applyBindings(new AppViewModel(model));

    $.ajax({
        // url: '/navstore/shop/items',
        url: './shop/items',
        method: 'GET',
        contentType: "application/json",
        error: function (xhr) {
            alert("At the moment, the list of goods is not available");
            console.log(xhr.status+" - "+xhr.message)
        },
        success: function (data) {
            model.wrongGoods = [];
            model.items = data;

            // add two non-existent goods
            /*model.items.push({
                name: "non-existent",
                password: "A66",
                price: 100.5
            });
            model.items.push({
                name: "non-existent",
                password: "B99",
                price: 90.5
            });*/
            initialize();
        }
    });


});