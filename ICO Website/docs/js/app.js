App = {
    web3Provider: null,
    contracts: {},
    account: '0x0',
    loading: false,
    tokenPrice: 1000000000000000,
    tokensSold: 0,
    tokensAvailable: 750000,

    init: function() {
        console.log("App initialized...")
        return App.initWeb3();
    },

    initWeb3: function() {
        if (typeof web3 !== 'undefined') {
          // If a web3 instance is already provided by Meta Mask.
          App.web3Provider = window.ethereum;
          web3 = new Web3(ethereum);
        } else {
          // Specify default instance if no web3 instance provided
          App.web3Provider = new Web3.providers.HttpProvider('http://localhost:7545');
          web3 = new Web3(App.web3Provider);
        }
        
        return App.initContracts();
    },
    initContracts: function() {
       
        $.getJSON("BenzeneTokenSale.json", function(benzeneTokenSale) {
            App.contracts.BenzeneTokenSale = TruffleContract(benzeneTokenSale);
            App.contracts.BenzeneTokenSale.setProvider(App.web3Provider);
            App.contracts.BenzeneTokenSale.deployed().then(function(benzeneTokenSale) {
                console.log("Benzene Token Sale Address:", benzeneTokenSale.address);
            });
        }).done(function() {
            $.getJSON("BenzeneToken.json", function(benzeneToken) {
                App.contracts.BenzeneToken = TruffleContract(benzeneToken);
                App.contracts.BenzeneToken.setProvider(App.web3Provider);
                App.contracts.BenzeneToken.deployed().then(function(benzeneToken) {
                    console.log("Benzene Token Address:", benzeneToken.address);
                }); 

                App.listenForEvents();
                return App.render();
            });
        })
    },

    //Listen for events emitted from the contract
    listenForEvents: function() {
        App.contracts.BenzeneTokenSale.deployed().then(function(instance) {
            instance.Sell({
                fromBlock:0,
               // toBlock: 'latest',  
            }, function(error, event){ 
                console.log("event triggereg", event);
                App.render();})
        })
    },

    render: function() {
        if(App.loading) {
            return;
        }
        App.loading = true;

        var loader = $('#loader');
        var content = $('#content');

        loader.show();
        content.hide();

        //load account data

        web3.eth.getCoinbase(function(err, account) {
            if(err == null) {
                console.log("account", account);
                App.account = account;
                $('#accountAddress').html("Your Account: " + account);
            }
        })

        //Load token sale contract
        App.contracts.BenzeneTokenSale.deployed().then(function(instance) {
            benzeneTokenSaleInstance = instance;
            return benzeneTokenSaleInstance.tokenPrice();
        }).then(function(tokenPrice) {
            App.tokenPrice = tokenPrice;
            $('.token-price').html(web3.fromWei(App.tokenPrice.toNumber(), "ether"));
            return benzeneTokenSaleInstance.tokensSold();
        }).then(function(tokensSold) {
            App.tokensSold = tokensSold.toNumber();
            $('.tokens-sold').html(App.tokensSold);
            $('.tokens-available').html(App.tokensAvailable);

            var progressPercent = (Math.ceil(App.tokensSold) / App.tokensAvailable) * 100;
            $('#progress').css('width', progressPercent + '%');

            //Load token contract
            App.contracts.BenzeneToken.deployed().then(function(instance) {
                benzeneTokenInstance = instance;
                return benzeneTokenInstance.balanceOf(App.account);
            }).then(function(balance) {
                $('.benzene-balance').html(balance.toString());

                App.loading = false;
                loader.hide();
                content.show(); 
            })
        });
    },

    buyTokens: function() {
        $('#content').hide();
        $('#loader').show();
        var numberOfTokens = $('#numberOfTokens').val();
        App.contracts.BenzeneTokenSale.deployed().then(function(instance) {
            return instance.buyTokens(numberOfTokens, {
                from: App.account,
                value: numberOfTokens * App.tokenPrice,
                gas: 500000
            });
        }).then(function(result) {
            console.log("Tokens bought...")
            $('from').trigger('reset') //reset number of tokens in form
            //Wait for Sell Event
        });
    }

}

$(function() {
    $(window).load(function() {
        App.init();
    })
});