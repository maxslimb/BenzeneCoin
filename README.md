> # Welcome to Benzene Coin's Official Repository!

# Benzene Coin App
Benzene Coin is a Cryptocurrency Platform which helps users to transfer money easily also in OFFLINE MODE. We have developed an App and deployed our crypto on the Ethereum network!

## Features

1. Easily Buy Tokens through the Android App

2. Transfer Coins in OFFLINE Mode between two peers

3. ERC-20 Cryptocurrency deployed on the Network 



## Tech Stack

1. Solidity, AWS, Node.js
2. Android, Kotlin, XML

## Implementation
![Home Page](https://i.imgur.com/02HjMNt.jpeg)


![Success Payment](https://i.imgur.com/QwORkAH.jpeg)


![Benzene Coin](https://i.imgur.com/J5tKfiz.jpeg)


![Benzene Coin 2](https://i.imgur.com/BvS389o.jpeg)

## Contribution

 - [Kishan Patel](https://www.github.com/maxslimb) 
 - [Het Patel](https://github.com/het54) 
 - [Karan Patel](https://github.com/karanpokar)

## Flowchart

```mermaid
graph LR
A[Benzene Coin App] -- API  --> B((AWS EC2 Instance))


B --> C(Smart Contract with ERC20)
C --> B((AWS EC2 Instance)) 
C-->D{Ethereum Blockchain}
D-->C(Smart Contract with ERC20)
