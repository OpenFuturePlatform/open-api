const solidityReservedWords = ['address', 'contract', 'function', 'struct', 'uint', 'returns', 'abstract', 'after', 'case', 'catch', 'final', 'in', 'inline', 'interface', 'let', 'match', 'of', 'pure', 'relocatable', 'static', 'switch', 'try', 'type', 'typeof', 'view', 'index', 'storage', 'state', 'variable', 'mapping', 'block', 'coinbase', 'difficulty', 'number', 'block', 'number', 'timestamp', 'msg', 'data', 'gas', 'sender', 'value', 'now', 'gas', 'price', 'origin', 'keccak256', 'ripemd160', 'sha256', 'ecrecover', 'addmod', 'mulmod', 'cryptography', 'this', 'super', 'selfdestruct', 'balance', 'send'];

export const validateScaffoldProperties = values => {
  const scaffoldFieldsArrayErrors = [];
  const digitRegex = /[^0-9]/g;

  values.forEach((field, fieldIndex) => {
    const scaffoldFieldsErrors = [];

    if (field.name) {
      if (field.name[0].match(/[a-z]/) === null) scaffoldFieldsErrors.push('A property should begin with a lowercase letter');
      if (field.name.match(/[\s]/) !== null) scaffoldFieldsErrors.push('A property should not contain a space');
      if (solidityReservedWords.includes(field.name)) scaffoldFieldsErrors.push(`${field.name} is a reserved word, pick another property name.`);
    }

    const ifNumber = field.type === 'NUMBER';
    const regexTest = field.defaultValue === undefined ? false : digitRegex.test(field.defaultValue);

    if (ifNumber && regexTest) {
      scaffoldFieldsErrors.push('Only integers are allowed in number datatype');
    }
    scaffoldFieldsArrayErrors[fieldIndex] = scaffoldFieldsErrors;
  });

  return scaffoldFieldsArrayErrors;
};

export const validateAddress = address => {
  let errors = [];
  if (!address.startsWith('0x')) errors.push('A developer address should beging with 0x');
  if (address.length !== 42) errors.push('A developer address should be 42 characters long');
  return errors;
};

export const warn = values => {
  const warnings = {};
  const digitRegex = /[^0-9.]/g;
  const regexTest = values.fiatAmount === undefined ? false : digitRegex.test(values.fiatAmount);

  if (values.developerAddress) {
    warnings.developerAddress = validateAddress(values.developerAddress);
  }
  if (values.fiatAmount && regexTest) {
    warnings.fiatAmount = 'Fiat amount should be a number';
  }
  return warnings;
};

const isUrl = (str) => {
  const regexp =  /^(?:(?:https?|ftp):\/\/)?(?:(?!(?:10|127)(?:\.\d{1,3}){3})(?!(?:169\.254|192\.168)(?:\.\d{1,3}){2})(?!172\.(?:1[6-9]|2\d|3[0-1])(?:\.\d{1,3}){2})(?:[1-9]\d?|1\d\d|2[01]\d|22[0-3])(?:\.(?:1?\d{1,2}|2[0-4]\d|25[0-5])){2}(?:\.(?:[1-9]\d?|1\d\d|2[0-4]\d|25[0-4]))|(?:(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)(?:\.(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)*(?:\.(?:[a-z\u00a1-\uffff]{2,})))(?::\d{2,5})?(?:\/\S*)?$/;
  return regexp.test(str);
};

export const validate = values => {
  const errors = {};
  errors.properties = [];

  if (!values.openKey) {
    errors.openKey = 'A Open Key is required.';
  }
  if (!values.developerAddress) {
    errors.developerAddress = 'A developer address is required.';
  }
  if (!values.scaffoldDescription) {
    errors.scaffoldDescription = 'A scaffold title is required.';
  }
  if (!values.fiatAmount) {
    errors.fiatAmount = 'Fiat Amount is required.';
  }
  if (values.webHook && !isUrl(values.webHook)) {
    errors.webHook = 'Webhook needs to be valid url';
  }

  if (values.properties) {
    const scaffoldFieldErrors = {};
    values.properties.forEach((field, fieldIndex) => {
      if (!field.property) scaffoldFieldErrors.property = 'required';
      if (!field.datatype) scaffoldFieldErrors.datatype = 'required';
      if (!field.defaultValue) scaffoldFieldErrors.defaultValue = 'required';
      errors.properties[fieldIndex] = scaffoldFieldErrors;
    });
  }

  return errors;
};
