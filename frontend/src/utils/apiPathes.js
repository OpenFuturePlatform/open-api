export const getScaffoldsPath = (address = '') => `/api/scaffolds/${address}`;
export const getScaffoldsSummaryPath = address => `/api/scaffolds/${address}/summary`;
export const getScaffoldTransaction = address => `/api/scaffolds/${address}/transactions`;
export const getShareHoldersPath = (scaffoldAddress = '', holderAddress = '') =>
  `${getScaffoldsPath(scaffoldAddress)}/holders/${holderAddress}`;
