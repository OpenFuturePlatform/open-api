// validation.js

const solidityReservedWords = ['address', 'contract', 'function', 'struct', 'uint', 'returns', 'abstract', 'after', 'case', 'catch', 'final', 'in', 'inline', 'interface', 'let', 'match', 'of', 'pure', 'relocatable', 'static', 'switch', 'try', 'type', 'typeof', 'view', 'index', 'storage', 'state', 'variable', 'mapping', 'block', 'coinbase', 'difficulty', 'number', 'block', 'number', 'timestamp', 'msg', 'data', 'gas', 'sender', 'value', 'now', 'gas', 'price', 'origin', 'keccak256', 'ripemd160', 'sha256', 'ecrecover', 'addmod', 'mulmod', 'cryptography', 'this', 'super', 'selfdestruct', 'balance', 'send'];

export const validateScaffoldProperties = values => {
  const scaffoldFieldsArrayErrors = [];
  const digitRegex = /[^0-9]/g;

  values.forEach((field, fieldIndex) => {
    const scaffoldFieldsErrors = [];

    if (field.property) {
      if (field.property[0].match(/[a-z]/) === null) scaffoldFieldsErrors.push('A property should begin with a lowercase letter');
      if (field.property.match(/[\s]/) !== null) scaffoldFieldsErrors.push('A property should not contain a space');
      if (solidityReservedWords.includes(field.property)) scaffoldFieldsErrors.push(`${field.property} is a reserved word, pick another property name.`);
    }

    const ifUint = field.datatype === 'uint';
    const regexTest = field.defaultValue === undefined ? false : digitRegex.test(field.defaultValue);

    if (ifUint && regexTest) {
      scaffoldFieldsErrors.push('Only integers are allowed in number datatype');
    }
    scaffoldFieldsArrayErrors[fieldIndex] = scaffoldFieldsErrors;
  });

  return scaffoldFieldsArrayErrors;
}

export const warn = values => {
  const warnings = {};
  const digitRegex = /[^0-9.]/g;
  const regexTest = values.fiatAmount === undefined ? false : digitRegex.test(values.fiatAmount);

  if (values.developerAddress) {
    warnings.developerAddress = [];
    if (!values.developerAddress.startsWith('0x')) warnings.developerAddress.push('A developer address should beging with 0x');
    if (values.developerAddress.length !== 42) warnings.developerAddress.push('A developer address should be 42 characters long');
  }
  if (values.fiatAmount && regexTest) {
    warnings.fiatAmount = 'Fiat amount should be a number';
  }
  return warnings;
}

export const validate = values => {
  const errors = {};
  errors.scaffoldFields = [];

  if (!values.developerAddress) {
    errors.developerAddress = 'A developer address is required.';
  }
  if (!values.scaffoldDescription) {
    errors.scaffoldDescription = 'A scaffold title is required.';
  }
  if (!values.fiatAmount) {
    errors.fiatAmount = 'Fiat Amount is required.';
  }

  if (values.scaffoldFields) {
    const scaffoldFieldErrors = {};
    values.scaffoldFields.forEach((field, fieldIndex) => {
      if (!field.property) scaffoldFieldErrors.property = 'required';
      if (!field.datatype) scaffoldFieldErrors.datatype = 'required';
      if (!field.defaultValue) scaffoldFieldErrors.defaultValue = 'required';
      errors.scaffoldFields[fieldIndex] = scaffoldFieldErrors;
    });
  }

  return errors;
}
