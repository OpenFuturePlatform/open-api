# OPEN Platform API [![Twitter Follow](https://img.shields.io/twitter/follow/openplatform.svg?style=social&label=Follow)](https://twitter.com/openplatform)

[![Release Version](https://img.shields.io/github/release/OpenFuturePlatform/open-api.svg?style=flat-square)](https://github.com/OpenFuturePlatform/open-api/releases)
[![Release Date](https://img.shields.io/github/release-date/OpenFuturePlatform/open-api.svg?style=flat-square&colorB=007EC6)](https://github.com/OpenFuturePlatform/open-api/releases)
[![Last Commit](https://img.shields.io/github/last-commit/OpenFuturePlatform/open-api.svg?style=flat-square&colorB=007EC6)](https://github.com/OpenFuturePlatform/open-api/commits)
[![Contributors](https://img.shields.io/github/contributors/OpenFuturePlatform/open-api.svg?style=flat-square&colorB=007EC6)](https://github.com/OpenFuturePlatform/open-api/contributors)
[![License](https://img.shields.io/github/license/OpenFuturePlatform/open-api.svg?style=flat-square)](https://github.com/OpenFuturePlatform/open-api/blob/master/LICENSE.txt)


This API is running on https://api.openfuture.io.

Documentation is available at https://docs.openfuture.io.

## Running the application

### Environment Variables

The OPEN Platform API uses several environment variables. All variables are required.

#### Google service

* `GOOGLE_CLIENT_ID`
* `GOOGLE_CLIENT_SECRET`

These environment variables are required for your app to utilize OAuth 2.0. To get started using the Gmail API, you need to first use the [setup tool](https://console.developers.google.com/flows/enableapi?apiid=gmail&credential=client_key&pli=1), to register your application for the Gmail API in the Google API Console.  The setup tool will guide you through creating a project in the Google API Console, enabling the API, and creating authentication credentials. The setup process is easy and consists of the following three basic steps:

1. On the Credentials page, click **My Project** and select the project you want to use.
2. If you plan to create an **OAuth client ID**, click on the **OAuth consent screen** tab at the top of the **Credentials** page and fill out the form. You will need it in step 5.
3. Click on the **Credentials** tab at the top of the **Credentials** page.
4. Click the **Create credentials** button. From the drop-down list, select **OAuth client ID** if you want to create OAuth 2.0 credentials or select **Service account key** to create a service account.
5. If you chose to create an **OAuth client ID**, select your application type (e.g. Web application, iOS app, etc.). Otherwise, go to step 6.
6. Fill out the form and click the **Create** button.

Your application's client IDs and service account keys are now listed on the **Credentials** page.  You can click on the client ID to see the details for this account.  Depending on the type of ID you created, you may see your email address, the client secret key, JavaScript origins, or the redirect URIs.  Add the required information to your **GOOGLE_CLIENT_ID** and **GOOGLE_CLIENT_SECRET** environment variables.

#### PostgresSQL service

* `POSTGRES_HOST`

Configure the **POSTGRES_HOST** environment variable to point to the host where you are running your PostgresSQL server. This is required to connect your application to the database.

* `POSTGRES_DB`

Add the name of your PostgreSQL database to the **POSTGRES_DB** environment variable. This lets the application know which database to connect to.

* `POSTGRES_USER` & `POSTGRES_PASSWORD`

Set the **POSTGRES_USER** environment variable with the username you want to use to connect to the database.  Set the **POSTGRES_PASSWORD** environment variable that user's password. Using environment variables to configure user authentication to your database is more secure than placing this configuration in your code. **NOTE:** The user you specify in these two environment variables should be a superuser, otherwise you will run into problems.

#### Ethereum client service

In order to begin working with the Ethereum blockchain you will need to install a client, such as [Parity](https://github.com/paritytech/parity) or [Geth](https://geth.ethereum.org/downloads/). Here is a brief description of the advantages of each:

##### Parity
* Written in Rust.
* Includes a pruning algorithm to prevent hard drive usage from growing exponentially.
* Includes an easy-to-use browser-based GUI.
* Implements passive-mode, which reduces the load on your CPU and network.
* Warp sync allows you to sync your node from scratch in a matter of hours, as opposed to days.
* Uses the Kovan test network to help you test your application.
* More feature-complete than Geth.

##### Geth
* Written in Go.
* Considered to be the reference implementation of Ethereum.
* Uses the Rinkeby test network to help you test your application.

**NOTE:** Both Parity and Geth can be installed on macOS via Homebrew.  Ubuntu users can install Parity via apt. Please check to see if binary packages are available for your OS.

* `INFURA_URL`

The INFURA_URL environment variable should be set to the IP address of your local machine and should include the port the service is running on. For example, `http://10.150.0.4:8545`

* `ETHEREUM_PRIVATE_KEY`

In order to use the Ethereum blockchain you need to create a **wallet**. A private key will be created as part of the wallet-creation process.  You will need to include your private key in the **ETHEREUM_PRIVATE_KEY** environment variable in order for the system to be able to communicate with your wallet.

* `OPEN_TOKEN_ADDRESS`

Before working in the app you need to create a **smart contract** within Ethereum. After creating a **smart contract**, you will receive an **OPEN_TOKEN_ADDRESS**.  You will need to set the **OPEN_TOKEN_ADDRESS** to include this token in order for the system to check client transfers.
