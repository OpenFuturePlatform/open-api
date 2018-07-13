export const getCurrentUserPath = () => '/api/users/current';
export const getGlobalPropertiesPath = () => '/api/properties';
export const getKeysPath = (key = '') => `/api/keys/${key}`;
export const getTemplatesPath = () => '/api/scaffolds/templates';
export const getScaffoldsPath = (address = '') => `/api/scaffolds/${address}`;
export const getScaffoldsSummaryPath = address => `/api/scaffolds/${address}/summary`;
export const getScaffoldTransaction = address => `/api/scaffolds/${address}/transactions`;
export const updateScaffoldTransactions = address => `/api/scaffolds/${address}/transactions/updates`;
export const getScaffoldDeactivateScaffoldPath = address => `/api/scaffolds/${address}/doDeactivate`;
export const getScaffoldDoCompile = () => '/api/scaffolds/doCompile';
export const getScaffoldDoDeploy = () => '/api/scaffolds/doDeploy';
export const getValidateUrlPath = () => '/api/validation/url';
export const getShareHoldersPath = (scaffoldAddress = '', holderAddress = '') =>
  `${getScaffoldsPath(scaffoldAddress)}/holders/${holderAddress}`;
