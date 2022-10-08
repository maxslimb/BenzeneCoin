pragma solidity >=0.4.2;

import "./BenzeneToken.sol";

contract BenzeneTokenSale{
    address payable admin;
    BenzeneToken public tokenContract;
    uint256 public tokenPrice;
    uint256 public tokensSold;

    event Sell(address _buyer, uint256 _amount);


    constructor(BenzeneToken _tokenContract, uint256 _tokenPrice) public {
        admin = msg.sender;
        tokenContract = _tokenContract;
        tokenPrice = _tokenPrice;
    }

    //multipy function 
    function multiply(uint x, uint y) internal pure returns (uint z){
        require(y==0 || (z=x*y)/y==x);
    }
    //Buying Tokens
    function buyTokens(uint256 _numberOfTokens) public payable {
        
        
        //Require that value is equal to tokens
        require(msg.value == multiply(_numberOfTokens, tokenPrice));
        
        //Require that the contract has enough tokens
        require(tokenContract.balanceOf(address(this)) >= _numberOfTokens);
        
        //Require that a transfer is successful
        //buy functionality
        require(tokenContract.transfer(msg.sender, _numberOfTokens));
        
        tokensSold += _numberOfTokens;
        
        emit Sell(msg.sender, _numberOfTokens);
    }

    //Ending token BenzeneTokenSale
    function endSale() public {
        //Require only admin can do this
        require(msg.sender == admin);
        //Transfer remaining Benzene tokens to admin
        require(tokenContract.transfer(admin, tokenContract.balanceOf(address(this))));
        //transfer the balance to the admin
        admin.transfer(address(this).balance);
    }
}
