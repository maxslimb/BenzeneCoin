const BenzeneToken = artifacts.require("./BenzeneToken.sol");
const BenzeneTokenSale = artifacts.require("./BenzeneTokenSale.sol");


module.exports = function (deployer) {
  deployer.deploy(BenzeneToken, 1000000).then(function(){
    //Token Price is 0.001 Ether
    var tokenPrice = 1000000000000000;
    return deployer.deploy(BenzeneTokenSale, BenzeneToken.address, tokenPrice);
  });
  
};
