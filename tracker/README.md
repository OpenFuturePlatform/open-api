# Open-transaction-widget
This is a payment transaction track widget that you can embed to your site for tracking payments from customers.

[![](http://joxi.net/VrwNex4COodeMA.png)]()

### How to use a widget

Paste the code below to a location at your site where you'd like to use this widget and set the width and height parameters.
You also have to put your scaffold address in the src element: 'https://api.openfuture.io/widget/transactions/address/your_wallet_address'
The best solution is to do it dynamically, especially if you have created a lot of scaffolds.


 ```html
 <iframe id="open-widget-iframe" src="https://api.openfuture.io/widget/transactions/address/your_address_here" width="400" height="600" scrolling="no" frameborder="0" allowfullscreen></iframe>

 ```

