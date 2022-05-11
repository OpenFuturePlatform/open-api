export const getCurrentUserPath = () => '/api/users/current';
export const getGlobalPropertiesPath = () => '/api/properties';
export const getKeysPath = (key = '') => `/api/keys/${key}`;
export const getEthereumTemplatesPath = () => '/api/ethereum-scaffolds/templates';
export const getEthereumScaffoldsPath = (address = '') => `/api/ethereum-scaffolds/${address}`;
export const getEthereumScaffoldsSummaryPath = address => `/api/ethereum-scaffolds/${address}/summary`;
export const getEthereumScaffoldTransaction = address => `/api/ethereum-scaffolds/${address}/transactions`;
export const updateEthereumScaffoldTransactions = address => `/api/ethereum-scaffolds/${address}/transactions/updates`;
export const getEthereumScaffoldDoCompile = () => '/api/ethereum-scaffolds/doCompile';
export const getEthereumScaffoldDoDeploy = () => '/api/ethereum-scaffolds/doDeploy';
export const getValidateUrlPath = () => '/api/validation/url';
export const getEthereumShareHoldersPath = (scaffoldAddress = '', holderAddress = '') =>
  `${getEthereumScaffoldsPath(scaffoldAddress)}/holders/${holderAddress}`;
export const getOpenScaffoldsPath = () => '/api/open-scaffolds';
export const getGatewayApplicationsPath = (id='') => `/api/application/${id}`;
export const getGatewayApplicationWalletsPath = (gatewayId='') => `/api/application/wallet/${gatewayId}`;
export const getGatewayApplicationWalletExport = () => '/api/application/wallet/export/private';
export const getUserTokenPath = (id='') => `/api/token/${id}`;
