import { groupBy, mapValues } from 'lodash';
import { adaptScaffold } from './scaffold-adapter';

export const parseApiError = e => {
  const response = e.response || {};
  const status = response.status;
  if (!response.data) {
    return e;
  }
  const message = response.data.message || response.statusText;
  const groupedErrors = groupBy(e.response.data.errors, 'field');
  const adaptedErrors = adaptScaffold(groupedErrors);
  const errorList = mapValues(adaptedErrors, it => it.map(error => error.code));
  return { message: `${status}: ${message}`, errors: errorList };
};
