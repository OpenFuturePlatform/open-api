# Open-widget
This is a payment widget that you can embed in your site for receiving payments from customers in the Crypto currency via [OPEN platform](https://api.openfuture.io/) API.

This widget requires [Metamask wallet](https://metamask.io/).

[![](http://joxi.net/VrwNex4COodeMA.png)]()

### Usage

width and height parameters.
You also need to put your scaffold address in the src element after  - "https://www.openfuture.io/widget/" 
The best solution is to do this dynamically, especially if you have created a lot of scaffolds.k


 ```html
 <iframe id="open-widget-iframe" src="https://www.openfuture.io/widget/your_scaffold_address_here" width="400" height="600" scrolling="no" frameborder="0" allowfullscreen></iframe>

 ```


The widget can contain a maximum of 9 fields of 3 types (string, number, boolean). 
Field names and type are set in a [personal OPEN-platform account](https://api.open-platform.zensoft.io/scaffolds/new) , before creating a smart contract.

