export const t = (key, ...params) => {
  const getter = messageTextMap[key];
  if (typeof getter === 'function') {
    return getter(...params);
  }
  return getter || key;
};

const messageTextMap = {
  // deploy contract validation messages
  'wrong url message': 'Webhook needs to be url with format [protocol]://[url]/[path]',
  'first letter must be lowercase': 'A property should begin with a lowercase letter',
  'space forbidden': 'A property should not contain a space, / and \\',
  'field name is forbidden': fieldName => `${fieldName} is a reserved word, pick another property name.`,
  'Name must be unique': 'Name must be unique',
  'only integers': 'Only integers are allowed in number datatype',
  'dev address should begin with 0x': 'A developer address should begin with 0x',
  'dev address should has length 42': 'A developer address should be 42 characters long',
  'fiat should be a number': 'Fiat amount should be a number',
  'fiat is required': 'Fiat Amount is required.',
  'open key is required': 'A Open Key is required.',
  'dev address is required': 'A developer address is required.',
  'scaffold title is required': 'A scaffold title is required.',
  'special characters are forbidden': 'Special characters are forbidden',
  'currency is required': 'Currency is required.',
  'low balance': 'Minimum balance: 0,0087 Eth. Change MetaMask account or top up the balance.',
  'invalid address': 'Invalid Eherium Address',

  // deploy contract process messages
  'it may take a while': 'PS: Please be patient this may take a while...',
  'scaffold deployed': 'Congratulations your scaffold is deployed.',
  'is deploying': 'Open is deploying your scaffold on-chain. Please be patient this may take a while...',

  // MetaMask error and warning messages
  'MM not found': 'Install MetaMask and refresh page',
  'choose target network': targetNetwork => `Choose ${targetNetwork}`,
  'log in to MM': 'Log in to MetaMask',
  'MM have no min tokens': minDeposit =>
    `MetaMask account have no OPEN Tokens. Select account with ${minDeposit} OPEN tokens or top up the balance.`,

  // contract activation
  'scaffold not activated': 'Your scaffold is created but is inactive.',
  'to activate you need tokens': minDeposit =>
    `To activate your scaffold you need to have ${minDeposit} OPEN Tokens on scaffold contract.`,
  'sure to top up balance': 'You are about to top up Scaffold Token Balance. Proceed?',
  'sure to activate scaffold': 'You are about to activate the Scaffold. Proceed?',

  // contract deactivation
  'sure to deactivate scaffold': 'You are about to deactivate the Scaffold. Are you sure?',
  'tokens will be returned to dev address': 'Note: Tokens will be send to Developer Address:',

  // shareholders
  'sure to remove shareholder': 'You are about to remove the Share Holder. Are you sure?',

  // key management
  'sure to deactivate key': 'You are about to deactivate the key. Are you sure?',

  // other
  'session was expired': 'Your session has expired. Please login again.'
};
