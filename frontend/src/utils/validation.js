import axios from 'axios';
import { solidityReservedWords } from '../const/solidity-reserved-words';
import { getValidateUrlPath } from './apiPathes';
import { t } from './messageTexts';
import web3 from '../utils/web3';

const urlErrorMessage = t('wrong url message');

export const validateWebHook = async url => await axios.post(getValidateUrlPath(), { url });

export const validateScaffoldProperties = values => {
  const scaffoldFieldsArrayErrors = [];
  const digitRegex = /[^0-9]/g;

  const propertyNames = values.map(it => it.name);

  values.forEach((field, fieldIndex) => {
    const scaffoldFieldsErrors = [];

    if (field.name) {
      if (field.name[0].match(/[a-z]/) === null) scaffoldFieldsErrors.push(t('first letter must be lowercase'));
      if (field.name.match(/^[a-zA-Z0-9_]+$/g) === null) {
        scaffoldFieldsErrors.push(t('special characters are forbidden'));
      }
      if (solidityReservedWords.includes(field.name))
        scaffoldFieldsErrors.push(t('field name is forbidden', field.name));
      if (propertyNames.filter(it => it === field.name).length > 1) scaffoldFieldsErrors.push(t('Name must be unique'));
    }

    const ifNumber = field.type === 'NUMBER';
    const regexTest = field.defaultValue === undefined ? false : digitRegex.test(field.defaultValue);

    if (ifNumber && regexTest) {
      scaffoldFieldsErrors.push(t('only integers'));
    }
    scaffoldFieldsArrayErrors[fieldIndex] = scaffoldFieldsErrors;
  });

  return scaffoldFieldsArrayErrors;
};

export const validateAddress = address => {
  let errors = [];
  if (!address.startsWith('0x')) errors.push(t('dev address should begin with 0x'));
  if (address.length !== 42) errors.push(t('dev address should has length 42'));
  try {
    web3.utils.toChecksumAddress(address);
  } catch (e) {
    errors.push(t('invalid address'));
  } finally {
    return errors;
  }
};

export const warn = values => {
  const warnings = {};
  const digitRegex = /[^0-9.]/g;
  const regexTest = values.fiatAmount === undefined ? false : digitRegex.test(values.fiatAmount);

  if (values.developerAddress) {
    warnings.developerAddress = validateAddress(values.developerAddress);
  }
  if (values.fiatAmount && regexTest) {
    warnings.fiatAmount = t('fiat should be a number');
  }
  return warnings;
};

const isUrl = str => {
  const regexp = /^((https?|ftp|smtp):\/\/)(www.)?[a-z0-9]+\.[a-z]+(\/[a-zA-Z0-9#/-]+\/?)*$/;
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

  if (!values.developerAddress) {
    errors.developerAddress = t('dev address is required');
  }
  if (!values.title) {
    errors.title = t('scaffold title is required');
  }
  if (!values.fiatAmount) {
    errors.fiatAmount = t('fiat is required');
  }
  if (!values.currency) {
    errors.currency = t('currency is required');
  }
  if (!values.name) {
    errors.name = t('application name is required');
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
