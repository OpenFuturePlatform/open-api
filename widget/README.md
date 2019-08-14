# Open-widget
This is a payment widget that you can embed to your site for processing payments from customers in the Cryptocurrency via  [OPEN Platform](https://api.openfuture.io/) API.

This widget requires [Metamask Wallet](https://metamask.io/).

[![](http://joxi.net/VrwNex4COodeMA.png)]()

### How to use a widget

Paste the code below to a location at your site where you'd like to use this widget and set the width and height parameters.
You also have to put your scaffold address in the src element: 'https://api.openfuture.io/widget/your_scaffold_address'
The best solution is to do it dynamically, especially if you have created a lot of scaffolds.


 ```html
 <iframe id="open-widget-iframe" src="https://api.openfuture.io/widget/your_scaffold_address_here" width="400" height="600" scrolling="no" frameborder="0" allowfullscreen></iframe>

 ```


The widget can contain a maximum of 9 fields of 3 types (string, number, boolean). 
Field names and types are set in a [personal OPEN-platform account](https://api.openfuture.io/scaffolds/new) during a smart contract creation.

