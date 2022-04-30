const express = require('express');
const app = express();
const cors = require('cors');
const Web3 = require('web3');
const CONTACT_ABI_BT = require('./config');
const CONTACT_ABI_BTS = require('./config');
const CONTACT_ADDRESS_BT = require('./config');
const CONTACT_ADDRESS_BTS = require('./config');
const routes = require('./routes.js')
app.use(cors());
app.use(express.json());

if (typeof web3 !== 'undefined') {
        var web3 = new Web3(web3.currentProvider); 
} else {
        var web3 = new Web3(new Web3.providers.HttpProvider('https://rinkeby.infura.io/v3/03dfd526311a4ce3933474b193a25238'));
}
const accounts =  web3.eth.getAccounts();
const benzeneToken_Contract = new web3.eth.Contract(CONTACT_ABI_BT.CONTACT_ABI_BT, CONTACT_ADDRESS_BT.CONTACT_ADDRESS_BT,{from: "0xFdcd021B3103DBd26497DD46fa06619d2e07c51E"});
const benzeneTokenSale_Contract = new web3.eth.Contract(CONTACT_ABI_BTS.CONTACT_ABI_BTS, CONTACT_ADDRESS_BTS.CONTACT_ADDRESS_BTS);

routes(web3, app, accounts, benzeneToken_Contract,benzeneTokenSale_Contract);
app.listen(5000, () => {
    console.log('listening on port '+ (process.env.PORT || 3000));
});
