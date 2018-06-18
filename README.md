# OPEN Platform API [![Twitter Follow](https://img.shields.io/twitter/follow/openplatformico.svg?style=social&label=Follow)](https://twitter.com/openplatformico)

[![Release Version](https://img.shields.io/github/release/OpenFuturePlatform/open-api.svg?style=flat-square)](https://github.com/OpenFuturePlatform/open-api/releases)
[![Release Date](https://img.shields.io/github/release-date/OpenFuturePlatform/open-api.svg?style=flat-square&colorB=007EC6)](https://github.com/OpenFuturePlatform/open-api/releases)
[![Last Commit](https://img.shields.io/github/last-commit/OpenFuturePlatform/open-api.svg?style=flat-square&colorB=007EC6)](https://github.com/OpenFuturePlatform/open-api/commits)
[![Contributors](https://img.shields.io/github/contributors/OpenFuturePlatform/open-api.svg?style=flat-square&colorB=007EC6)](https://github.com/OpenFuturePlatform/open-api/contributors)
[![License](https://img.shields.io/github/license/OpenFuturePlatform/open-api.svg?style=flat-square)](https://github.com/OpenFuturePlatform/open-api)


This API is running on https://api.openfuture.io.

Documentation is available at https://docs.openfuture.io.

## Running the application

### Environment Variables
The OPEN Platform API uses several environment variables. All
variables are required.

#### Google service

* `GOOGLE_CLIENT_ID`
* `GOOGLE_CLIENT_SECRET`

This environment variables is required for app to use OAuth 2.0.
To get started using Gmail API, you need to first use the
[setup tool](https://console.developers.google.com/flows/enableapi?apiid=gmail&credential=client_key&pli=1),
which guides you through creating a project in the Google API Console,
enabling the API, and creating credentials.

1. From the Credentials page, click **Create credentials >
OAuth client ID** to create your OAuth 2.0 credentials or
**Create credentials > Service account key** to create a service account.
2. If you created an OAuth client ID, then select your application type.
3. Fill in the form and click **Create**.

Your application's client IDs and service account keys are now listed on the Credentials page. For details, click a client ID; parameters vary depending on the ID type, but might include email address, client secret, JavaScript origins, or redirect URIs.

#### PostgresSQL service

* `POSTGRES_HOST`

The hostname is installed in order to be able to integrate the
app with the database service.

* `POSTGRES_DB`

This environment variable need be used to define name for the database.

* `POSTGRES_USER`

This environment variable is used in conjunction with
*POSTGRES_PASSWORD* to set a user and its password. This variable will
create the specified user with superuser power.

* `POSTGRES_PASSWORD`

This environment variable needs for you to use the PostgreSQL database.
 This environment variable sets the superuser password for
PostgreSQL.

#### Ethereum client service

In order to begin to work with Ethereum blockchain you need to install
client service on any work machine.

* `INFURA_URL`

The INFURA_URL is address your work machine with port your client
service.

* `ETHEREUM_PRIVATE_KEY`

In order to use Ethereum blockchain you need to create **wallet**, where
the private key will be generated. The private key needs in order to
app can work on behalf of your wallet.

* `OPEN_TOKEN_ADDRESS`

Before working in the app you need to create **smart contract** in
Ethereum blockchain. After creating you get *OPEN_TOKEN_ADDRESS*.
With which app can check the transfers of clients.