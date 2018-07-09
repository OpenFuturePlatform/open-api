import { validateWebHook } from '../actions/deploy-contract';
import { solidityReservedWords } from '../const/solidity-reserved-words';

const urlErrorMessage = 'Webhook needs to be url with format [protocol]://[url]/[path]';

export const validateScaffoldProperties = values => {
  const scaffoldFieldsArrayErrors = [];
  const digitRegex = /[^0-9]/g;

  const propertyNames = values.map(it => it.name);

  values.forEach((field, fieldIndex) => {
    const scaffoldFieldsErrors = [];

    if (field.name) {
      if (field.name[0].match(/[a-z]/) === null)
        scaffoldFieldsErrors.push('A property should begin with a lowercase letter');
      /* eslint-disable */
      if (field.name.match(/[\s\/\\]/) !== null)
        /* eslint-enable */
        scaffoldFieldsErrors.push('A property should not contain a space, / and \\');
      if (solidityReservedWords.includes(field.name))
        scaffoldFieldsErrors.push(`${field.name} is a reserved word, pick another property name.`);
      if (propertyNames.filter(it => it === field.name).length > 1) scaffoldFieldsErrors.push('Name must be unique');
    } else {
      // scaffoldFieldsErrors.push('Name is required');
    }

    // if (!field.type) {
    // scaffoldFieldsErrors.push('Type is required');
    // }

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
  if (!address.startsWith('0x')) errors.push('A developer address should begin with 0x');
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

const isUrl = str => {
  const regexp = /^((https?|ftp|smtp):\/\/)(www.)?[a-z0-9]+\.[a-z]+(\/[a-zA-Z0-9#]+\/?)*$/;
  return regexp.test(str);
};

export const asyncValidate = async values => {
  if (!values.webHook) {
    return;
  }

  try {
    return await validateWebHook(values.webHook);
  } catch (e) {
    throw { webHook: urlErrorMessage }; // eslint-disable-line
  }
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
  if (!values.title) {
    errors.title = 'A scaffold title is required.';
  }
  if (!values.fiatAmount) {
    errors.fiatAmount = 'Fiat Amount is required.';
  }
  if (!values.currency) {
    errors.currency = 'Currency is required.';
  }
  if (values.webHook && !isUrl(values.webHook)) {
    errors.webHook = urlErrorMessage;
  }

  if (values.properties) {
    const scaffoldFieldErrors = {};
    values.properties.forEach((field, fieldIndex) => {
      if (!field.name) scaffoldFieldErrors.name = 'required';
      if (!field.type) scaffoldFieldErrors.type = 'required';
      errors.properties[fieldIndex] = scaffoldFieldErrors;
    });
  }

  return errors;
};
